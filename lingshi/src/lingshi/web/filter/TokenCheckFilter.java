package lingshi.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

import lingshi.valid.StringValid;
import lingshi.web.model.LingShiTokenAndUserPool;
import lingshi.web.model.LingShiTokenCode;
import lingshi.web.model.MsgCode;
import lingshi.web.model.ResponseData;

public class TokenCheckFilter implements javax.servlet.Filter {
	private List<String> allowpath;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hRequest = (HttpServletRequest) request;
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
		if (StringValid.isNullOrEmpty(appKey)) {
			responseData.fail("Token错误，非法请求", null, MsgCode.TOKEN_FAIL);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(JSON.toJSONString(responseData));
			response.getWriter().close();
			return;
		}

		LingShiTokenAndUserPool pool = LingShiTokenAndUserPool.getLingShiTokenAndUserPool();
		int code = pool.checkToken(appKey, token);
		if (code == LingShiTokenCode.FAIL) {
			responseData.fail("Token错误，非法请求", null, MsgCode.TOKEN_FAIL);
			response.setContentType(response.getContentType().replace("text/html", "application/json"));
			response.getWriter().write(JSON.toJSONString(responseData));
			response.getWriter().close();
			return;
		}
		if (code == LingShiTokenCode.EXP) {
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
	}
}
