package lingshi.gateway.token.strategy;

import java.util.Date;

import lingshi.gateway.factory.TokenPoolFactory;
import lingshi.gateway.token.LingShiTokenEnum.TokenStatus;
import lingshi.gateway.token.model.TokenBase;
import lingshi.gateway.token.model.UserToken;
import lingshi.gateway.token.service.TokenMgrService;
import lingshi.gateway.token.service.TokenPoolBase;
import lingshi.gateway.token.service.impl.TokenMgrServiceMd5Impl;
import lingshi.model.LingShiConfig;
import lingshi.utilities.DateUtil;

public class UserTokenStrategy implements TokenStrategy {

	private TokenPoolBase pool;
	private TokenMgrService tokenMgrService;

	private UserTokenStrategy() {
		this.pool = TokenPoolFactory.getTokenPool();
		this.tokenMgrService = new TokenMgrServiceMd5Impl();
	}

	public static UserTokenStrategy get() {
		return new UserTokenStrategy();
	}

	@Override
	public TokenBase create(Object data) {
		String newToken = tokenMgrService.createTokenStr(LingShiConfig.getInstance().getAppKey());
		UserToken userToken = new UserToken();
		userToken.setToken(newToken);
		userToken.setData(data);
		userToken.setExp(new Date());
		pool.add(userToken);
		return userToken;
	}

	@Override
	public void update(String token, Object data) {
		UserToken userToken = (UserToken) pool.get(token);
		if (userToken != null) {
			userToken.setData(data);
			pool.update(userToken);
		}
	}

	@Override
	public void refreshExp(String token) {
		TokenBase tokenBase = pool.get(token);
		if (tokenBase != null) {
			int exp = LingShiConfig.getInstance().getTokenExp();
			tokenBase.setExp(DateUtil.addMilliSecond(exp));
		}
	}

	@Override
	public void remove(String token) {
		pool.delete(token);
	}

	@Override
	public Object getData(String token) {
		UserToken userToken = (UserToken) pool.get(token);
		return userToken == null ? null : userToken.getData();
	}

	@Override
	public TokenStatus check(String token) {
		return tokenMgrService.tokenCheck(token);
	}

}
