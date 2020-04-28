package dongxun.com.login.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * SSO登录重定向后返回用于验证的CODE
 * @author xuxu
 *
 */
public class RedirectCode {
	
	/*唯一标识符*/
	private String uuid;
	
	/*过期时间*/
	private Long expiredAt;
	
	/*code发布来源*/
	private Integer source;
	
	/*用户账户*/
	private String userName;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(Long expiredAt) {
		this.expiredAt = expiredAt;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
