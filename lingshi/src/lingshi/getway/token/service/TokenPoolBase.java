package lingshi.getway.token.service;

import lingshi.getway.token.model.TokenBase;

/**
 * 抽象Token池
 * 
 * @author caich
 *
 */
public abstract class TokenPoolBase {

	/**
	 * 通过Token获取tokenBase对象
	 * 
	 * @param token
	 * @return
	 */
	public abstract TokenBase get(String token);

	/**
	 * 插入
	 * 
	 * @param baseToken
	 */
	public abstract void add(TokenBase baseToken);

	/**
	 * 修改
	 * 
	 * @param baseToken
	 */
	public abstract void update(TokenBase baseToken);

	/**
	 * 移除
	 * 
	 * @param token
	 */
	public abstract void delete(String token);
}
