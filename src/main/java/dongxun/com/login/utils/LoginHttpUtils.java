package dongxun.com.login.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import dongxun.com.login.LoginConstants;
import dongxun.com.login.dto.LoginUser;
import dongxun.com.login.exceptions.LoginException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
  *  登录HTTP验证工具类
 * @author xuxu
 *
 */
public class LoginHttpUtils {
	
	
	private  static Logger logger = LoggerFactory.getLogger(LoginHttpUtils.class);
	

	private LoginHttpUtils() {
	}
	
	/**
	  * 登出请求
	 * @param loginUser
	 * @param url
	 * @return
	 */
	public static LoginUser loginOut(LoginUser loginUser, String url) {
		return postRequest(loginUser,url,LoginUser.class);
	}
    
	/**
	   * 验证用户是否登录
	 * @param loginUser
	 * @param url
	 * @return
	 */
	public static LoginUser verifyLogin(LoginUser loginUser, String url) {
		return postRequest(loginUser,url,LoginUser.class);
	}
	
	/**
	  *  验证登录code
	 * @param code
	 * @param url
	 * @return
	 */
	public static LoginUser verifyCode(String code,String url) {
		Map<String ,String> param=new HashMap<>();
		param.put(LoginConstants.SSO_REDIRECT_CODE, code);
		return postRequest(param,url,LoginUser.class);
	}

	
	/**
	 * post请求公用方法
	 * @param <R>
	 * @param <T>
	 * @param param
	 * @param url
	 * @param returnType
	 * @return
	 */
	private static <R,T> R postRequest(T param, String url,Class<R> returnType) {
		try {
			OkHttpClient client = new OkHttpClient();
			RequestBody requestBody = RequestBody
					.create(JSON.toJSONString(param)
							.getBytes());
			Request request = new Request.Builder()
					.url(url)
					.post(requestBody)
					.addHeader("Content-Type", "application/json")
					.build();		
			Response response = client.newCall(request).execute();
			String json=response.body().string();
			return JSON.parseObject(json, returnType);
		} catch (IOException e) {
			logger.error("LoginHttpUtils调用发生异常：{}",e);
			throw new LoginException("验证登录信息异常");
		}
		
	}

}
