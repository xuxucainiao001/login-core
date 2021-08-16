package cn.toseektech.login.core;

/**
 * 需要用户具体的实现
 */
import cn.toseektech.login.dto.LoginUser;

public interface UserLoginService {
	
	/**
	 * 
	 * @param redirectCode
	 * @return  若未登录 返回null，，若已经登录则返回更新的 LoginUser
	 */
	public LoginUser getUserFromFirstLogin(String redirectCode);
	
	/**
	 * 
	 * @param loginUser
	 * @return  若未登录返回null，若已经登录则返回更新的 LoginUser
	 */
	public LoginUser getUserIfLogin(Long userId);
	   
    /**
     * 用户登出
     * @param loginUser
     */
    public void loginOut(LoginUser loginUser);
    
  
    
    
}
