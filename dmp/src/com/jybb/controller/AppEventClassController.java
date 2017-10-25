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
import com.jybb.mapper.AppChannelMapper;
import com.jybb.mapper.AppEventClassMapper;
import com.jybb.pojo.AppChannel;
import com.jybb.pojo.AppEventClass;
import com.jybb.pojo.Website;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/appEventClass")
public class AppEventClassController {
	
	private Logger logger=LoggerFactory.getLogger(AppEventClassController.class);
	private static final Integer PAGE_SIZE=7;
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private AppEventClassMapper appEventClassMapper;
	
	@RequestMapping("toAppEventClass.do")
	public ModelAndView addAppEventClass(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","事件分类");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		
		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		
		List<AppEventClass> appEventClasss=appEventClassMapper.findAllByPage(start,PAGE_SIZE);
		Integer total=appEventClassMapper.count();
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		return new ModelAndView("appEventClass/appEventClass")
		.addObject("privileges", privileges)
		.addObject("page", page)
		.addObject("total", total)
		.addObject("appEventClasss", appEventClasss)
		.addObject("totalPage", totalPage);
	}
	
	@RequestMapping("/addAppEventClass.do")
	@ResponseBody
	public String addAppEventClass(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String app_event_class_name) throws Exception{

		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","事件分类");
			
			AppEventClass appEventClass=new AppEventClass();
			appEventClass.setApp_event_class_name(app_event_class_name);
			
			appEventClassMapper.addAppEventClass(appEventClass);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("保存APP事件分类失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存APP事件分类失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/updateAppEventClass.do")
	@ResponseBody
	public String updateAppEventClass(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			String app_event_class_name) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"修改","事件分类");
			
			AppEventClass appEventClass=new AppEventClass();
			appEventClass.setId(id);
			appEventClass.setApp_event_class_name(app_event_class_name);
			
			appEventClassMapper.updateAppEventClass(appEventClass);
			
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("修改APP事件分类失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改APP事件分类失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/deleteAppEventClass.do")
	@ResponseBody
	public String deleteAppEventClass(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,Integer id) throws Exception{
		
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"删除","事件分类");
			
			appEventClassMapper.deleteAppEventClass(id);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("删除APP事件分类失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除APP事件分类失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/checkAppEventClassName.do")
	@ResponseBody
	public String checkAppEventClassName(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String app_event_class_name,
			Integer id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"访问","APP渠道");
			AppEventClass appEventClass=appEventClassMapper.findAppEventClassByAppEventClassName(app_event_class_name,id);
			if(appEventClass==null){
				return "ok";
			}else{
				return "no";
			}
			
		} catch (DataAccessException e) {
			logger.warn("检查APP事件分类名称是否存在(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("检查APP事件分类名称是否存在(异常)--"+e.getMessage());
			throw e;
		}	
	}
	
	@RequestMapping("/getAppEventClasss.do")
	@ResponseBody
	public List<AppEventClass> getAppEventClasss(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","APP渠道");
		
		List<AppEventClass> appEventClasss=appEventClassMapper.findAll();
		
		return appEventClasss;
	}
	
	
}
