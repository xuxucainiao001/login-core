package cn.toseektech.login.core;

import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = LoginProperties.PREFIX)
public class LoginProperties {
	
	public static final String PREFIX="login.config";
	

	private String issuer = "";
	

	private String subject = "";
	

	private String secret;
	
	
	/*允许最大token过期时间，超过该时间则自动登出 */
	private Integer allowMaxExpiredSeconds = 600;
	
	/*jwt的token有效期 */
	private Integer validSeconds = 600;

	/**
	 * 需要登录的路径配置
	 */
	private Set<String> paths;
	
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
	
	
	public Integer getValidSeconds() {
		return validSeconds;
	}

	public void setValidSeconds(Integer validSeconds) {
		this.validSeconds = validSeconds;
	}
	

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public Set<String> getPaths() {
		return paths;
	}

	public void setPaths(Set<String> paths) {
		this.paths = paths;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
