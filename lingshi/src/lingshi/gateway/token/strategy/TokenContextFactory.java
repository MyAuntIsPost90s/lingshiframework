package lingshi.gateway.token.strategy;

public class TokenContextFactory {

	private static TokenContext tokenContext;

	public synchronized static TokenContext getTokenContext() {
		if (tokenContext == null) {
			initUserTokenContext();
		}
		return tokenContext;
	}

	private static void initUserTokenContext() {
		tokenContext = new TokenContext(UserTokenStrategy.get());
	}

}
