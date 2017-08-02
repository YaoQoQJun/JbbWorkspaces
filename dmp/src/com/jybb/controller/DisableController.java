package com.jybb.controller;


import java.sql.Timestamp;
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
import com.jybb.mapper.DisableMapper;
import com.jybb.mapper.ExtensionMapper;
import com.jybb.pojo.Disable;
import com.jybb.pojo.Website;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/disable")
public class DisableController {
	
	private Logger logger=LoggerFactory.getLogger(DisableController.class);
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private ExtensionMapper extensionMapper;
	
	@Autowired
	private DisableMapper disableMapper;
	
	private static final Integer PAGE_SIZE=7;
	
	@RequestMapping("/toDisable.do")
	public ModelAndView toDisable(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","禁用链接");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		if(page==null||page==0){
			page=1;
		}
		Integer start=(page-1)*PAGE_SIZE;
		
		List<Disable> disables=disableMapper.findDisablesByPage(start,PAGE_SIZE);
		Integer total=disableMapper.countDisables();
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		return new ModelAndView("disable/disable")
		.addObject("page", page)
		.addObject("privileges", privileges)
		.addObject("total", total)
		.addObject("totalPage", totalPage)
		.addObject("disables", disables);
	}
	
	
	@RequestMapping("/getWebsite.do")
	@ResponseBody
	public Website getWebsite(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer website_id){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","推广链接");
		
		Website website=extensionMapper.getWebsite(website_id);
		
		return website;
	}
	
	@RequestMapping("/addDisable.do")
	@ResponseBody
	public String addExtension(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer website_id,
			String channel_name,
			String channel_id,
			String disable_link) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","推广链接");
			
			Disable disable=new Disable();
			disable.setWebsite_id(website_id);
			disable.setChannel_id(channel_id);
			disable.setChannel_name(channel_name);
			disable.setDisable_link(disable_link);
			
			
			disableMapper.addDisable(disable);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("保存网站失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存网站失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/updateDisableState.do")
	@ResponseBody
	public String updateDisableState(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			Integer state) throws Exception{
		try{
			
			PrivilegeUtil.checkPriviege(session,request,response,"修改","禁用链接");
			
			if(state==1){
				Disable disable=disableMapper.getDisableById(id);
				Website website=extensionMapper.getWebsite(disable.getWebsite_id());
				if(website.getState()==0){
					return "no_ok";
				}
			}
			
			Disable disable=new Disable();
			disable.setId(id);
			disable.setState(state);
			if(state==0){
				disable.setDisable_time(new Timestamp(System.currentTimeMillis()));
			}else{
				disable.setEnable_time(new Timestamp(System.currentTimeMillis()));
			}
			
			disableMapper.updateWebsiteState(disable);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改推广链接状态失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改推广链接状态失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/updateDisable.do")
	@ResponseBody
	public String updateDisable(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			Integer website_id,
			String channel_name,
			String channel_id,
			String disable_link) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"修改","禁用链接");
			
			Disable disable=new Disable();
			disable.setId(id);
			disable.setWebsite_id(website_id);
			disable.setChannel_id(channel_id);
			disable.setChannel_name(channel_name);
			disable.setDisable_link(disable_link);
			
			disableMapper.updateDisable(disable);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改禁用链接失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改禁用链接失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/deleteDisable.do")
	@ResponseBody
	public String deleteDisable(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id) throws Exception{
		try{
			
			PrivilegeUtil.checkPriviege(session,request,response,"删除","禁用链接");
			
			disableMapper.deleteDisable(id);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("删除禁用链接失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除禁用链接接失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
}
