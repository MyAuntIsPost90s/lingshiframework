package lingshi.model;

import lingshi.gateway.GatewayConstant;
import lingshi.utilities.SpringUtil;
import lingshi.valid.ObjectValid;

public class LingShiConfig {
	private static String appKey;
	private static String domain;
	private static boolean useSSO;
	private static Integer tokenExp;
	private static String tokenName;

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

	public String getTokenName() {
		if (ObjectValid.isNull(tokenName)) {
			tokenName = GatewayConstant.COOKIE_TOKEN_KEY;
		}
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		LingShiConfig.tokenName = tokenName;
	}

	public void setTokenExp(Integer tokenExp) {
		LingShiConfig.tokenExp = tokenExp;
	}

	public static LingShiConfig getInstance() {
		return SpringUtil.getBean(LingShiConfig.class);
	}

}
