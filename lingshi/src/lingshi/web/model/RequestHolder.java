package lingshi.web.model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSON;

import lingshi.model.LingShiConfig;
import lingshi.valid.StringValid;

public class RequestHolder {
	private HttpServletResponse response;
	private HttpServletRequest request;
	private HttpSession session;
	private String token;
	private RequestFile requestFile;
	private LingShiConfig config;
	private static LingShiTokenAndUserPool pool;

	private RequestHolder(HttpServletRequest request, HttpServletResponse response) {
		this.response = response;
		this.request = request;
		this.session = request.getSession();
		this.config = (LingShiConfig) ContextLoader.getCurrentWebApplicationContext().getBean(LingShiConfig.class);
		pool = LingShiTokenAndUserPool.getLingShiTokenAndUserPool();

		this.token = request.getHeader("AccessToken");
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
				Logger.getRootLogger().info(e);
			}
		}
		return requestFile;
	}

	/**
	 * 设置用户
	 * 
	 * @throws Exception
	 */
	public void addClientUser(Object user) throws Exception {
		LingShiToken token = null;
		if (config.getUseSSO() == true) { // 判断是否启用了单点登陆
			token = pool.addTokenUser(user, config.getAppKey());
		} else {
			token = pool.updateTokenUser(user);
		}
		if(token==null){	//当不存在时需要执行添加
			token = pool.addTokenUser(user, config.getAppKey());
		}

		Cookie cookie = new Cookie("LingShi_Token", token.getToken());
		cookie.setMaxAge(60 * 60 * 24 * 15);
		cookie.setPath("/");
		if (!StringValid.isNullOrEmpty(config.getDomain())) {
			cookie.setDomain(config.getDomain());
		}
		response.addCookie(cookie);
		response.setHeader("AccessToken", token.getToken());
	}
	
	/**
	 * 修改token中的User
	 * 
	 * @param user
	 * @throws Exception
	 */
	public void updateClientUser(Object user)throws Exception{
		LingShiToken token = pool.updateTokenUser(user);
		if(token==null){	//当不存在时需要执行添加
			token = pool.addTokenUser(user, config.getAppKey());
		}

		Cookie cookie = new Cookie("LingShi_Token", token.getToken());
		cookie.setMaxAge(60 * 60 * 24 * 15);
		cookie.setPath("/");
		if (!StringValid.isNullOrEmpty(config.getDomain())) {
			cookie.setDomain(config.getDomain());
		}
		response.addCookie(cookie);
		response.setHeader("AccessToken", token.getToken());
	}

	/**
	 * 移除登陆用户
	 */
	public void removeClientUser() {
		pool.removeTokenUser(token);
	}

	/**
	 * 获取用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object getClientUser() {
		LingShiToken result = pool.getToken(token);
		return result == null ? null : result.getData();
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
			String json = JSON.toJSONString(responseData);
			Logger.getRootLogger().info("return json : " + json);

			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (Exception e) {
			Logger.getRootLogger().error(e);
		}
	}

	public void err(String msg, Exception e) {
		err(msg, null, null, e);
	}

	public void err(Object data, Exception e) {
		err(null, data, null, e);
	}

	public void err(Exception e) {
		err(null, null, null, e);
	}

	public void err(String msg, Object data, Exception e) {
		err(msg, data, null, e);
	}

	public void err(String msg, Object data, String msgcode, Exception err) {
		Logger.getRootLogger().error(err);
		ResponseData responseData = new ResponseData();
		responseData.fail(msg, data, msgcode);

		try {
			String json = JSON.toJSONString(responseData);
			Logger.getRootLogger().info("return json : " + json);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (Exception e) {
			Logger.getRootLogger().error(e);
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
			String json = JSON.toJSONString(responseData);
			Logger.getRootLogger().info("return json : " + json);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (Exception e) {
			Logger.getRootLogger().error(e);
		}
	}

	public void entity(Object object) {
		try {
			String json = JSON.toJSONString(object);
			Logger.getRootLogger().info("return json : " + json);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(json);
			response.getWriter().close();
		} catch (Exception e) {
			Logger.getRootLogger().error(e);
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
		return this.request.getServletContext().getRealPath("/") + path;
	}
}
