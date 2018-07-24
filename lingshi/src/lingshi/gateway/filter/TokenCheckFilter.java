package lingshi.gateway.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import lingshi.convert.Convert;
import lingshi.gateway.GatewayConstant;
import lingshi.gateway.model.MsgCode;
import lingshi.gateway.model.ResponseData;
import lingshi.gateway.token.LingShiTokenEnum.TokenStatus;
import lingshi.gateway.token.strategy.TokenContext;
import lingshi.gateway.token.strategy.TokenContextFactory;
import lingshi.model.LingShiConfig;
import lingshi.valid.ObjectValid;
import lingshi.valid.StringValid;

public class TokenCheckFilter implements javax.servlet.Filter {
	private List<String> allowPath; // 允许通过的路径
	private List<String> checkPath; // 需要校验的路径
	private Boolean isCross; // 是否允许跨域

	private final String ALLOW_PATH = "allowPath";
	private final String CHECK_PATH = "checkPath";
	private final String IS_CROSS = "isCross";

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hRequest = (HttpServletRequest) request;
		HttpServletResponse hResponse = (HttpServletResponse) response;
		if (isCross) {
			setCrossHeader(hResponse);
			boolean isOption = hRequest.getMethod().toUpperCase().equals("OPTIONS");
			if (isOption) {
				chain.doFilter(request, response);
				return;
			}
		}
		String url = getUri(hRequest);
		if (isAllowPath(url)) {
			chain.doFilter(request, response);
			return;
		}
		if (isNeedCheck(url)) {
			if (!checkPass(hRequest, hResponse)) {
				return;
			} else {
				refreshTokenExp(hRequest);
			}
		}
		chain.doFilter(request, response);
	}

	private void refreshTokenExp(HttpServletRequest request) {
		String accessToken = GatewayConstant.getCurrAccessToken(request);
		TokenContext tokenContext = TokenContextFactory.getTokenContext();
		tokenContext.refreshExp(accessToken);
	}

	private boolean checkPass(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String appKey = GatewayConstant.getCurrAppKey(request);
		String accessToken = GatewayConstant.getCurrAccessToken(request);
		ResponseData responseData = new ResponseData();
		if (!LingShiConfig.getInstance().getAppKey().equals(appKey)) {
			responseData.fail("appKey错误，非法请求", null, MsgCode.TOKEN_FAIL);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(JSON.toJSONString(responseData));
			response.getWriter().close();
			return false;
		}
		TokenContext tokenContext = TokenContextFactory.getTokenContext();
		TokenStatus tokenStatus = tokenContext.check(accessToken);
		if (tokenStatus == TokenStatus.FAIL) {
			responseData.fail("Token错误，非法请求", null, MsgCode.TOKEN_FAIL);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(JSON.toJSONString(responseData));
			response.getWriter().close();
			return false;
		}
		if (tokenStatus == TokenStatus.EXP) {
			responseData.fail("Token已过期", null, MsgCode.TOKEN_FAIL);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(JSON.toJSONString(responseData));
			response.getWriter().close();
			return false;
		}
		return true;
	}

	private void setCrossHeader(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", response.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "0");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,AppKey,AccessToken");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("XDomainRequestAllowed", "1");
	}

	private boolean isNeedCheck(String url) {
		if (ObjectValid.isEmpty(this.checkPath)) {
			return true;
		}
		for (String path : checkPath) {
			if (url.equals(path)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAllowPath(String url) {
		if (allowPath != null) {
			for (String item : allowPath) {
				if (url.equals(item)) {
					return true;
				}
			}
		}
		return false;
	}

	private String getUri(HttpServletRequest request) {
		String webName = request.getServletContext().getContextPath();
		String url = request.getRequestURI();
		if (StringValid.isNotNullOrEmpty(webName)) {
			url = url.replaceAll(webName, "");
		}
		return url;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		initCheckPath(filterConfig);
		initAllowPath(filterConfig);
		initIsCross(filterConfig);

	}

	private void initCheckPath(FilterConfig filterConfig) {
		String checkPath = filterConfig.getInitParameter(CHECK_PATH);
		if (StringValid.isNotNullOrWhiteSpace(checkPath)) {
			String[] strs = checkPath.split(",");
			this.checkPath = Arrays.asList(strs);
			System.out.println("Load checkPath:" + checkPath.toString());
		} else {
			this.checkPath = new ArrayList<String>();
		}
	}

	private void initAllowPath(FilterConfig filterConfig) {
		String allowPath = filterConfig.getInitParameter(ALLOW_PATH);
		if (StringValid.isNotNullOrWhiteSpace(allowPath)) {
			String[] strs = allowPath.split(",");
			this.allowPath = Arrays.asList(strs);
			System.out.println("Load allowPath:" + allowPath.toString());
		} else {
			this.allowPath = new ArrayList<String>();
		}
	}

	private void initIsCross(FilterConfig filterConfig) {
		String crossStr = filterConfig.getInitParameter(IS_CROSS);
		if (StringValid.isNotNullOrWhiteSpace(crossStr)) {
			this.isCross = Convert.toBoolean(crossStr);
			System.out.println("Load isCross:" + isCross);
		} else {
			this.isCross = false;
			System.out.println("Load isCross: false");
		}
	}
}
