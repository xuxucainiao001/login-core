package dongxun.com.login.utils;

import java.util.Date;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import dongxun.com.login.LoginConstants;
import dongxun.com.login.LoginProperties;
import dongxun.com.login.dto.LoginUser;


/*
 *   iss: jwt签发者
     sub: 面向的用户(jwt所面向的用户)
     aud: 接收jwt的一方
     exp: 过期时间戳(jwt的过期时间，这个过期时间必须要大于签发时间)
     nbf: 定义在什么时间之前，该jwt都是不可用的.
     iat: jwt的签发时间
     jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。

*/

public class JwtUtils {
	
	protected  Logger logger =LoggerFactory.getLogger(JwtUtils.class);
	
    
	@Resource
	private LoginProperties loginProperties;
	
    public void setLoginProperties(@Valid LoginProperties loginProperties) {
		this.loginProperties = loginProperties;
	}
	
    /**
           * 创建jwt token
     * @param loginUser
     * @return
     */
	public  String  createJwtToken(LoginUser loginUser) {
		String token = JWT.create()
				.withExpiresAt(DateUtils.addMilliseconds(new Date(), loginProperties.getValidSeconds()))
				.withIssuedAt(new Date())
				.withAudience(loginUser.getUsername())
				.withIssuer(loginProperties.getIssuer())
				.withSubject(loginProperties.getSubject())
				.withClaim(LoginConstants.USER_INFO_KEY, JSON.toJSONString(loginUser))
				.sign(Algorithm.HMAC512(loginProperties.getSecret()));
		if(logger.isDebugEnabled()) {
			logger.debug("{} 用户生成jwt的token:{}",loginUser,token);
		}
		return token;

	}
	
	/**
	  * 验证jwt token
	 * @param token
	 * @return
	 */
	public void verifyJwtToken(String token) {
		JWT.require(Algorithm.HMAC512(loginProperties.getSecret()))
				.build()
				.verify(token);
	}
	
	/**
	  * 解析jwt token
	 * @param token
	 * @return
	 */
	public DecodedJWT parseJwt(String token) {
		return JWT.decode(token);
	}

	
	
}
