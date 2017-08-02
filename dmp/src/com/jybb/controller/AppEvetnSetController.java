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
import com.jybb.mapper.AppEventSetMapper;
import com.jybb.pojo.AppChannel;
import com.jybb.pojo.AppEventSet;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/appEventSet")
public class AppEvetnSetController {
	
	private Logger logger=LoggerFactory.getLogger(AppEvetnSetController.class);
	private static final Integer PAGE_SIZE=7;
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private AppEventSetMapper appEventSetMapper;
	
	@RequestMapping("toAppEventSet.do")
	public ModelAndView toLogin(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","事件设置");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		
		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		
		List<AppEventSet> appEventSets=appEventSetMapper.findAllByPage(start,PAGE_SIZE);
		Integer total=appEventSetMapper.count();
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		return new ModelAndView("appEventSet/appEventSet")
		.addObject("privileges", privileges)
		.addObject("page", page)
		.addObject("total", total)
		.addObject("appEventSets", appEventSets)
		.addObject("totalPage", totalPage);
	}
	
	
	@RequestMapping("/addAppEventSet.do")
	@ResponseBody
	public String addAppEventSet(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String app_event_name,
			String app_event_remark) throws Exception{

		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","事件设置");
			
			AppEventSet appEventSet=new AppEventSet();
			appEventSet.setApp_event_name(app_event_name);
			appEventSet.setApp_event_remark(app_event_remark);
			
			appEventSetMapper.addAppEventSet(appEventSet);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("保存事件設置失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存事件設置失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/updateAppEventSet.do")
	@ResponseBody
	public String updateAppEventSet(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			String app_event_name,
			String app_event_remark) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"修改","事件设置");
			
			AppEventSet appEventSet=new AppEventSet();
			appEventSet.setId(id);
			appEventSet.setApp_event_name(app_event_name);
			appEventSet.setApp_event_remark(app_event_remark);
			
			appEventSetMapper.updateAppEventSet(appEventSet);
			
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("修改APP渠道失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改APP渠道失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/deleteAppEventSet.do")
	@ResponseBody
	public String deleteAppEventSet(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,Integer id) throws Exception{
		
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"删除","事件设置");
			
			appEventSetMapper.deleteAppEventSet(id);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("删除APP事件失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除APP事件失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/checkAppEventSetName.do")
	@ResponseBody
	public String checkAppEventSetName(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String app_event_name,
			Integer id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"访问","事件设置");
			
			AppEventSet appEventSet=appEventSetMapper.findAppEventSetByAppEventName(app_event_name,id);
			
			if(appEventSet==null){
				return "ok";
			}else{
				return "no";
			}
			
		} catch (DataAccessException e) {
			logger.warn("检查事件名称是否存在(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("检查事件名称是否存在(异常)--"+e.getMessage());
			throw e;
		}	
	}
	
	
	
}
