package lingshi.model;

import org.springframework.web.context.ContextLoader;

import lingshi.gateway.GatewayConstant;
import lingshi.valid.ObjectValid;

public class LingShiConfig {
	private static String appKey;
	private static String domain;
	private static boolean useSSO;
	private static Integer tokenExp;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		LingShiConfig.appKey = appKey;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		LingShiConfig.domain = domain;
	}

	public boolean getUseSSO() {
		return useSSO;
	}

	public void setUseSSO(boolean useSSO) {
		LingShiConfig.useSSO = useSSO;
	}

	public Integer getTokenExp() {
		if (ObjectValid.isNull(tokenExp)) {
			tokenExp = GatewayConstant.TOKEN_DEFAULT_EXP;
		}
		return tokenExp;
	}

	public void setTokenExp(Integer tokenExp) {
		LingShiConfig.tokenExp = tokenExp;
	}

	public static LingShiConfig getInstance() {
		return (LingShiConfig) ContextLoader.getCurrentWebApplicationContext().getBean(LingShiConfig.class);
	}
}
