package com.jybb.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.jybb.mapper.AdminMapper;
import com.jybb.pojo.Admin;


@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	private AdminMapper adminMapper;
	
	@RequestMapping("toLogin.do")
	public ModelAndView toLogin(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response){
		
		return new ModelAndView("login/login");
	}
	
	//登录
	@RequestMapping(value="/login.do")
	public ModelAndView login(Admin admin,HttpSession session,HttpServletRequest request){
		Admin findAdmin=adminMapper.findAdmin(admin.getUsername(), admin.getPassword());
		if(findAdmin==null){
			return new ModelAndView("login/login").addObject("error", "用户名或密码错误！");
		}else{
			session.setAttribute("admin", findAdmin);
			return new ModelAndView(new RedirectView(request.getContextPath()+"/home/toHome.do"));
		}
	}
	@RequestMapping("/out.do")
	public ModelAndView toLogin(HttpSession session,HttpServletRequest request){
		session.removeAttribute("admin");
		return new ModelAndView(new RedirectView(request.getContextPath()+"/login/toLogin.do"));
	}
}
