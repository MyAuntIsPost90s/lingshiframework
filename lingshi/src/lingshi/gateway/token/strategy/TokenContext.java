package lingshi.gateway.token.strategy;

import lingshi.gateway.token.LingShiTokenEnum.TokenStatus;
import lingshi.gateway.token.model.TokenBase;

public class TokenContext implements TokenStrategy {

	private TokenStrategy strategy;

	public TokenContext(TokenStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public TokenBase create(Object data) {
		return strategy.create(data);
	}

	@Override
	public void update(String token, Object data) {
		strategy.update(token, data);
	}

	@Override
	public void refreshExp(String token) {
		strategy.refreshExp(token);
	}

	@Override
	public void remove(String token) {
		strategy.remove(token);
	}

	@Override
	public Object getData(String token) {
		return strategy.getData(token);
	}

	@Override
	public TokenStatus check(String token) {
		return strategy.check(token);
	}

}
