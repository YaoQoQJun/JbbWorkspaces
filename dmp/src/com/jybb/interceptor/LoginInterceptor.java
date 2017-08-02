package com.jybb.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jybb.pojo.Admin;


public class LoginInterceptor implements HandlerInterceptor{

	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		Admin admin=(Admin) request.getSession().getAttribute("admin");
		if(admin==null){
			request.setAttribute("error", "请先登录在访问后台系统");
			request.getRequestDispatcher("/WEB-INF/jsp/login/login.jsp").forward(request, response);
			return false;
		}else{
			return true;
		}
	}

}
