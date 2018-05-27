package lingshi.getway.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import lingshi.convert.Convert;
import lingshi.getway.model.MsgCode;
import lingshi.getway.model.ResponseData;
import lingshi.getway.token.LingShiTokenEnum.TokenStatus;
import lingshi.getway.token.service.TokenMgrService;
import lingshi.getway.token.service.impl.TokenMgrServiceMd5Impl;
import lingshi.model.LingShiConfig;
import lingshi.valid.StringValid;

public class TokenCheckFilter implements javax.servlet.Filter {
	private List<String> allowpath;
	private Boolean iscross;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hRequest = (HttpServletRequest) request;
		HttpServletResponse hResponse = (HttpServletResponse) response;

		// 当开启跨域时
		if (iscross == true) {
			hResponse.setHeader("Access-Control-Allow-Origin", hRequest.getHeader("Origin"));

			hResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			hResponse.setHeader("Access-Control-Max-Age", "0");
			hResponse.setHeader("Access-Control-Allow-Headers",
					"Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,AppKey,AccessToken");
			hResponse.setHeader("Access-Control-Allow-Credentials", "true");
			hResponse.setHeader("XDomainRequestAllowed", "1");

			// 过滤试探请求
			if (hRequest.getMethod().toUpperCase().equals("OPTIONS")) {
				chain.doFilter(request, response);
				return;
			}
		}

		// 跳过校验
		if (allowpath != null) {
			for (String item : allowpath) {
				if (hRequest.getRequestURL().toString().contains(item)) {
					chain.doFilter(request, response);
					return;
				}
			}
		}

		String appKey = hRequest.getHeader("AppKey");
		String token = hRequest.getHeader("AccessToken");

		ResponseData responseData = new ResponseData();
		if (StringValid.isNullOrEmpty(appKey) || !appKey.equals(LingShiConfig.getInstance().getAppKey())) {
			responseData.fail("appKey错误，非法请求", null, MsgCode.TOKEN_FAIL);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(JSON.toJSONString(responseData));
			response.getWriter().close();
			return;
		}

		TokenMgrService tokenMgrService = new TokenMgrServiceMd5Impl();
		TokenStatus tokenStatus = tokenMgrService.tokenCheck(token);
		if (tokenStatus == TokenStatus.FAIL) {
			responseData.fail("Token错误，非法请求", null, MsgCode.TOKEN_FAIL);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(JSON.toJSONString(responseData));
			response.getWriter().close();
			return;
		}
		if (tokenStatus == TokenStatus.EXP) {
			responseData.fail("Token已过期", null, MsgCode.TOKEN_FAIL);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(JSON.toJSONString(responseData));
			response.getWriter().close();
			return;
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String[] strs = filterConfig.getInitParameter("allowpath").split(",");
		this.allowpath = Arrays.asList(strs);
		Logger.getRootLogger().info("Load allowpath:" + allowpath.toString());

		// 是否开启跨域
		String crossStr = filterConfig.getInitParameter("iscross");
		iscross = Convert.toBoolean(crossStr);
		Logger.getRootLogger().info("Load iscross:" + iscross);
	}
}
