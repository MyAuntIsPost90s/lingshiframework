package lingshi.gateway;

import javax.servlet.http.HttpServletRequest;

import lingshi.valid.StringValid;

public class GatewayConstant {

	public final static String APP_KEY = "AppKey";

	public final static String ACCESSTOKEN = "AccessToken";

	public final static String COOKIE_TOKEN_KEY = "LingShi_Token";

	public final static int TOKEN_DEFAULT_EXP = 60 * 1000 * 60 * 2;

	/**
	 * 获取当前对话的appkey
	 * 
	 * @param request
	 * @return
	 */
	public static String getCurrAppKey(HttpServletRequest request) {
		String appKey = request.getHeader(APP_KEY);
		if (StringValid.isNullOrEmpty(appKey)) {
			appKey = request.getParameter(APP_KEY);
		}
		return appKey;
	}

	/**
	 * 获取当前会话token
	 * 
	 * @param request
	 * @return
	 */
	public static String getCurrAccessToken(HttpServletRequest request) {
		String token = request.getHeader(ACCESSTOKEN);
		if (StringValid.isNullOrEmpty(token)) {
			token = request.getParameter(ACCESSTOKEN);
		}
		return token;
	}

}
