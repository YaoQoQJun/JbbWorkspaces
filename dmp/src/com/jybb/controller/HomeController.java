package com.jybb.controller;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jybb.mapper.AdminMapper;
import com.jybb.pojo.Admin;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	private AdminMapper adminMapper;
	
	private Logger logger=LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping("toHome.do")
	public ModelAndView toHome(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response){
		
		List<Map<String,String>> privileges=PrivilegeUtil.getPrivileges(session,adminMapper);
		session.setAttribute("privileges", privileges);
		return new ModelAndView("home/home").addObject("privileges", privileges);
	}
	
	@RequestMapping("/updatePassword.do")
	@ResponseBody
	public String updatePassword(HttpSession session,String password) throws Exception{
		try{
			Admin admin=(Admin)session.getAttribute("admin");
			admin.setPassword(password);
			adminMapper.updateAdmin(admin);
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改密码失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改密码失败(异常)--"+e.getMessage());
			throw e;
		}	
	}
	
}
