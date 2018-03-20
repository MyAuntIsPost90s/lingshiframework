package lingshi.model;

public class LingShiConfig {
	private static String appKey;
	private static String domain;
	private static boolean useSSO;

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
	
	public boolean getUseSSO(){
		return useSSO;
	}
	
	public void setUseSSO(boolean useSSO){
		LingShiConfig.useSSO = useSSO;
	}
}
