package dongxun.com.login.intercepters;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import dongxun.com.login.LoginConstants;
import dongxun.com.login.LoginProperties;
import dongxun.com.login.annotations.Login;
import dongxun.com.login.dto.LoginUser;
import dongxun.com.login.exceptions.LoginException;
import dongxun.com.login.utils.JwtUtils;
import dongxun.com.login.utils.LoginHttpUtils;
import dongxun.com.login.utils.LoginUtils;

public class LoginCheckInterceptor extends HandlerInterceptorAdapter {

	private static Logger logger = LoggerFactory.getLogger(LoginCheckInterceptor.class);

	private ExecutorService executor = Executors.newFixedThreadPool(1, r -> {
		Thread t = new Thread(r);
		t.setName("Login-ThreadPool-Executor");
		return t;
	});


	@Resource
	private JwtUtils jwtUtils;

	@Resource
	private LoginProperties loginProperties;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(!loginProperties.isEnable()) {
			return true;
		}
		//仅拦截@Login注解的controller
		Login logoin=HandlerMethod.class.cast(handler).getMethodAnnotation(Login.class);
		if(Objects.isNull(logoin)) {
			return true;
		}
		String token = request.getHeader(LoginConstants.TOKEN_NAME);
		if (StringUtils.isBlank(token)) {
			//若head中没有token,则判断是否为首次验证，此时从Paramter中取出标识符
			String code = request.getParameter(LoginConstants.SSO_REDIRECT_CODE);
			if(StringUtils.isBlank(code)) {
				if(logger.isDebugEnabled()) {
					logger.debug("用户未登录");
				}			
				return false;
			}
			LoginUser loginUser= LoginHttpUtils.verifyCode(code, loginProperties.getVerifyLoginCodeUrl());
			if(logger.isDebugEnabled()) {
				logger.debug("SSO验证结果，code:{},loginUser:{}",code,loginUser);
			}
			if(loginUser.getIsLogin()) {
				createJwt(loginUser, response);
				return true;
			}
			return false;
		}
		DecodedJWT jwt = null;
		try {
			jwt = jwtUtils.parseJwt(token);
			jwtUtils.verifyJwtToken(token);
			String loginUserJson = jwt.getClaim(LoginConstants.USER_INFO_KEY).asString();
			LoginUtils.setLoginUser(JSON.parseObject(loginUserJson, LoginUser.class));
			if(logger.isDebugEnabled()) {
				logger.debug("当前登录的用户信息:{}",loginUserJson);
			}
			return true;
		} catch (TokenExpiredException e) {
			if (logger.isDebugEnabled()) {
				logger.debug("token已经过期 :{}",jwt);
			}
			if (Objects.isNull(jwt)) {
				throw new LoginException("JWT解析不能为 null !");
			}
			// token过期超过指定时间 直接登出
			String loginUserJson = jwt.getClaim(LoginConstants.USER_INFO_KEY).asString();
			if (new Date().after(
					DateUtils.addMilliseconds(jwt.getExpiresAt(), loginProperties.getAllowMaxExpiredSeconds()))) {
				executor.submit(() -> {
					         LoginUser loginOutUser=LoginHttpUtils
						           .loginOut(JSON.parseObject(loginUserJson, LoginUser.class),
						            loginProperties.getLoginOutUrl());
					         if(loginOutUser.getIsLogin()&&logger.isDebugEnabled()) {
						        logger.debug("用户已经登出：{}",loginOutUser);
					         }
					      });							        						
				return false;
			}
			// 去sso验证用户是否已经登录
			LoginUser loginUser=LoginHttpUtils.verifyLogin(JSON.parseObject(loginUserJson, LoginUser.class), loginProperties.getVerifyLoginUrl());
			if(logger.isDebugEnabled()) {
				logger.debug("SSO验证结果：{}",loginUser);
			}
			if(loginUser.getIsLogin()) {
				// 更新用户信息重新创建jwt
				createJwt(loginUser,response);
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error("JWT解析 / 验证发生异常：{}", e);
			return false;
		}

	}
	
	private void createJwt(LoginUser loginUser,HttpServletResponse response) {
		String newToken=jwtUtils.createJwtToken(loginUser);
		if(logger.isDebugEnabled()) {
			logger.debug("loginUser新token:{}：{}",loginUser,newToken);
		}		
		response.setHeader("token", newToken);
		LoginUtils.setLoginUser(loginUser);
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		//移除当前登录用户
		LoginUtils.removeLoginUser();
	}

}
