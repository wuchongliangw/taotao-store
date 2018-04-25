package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserLoginService;

public class LoginInterceptor implements HandlerInterceptor{

	@Value("${TT_TOKEN}")
	private String TT_TOKEN;
	
	@Value("${SSO_LOGIN_URL}")
	private String SSO_LOGIN_URL;
	
	@Autowired
	private UserLoginService userLoginService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		//根据cookie查询用户信息
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		//tooken为空，用户未登录，跳转到登录
		if (StringUtils.isBlank(token)) {
			//取当前请求的URL
			String url = request.getRequestURL().toString();
			
			response.sendRedirect(SSO_LOGIN_URL+"?redirect="+url);
			//拦截
			return false;
		}
		//tooken不为空，根据tooken查询用户信息
		TaotaoResult result = userLoginService.getUserByToken(token);
		TbUser user = new TbUser();
		if (result != null && result.getStatus() == 200) {
			user = (TbUser) result.getData();
			//将用户信息传递到OrderController
			request.setAttribute("user", user);
		}else {
			//没有查询到用户信息，跳转到登录页面
			//取当前请求的URL
			String url = request.getRequestURL().toString();
			response.sendRedirect(SSO_LOGIN_URL+"?redirect="+url);
			//拦截
			return false;
		}
		//查询到用户信息，放行
		return true;
	}

}
