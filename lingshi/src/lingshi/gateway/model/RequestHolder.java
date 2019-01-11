package lingshi.gateway.model;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lingshi.gateway.GatewayConstant;
import lingshi.gateway.token.model.TokenBase;
import lingshi.gateway.token.strategy.TokenContext;
import lingshi.gateway.token.strategy.TokenContextFactory;
import lingshi.model.LingShiConfig;
import lingshi.valid.StringValid;

public class RequestHolder {
	private HttpServletResponse response;
	private HttpServletRequest request;
	private HttpSession session;
	private String token;
	private RequestFile requestFile;
	private LingShiConfig config;
	private TokenContext tokenContext;

	private RequestHolder(HttpServletRequest request, HttpServletResponse response) {
		this.response = response;
		this.request = request;
		this.session = request.getSession();
		this.config = LingShiConfig.getInstance();
		this.token = GatewayConstant.getCurrAccessToken(request);
		this.tokenContext = TokenContextFactory.getTokenContext();
	}

	public static RequestHolder get(HttpServletRequest request, HttpServletResponse response) {
		RequestHolder requestHolder = new RequestHolder(request, response);
		return requestHolder;
	}

	public HttpSession getSession() {
		return this.session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public RequestFile getRequestFile() {
		if (requestFile == null) {
			try {
				requestFile = new RequestFile(this.request);
			} catch (Exception e) {
				requestFile = null;
				e.printStackTrace();
			}
		}
		return requestFile;
	}

	/**
	 * 设置用户
	 * 
	 * @throws Exception
	 */
	public String addClientUser(Object user) throws Exception {
		if (config.getUseSSO() == true) { // 判断是否启用了单用户登陆
			TokenBase tokenBase = tokenContext.create(user);
			token = tokenBase.getToken();
		} else {
			tokenContext.update(token, user);
		}
		refreshCookie();
		return token;
	}

	/**
	 * 修改token中的User
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void updateClientUser(Object user) throws Exception {
		tokenContext.update(token, user);
		refreshCookie();
	}

	/**
	 * 刷新cookie
	 */
	private void refreshCookie() {
		Cookie cookie = new Cookie(config.getTokenName(), token);
		cookie.setMaxAge(60 * 60 * 24 * 15);
		cookie.setPath("/");
		if (!StringValid.isNullOrEmpty(config.getDomain())) {
			cookie.setDomain(config.getDomain());
		}
		response.addCookie(cookie);
		response.setHeader(GatewayConstant.ACCESSTOKEN, token);
	}

	/**
	 * 移除登陆用户
	 */
	public void removeClientUser() {
		tokenContext.remove(token);
	}

	/**
	 * 获取用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object getClientUser() {
		return tokenContext.getData(token);
	}

	public void fail(String msg) {
		fail(msg, null, null);
	}

	public void fail(Object data) {
		fail(null, data, null);
	}

	public void fail() {
		fail(null, null, null);
	}

	public void fail(String msg, Object data) {
		fail(msg, data, null);
	}

	public void fail(String msg, Object data, String msgcode) {
		ResponseData responseData = new ResponseData();
		responseData.fail(msg, data, msgcode);
		try {
			String json = JSON.toJSONString(responseData, SerializerFeature.DisableCircularReferenceDetect);
			System.out.println("return json : " + json);
			if (StringValid.isNullOrEmpty(response.getContentType())) {
				response.setContentType("application/json");
			} else {
				response.setContentType(response.getContentType().replace("text/html", "application/json"));
			}
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void success(String msg) {
		success(msg, null, null);
	}

	public void success() {
		success(null, null, null);
	}

	public void success(Object obj) {
		success(null, obj, null);
	}

	public void success(String msg, Object obj) {
		success(msg, obj, null);
	}

	public void success(String msg, Object obj, String msgcode) {
		ResponseData responseData = new ResponseData();
		responseData.success(msg, obj, msgcode);
		try {
			String json = JSON.toJSONString(responseData, SerializerFeature.DisableCircularReferenceDetect);
			System.out.println("return json : " + json);
			if (StringValid.isNullOrEmpty(response.getContentType())) {
				response.setContentType("application/json");
			} else {
				response.setContentType(response.getContentType().replace("text/html", "application/json"));
			}
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void entity(Object object) {
		try {
			String json = JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
			System.out.println("return json : " + json);
			if (StringValid.isNullOrEmpty(response.getContentType())) {
				response.setContentType("application/json");
			} else {
				response.setContentType(response.getContentType().replace("text/html", "application/json"));
			}
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取当前Web根目录物理路径
	 * 
	 * @return
	 */
	public String getRealPathPath(String path) {
		if (path != null && path.length() > 0 && path.substring(0, 1).equals("/")) {
			path = path.substring(1);
		}
		ServletContext servletContext = this.request.getServletContext();
		return servletContext.getRealPath("/") + path;
	}
}
