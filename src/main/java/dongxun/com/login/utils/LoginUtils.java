package dongxun.com.login.utils;

import dongxun.com.login.dto.LoginUser;
import dongxun.com.login.exceptions.LoginException;

public class LoginUtils {

	private static ThreadLocal<LoginUser> tl = new ThreadLocal<>();

	private LoginUtils() {
	}

	public static LoginUser getLoginUser() {
		LoginUser loginUser = tl.get();
		if (loginUser == null) {
			throw new LoginException("没有获取到登录用户信息");
		}
		return loginUser;
	}

	public static void setLoginUser(LoginUser loginUser) {
		tl.set(loginUser);
	}
	
	public static void removeLoginUser() {
		tl.remove();
	}

}
