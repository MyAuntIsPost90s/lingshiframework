package lingshi.gateway.token.model;

import java.util.Date;

/**
 * 基本Token
 * 
 * @author caich
 *
 */
public class TokenBase {

	/**
	 * token字符
	 */
	private String token;

	/**
	 * 过期时间
	 */
	private Date exp;

	public Date getExp() {
		return exp;
	}

	public void setExp(Date exp) {
		this.exp = exp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
