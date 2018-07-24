package lingshi.gateway.factory;

import lingshi.gateway.token.service.TokenPoolBase;
import lingshi.gateway.token.service.impl.TokenPoolMap;

/**
 * Token池工厂
 *
 */
public class TokenPoolFactory {

	private static TokenPoolBase tokenPool;

	public static synchronized TokenPoolBase getTokenPool() {
		if (TokenPoolFactory.tokenPool == null) {
			tokenPool = TokenPoolMap.get();
		}
		return tokenPool;
	}

	public static void setTokenPool(TokenPoolBase tokenPool) {
		TokenPoolFactory.tokenPool = tokenPool;
	}
}
