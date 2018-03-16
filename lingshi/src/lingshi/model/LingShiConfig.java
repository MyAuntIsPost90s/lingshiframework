package lingshi.model;

public class LingShiConfig {
	private static String appKey;
	private static String domain;

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
}
