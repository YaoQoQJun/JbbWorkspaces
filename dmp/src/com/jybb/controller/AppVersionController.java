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
import com.jybb.mapper.AppVersionMapper; 
import com.jybb.pojo.AppVersion;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/appVersion")
public class AppVersionController {
	
	private Logger logger=LoggerFactory.getLogger(AppVersionController.class);
	private static final Integer PAGE_SIZE=7;
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private AppVersionMapper appVersionMapper;
	
	@RequestMapping("toAppVersion.do")
	public ModelAndView toLogin(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","APP版本");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		
		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		
		List<AppVersion> appVersions=appVersionMapper.findAllByPage(start,PAGE_SIZE);
		Integer total=appVersionMapper.count();
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		return new ModelAndView("appVersion/appVersion")
		.addObject("privileges", privileges)
		.addObject("page", page)
		.addObject("total", total)
		.addObject("appVersions", appVersions)
		.addObject("totalPage", totalPage);
	}
	
	@RequestMapping("/addAppVersion.do")
	@ResponseBody
	public String addAppVersion(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String app_version_name) throws Exception{

		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","APP版本");
			
			AppVersion appVersion=new AppVersion();
			appVersion.setApp_version_name(app_version_name);
			
			appVersionMapper.addAppVersion(appVersion);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("保存APP版本失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存APP版本失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/updateAppVersion.do")
	@ResponseBody
	public String updateAppVersion(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			String app_version_name) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"修改","APP版本");
			
			AppVersion appVersion=new AppVersion();
			appVersion.setId(id);
			appVersion.setApp_version_name(app_version_name);
			
			appVersionMapper.updateAppVersion(appVersion);
			
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("修改APP版本失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改APP版本失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/deleteAppVersion.do")
	@ResponseBody
	public String deleteAppVersion(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,Integer id) throws Exception{
		
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"删除","APP版本");
			
			appVersionMapper.deleteAppVersion(id);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("删除APP版本失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除APP版本失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/checkAppVersionName.do")
	@ResponseBody
	public String checkAppVersionName(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String app_version_name,
			Integer id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"访问","APP版本");
			AppVersion appVersion=appVersionMapper.findAppVersionByAppVersionName(app_version_name,id);
			if(appVersion==null){
				return "ok";
			}else{
				return "no";
			}
			
		} catch (DataAccessException e) {
			logger.warn("检查APP版本名称是否存在(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("检查APP版本名称是否存在(异常)--"+e.getMessage());
			throw e;
		}	
	}
	
	
}
