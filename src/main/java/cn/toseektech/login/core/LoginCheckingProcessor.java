package cn.toseektech.login.core;

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
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import cn.hutool.json.JSONUtil;
import cn.toseektech.login.dto.LoginUser;
import cn.toseektech.login.exceptions.LoginException;
import cn.toseektech.login.utils.JwtUtils;

public class LoginCheckingProcessor implements CheckingProcessor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private UserLoginService userLoginService;

	@Resource
	private JwtUtils jwtUtils;

	@Resource
	private LoginProperties loginProperties;

	private ExecutorService executor = Executors.newFixedThreadPool(1, r -> {
		Thread t = new Thread(r);
		t.setName("Login-ThreadPool-Executor");
		return t;
	});

	@Override
	public boolean check(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(LoginConstants.TOKEN_NAME);
		if (StringUtils.isBlank(token)) {
			// 若head中没有token,则判断是否为首次验证，此时从Paramter中取出标识符code
			String code = request.getParameter(LoginConstants.SSO_REDIRECT_CODE);
			if (StringUtils.isBlank(code)) {
				logger.debug("用户未登录");
				return false;
			}
			logger.debug("去SSO验证code:{} ", code);
			LoginUser loginUser = userLoginService
					.getUserFromFirstLogin(code);
			
			if (loginUser != null) {
				createJwtAndSaveUser(loginUser,response);
				return true;
			}
			return false;
		}
		DecodedJWT jwt = null;
		try {
			jwt = JwtUtils.parseJwt(token);
			jwtUtils.verifyJwtToken(token);
			String loginUserJson = jwt.getClaim(LoginConstants.USER_INFO_KEY).asString();
			logger.debug("当前登录的用户信息:{}", loginUserJson);
			return true;
		} catch (TokenExpiredException e) {
			logger.debug("token已经过期 :{}", jwt);
			if (Objects.isNull(jwt)) {
				throw new LoginException("JWT解析不能为 null !");
			}
			// token过期超过指定时间 直接登出
			String loginUserJson = jwt.getClaim(LoginConstants.USER_INFO_KEY).asString();
			if (new Date().after(
					DateUtils.addSeconds(jwt.getExpiresAt(), loginProperties.getAllowMaxExpiredSeconds()))) {
				executor.submit(() -> userLoginService.loginOut(JSONUtil.parse(loginUserJson).toBean(LoginUser.class)));
				return false;
			}
			// 去sso验证用户是否已经登录
			LoginUser loginUser = userLoginService
					.getUserIfLogin(JSONUtil.parse(loginUserJson).toBean(LoginUser.class).getId());
			// 更新用户信息重新创建jwt
			if (loginUser != null) {
				createJwtAndSaveUser(loginUser,response);
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error("JWT解析或验证发生异常：", e);
			return false;
		}

	}

	private void createJwtAndSaveUser(LoginUser loginUser,HttpServletResponse response) {
		String newToken = jwtUtils.createJwtToken(loginUser);
		logger.debug("loginUser新token:{}：{}", loginUser, newToken);
		response.setHeader(LoginConstants.TOKEN_NAME, newToken);	
	}
	

	@Override
	public boolean check(ServerHttpRequest request, ServerHttpResponse response) {
		String token = request.getHeaders().getFirst(LoginConstants.TOKEN_NAME);
		if (StringUtils.isBlank(token)) {
			// 若head中没有token,则判断是否为首次验证，此时从Paramter中取出标识符code
			String code = request.getQueryParams().getFirst(LoginConstants.SSO_REDIRECT_CODE);
			if (StringUtils.isBlank(code)) {
				logger.debug("用户未登录");
				return false;
			}
			logger.debug("去SSO验证code:{} ", code);
			LoginUser loginUser = userLoginService
					.getUserFromFirstLogin(code);
			
			if (loginUser != null) {
				createJwtAndSaveUser(loginUser,response);
				return true;
			}
			return false;
		}
		DecodedJWT jwt = null;
		try {
			jwt = JwtUtils.parseJwt(token);
			jwtUtils.verifyJwtToken(token);
			LoginUser loginUser = JwtUtils.parseUserInfo(token);
			logger.debug("当前登录的用户信息:{}", loginUser);
			return true;
		} catch (TokenExpiredException e) {
			logger.debug("token已经过期 :{}", jwt);
			if (Objects.isNull(jwt)) {
				throw new LoginException("JWT解析不能为 null !");
			}
			// token过期超过指定时间 直接登出
			LoginUser loginUser = JwtUtils.parseUserInfo(token);
			if (new Date().after(
					DateUtils.addSeconds(jwt.getExpiresAt(), loginProperties.getAllowMaxExpiredSeconds()))) {
				executor.submit(() -> userLoginService.loginOut(loginUser));
				return false;
			}
			// 去sso验证用户是否已经登录
			LoginUser user = userLoginService
					.getUserIfLogin(loginUser.getId());
			// 更新用户信息重新创建jwt
			if (user != null) {
				createJwtAndSaveUser(user,response);
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error("JWT解析或验证发生异常,jwt：", e);
			logger.error("JWT解析或验证发生异常：", e);
			return false;
		}
	}
	
	private void createJwtAndSaveUser(LoginUser loginUser,ServerHttpResponse response) {
		String newToken = jwtUtils.createJwtToken(loginUser);
		logger.debug("loginUser新token:{}：{}", loginUser, newToken);
		response.getHeaders().set(LoginConstants.TOKEN_NAME, newToken);
	}

}
