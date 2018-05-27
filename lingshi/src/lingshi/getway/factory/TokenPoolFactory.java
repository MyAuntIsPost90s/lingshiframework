package lingshi.getway.factory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lingshi.getway.token.service.TokenPoolBase;
import lingshi.getway.token.service.impl.TokenPoolMap;

/**
 * Token池工厂
 *
 */
public class TokenPoolFactory {

	private static TokenPoolBase tokenPool;
	private static Lock lock = new ReentrantLock();

	public static TokenPoolBase getTokenPool() {
		if (TokenPoolFactory.tokenPool == null) {
			lock.lock();
			try {
				if (TokenPoolFactory.tokenPool == null) {
					tokenPool = TokenPoolMap.get();
				}
			} finally {
				lock.unlock();
			}
		}
		return tokenPool;
	}

	public static void setTokenPool(TokenPoolBase tokenPool) {
		TokenPoolFactory.tokenPool = tokenPool;
	}
}
