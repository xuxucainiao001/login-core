package dongxun.com.login;

import javax.validation.constraints.NotEmpty;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class LoginProperties {
	
	public static final String PREFIX="dongxun.login";
	
	@NotEmpty
	private String issuer;
	
	@NotEmpty
	private String subject;
	
	@NotEmpty
	private String secret;
	
	/*登出请求地址*/
	@NotEmpty
	private String loginOutUrl;
	
	/*验证是否登录的请求地址*/
	@NotEmpty
	private String verifyLoginUrl;
	
	/*验证是否登录的标识符请求地址*/
	@NotEmpty
	private String verifyLoginCodeUrl;
	
	/*允许最大token过期时间，超过该时间则自动登出 */
	private Integer allowMaxExpiredSeconds = 600;
	
	/*jwt的token有效期 */
	private Integer validSeconds = 600;
	
	/*登录是否可用*/
	private boolean enable = true;

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
		
	public Integer getAllowMaxExpiredSeconds() {
		return allowMaxExpiredSeconds;
	}

	public void setAllowMaxExpiredSeconds(Integer allowMaxExpiredSeconds) {
		this.allowMaxExpiredSeconds = allowMaxExpiredSeconds;
	}
	
	public String getLoginOutUrl() {
		return loginOutUrl;
	}

	public void setLoginOutUrl(String loginOutUrl) {
		this.loginOutUrl = loginOutUrl;
	}

	public String getVerifyLoginUrl() {
		return verifyLoginUrl;
	}

	public void setVerifyLoginUrl(String verifyLoginUrl) {
		this.verifyLoginUrl = verifyLoginUrl;
	}
	
	public Integer getValidSeconds() {
		return validSeconds;
	}

	public void setValidSeconds(Integer validSeconds) {
		this.validSeconds = validSeconds;
	}
	

	public String getVerifyLoginCodeUrl() {
		return verifyLoginCodeUrl;
	}

	public void setVerifyLoginCodeUrl(String verifyLoginCodeUrl) {
		this.verifyLoginCodeUrl = verifyLoginCodeUrl;
	}
	

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public static String getPrefix() {
		return PREFIX;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
