package lingshi.gateway.token.strategy;

import lingshi.gateway.token.LingShiTokenEnum.TokenStatus;
import lingshi.gateway.token.model.TokenBase;

public interface TokenStrategy {

	/**
	 * 创建一个新的token
	 * 
	 * @param data
	 * @return
	 */
	TokenBase create(Object data);
	
	/**
	 * 获取token中存储的数据
	 * 
	 * @param token
	 * @return
	 */
	Object getData(String token);

	/**
	 * 修改token
	 * 
	 * @param token
	 * @param data
	 */
	void update(String token, Object data);

	/**
	 * 刷新token的过期时间
	 * 
	 * @param token
	 */
	void refreshExp(String token);

	/**
	 * 移除token
	 * 
	 * @param token
	 */
	void remove(String token);
	
	/**
	 * 校验token
	 * 
	 * @param token
	 * @return
	 */
	TokenStatus check(String token);
}
