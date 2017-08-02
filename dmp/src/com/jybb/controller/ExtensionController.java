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
import com.jybb.mapper.ExtensionMapper;
import com.jybb.pojo.Extension;
import com.jybb.pojo.SonLink;
import com.jybb.pojo.Website;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/extension")
public class ExtensionController {
	
	private Logger logger=LoggerFactory.getLogger(ExtensionController.class);
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private ExtensionMapper extensionMapper;
	
	
	private static final Integer PAGE_SIZE=7;
	
	@RequestMapping("/toExtension.do")
	public ModelAndView toWebsite(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","推广链接");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		if(page==null||page==0){
			page=1;
		}
		Integer start=(page-1)*PAGE_SIZE;
		
		List<Extension> extensions=extensionMapper.findExtensionsByPage(start,PAGE_SIZE);
		Integer total=extensionMapper.countExtensions();
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		return new ModelAndView("extension/extension")
		.addObject("page", page)
		.addObject("privileges", privileges)
		.addObject("total", total)
		.addObject("totalPage", totalPage)
		.addObject("extensions", extensions);
	}
	
	@RequestMapping("/getWebsites.do")
	@ResponseBody
	public List<Website> getWebsites(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","推广链接");
		
		List<Website> websites=extensionMapper.getWebsites();
		
		return websites;
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
	
	@RequestMapping("/addExtension.do")
	@ResponseBody
	public String addExtension(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer website_id,
			String channel_name,
			String channel_id,
			String extension_link) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","推广链接");
			
			Extension extension=new Extension();
			extension.setWebsite_id(website_id);
			extension.setChannel_id(channel_id);
			extension.setChannel_name(channel_name);
			extension.setExtension_link(extension_link);
			
			
			extensionMapper.addExtension(extension);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("保存网站失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存网站失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/updateExtensionState.do")
	@ResponseBody
	public String updateExtensionState(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			Integer state) throws Exception{
		try{
			
			PrivilegeUtil.checkPriviege(session,request,response,"修改","推广链接");
			
			if(state==1){
				Extension extension=extensionMapper.getExtensionById(id);
				Website website=extensionMapper.getWebsite(extension.getWebsite_id());
				if(website.getState()==0){
					return "no_ok";
				}
				
			}
			
			Extension extension=new Extension();
			extension.setId(id);
			extension.setState(state);
			if(state==0){
				extension.setDisable_time(new Timestamp(System.currentTimeMillis()));
			}else{
				extension.setEnable_time(new Timestamp(System.currentTimeMillis()));
			}
			
			extensionMapper.updateWebsiteState(extension);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改推广链接状态失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改推广链接状态失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/updateExtension.do")
	@ResponseBody
	public String updateExtension(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			Integer website_id,
			String channel_name,
			String channel_id,
			String extension_link) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"修改","推广链接");
			Extension extension=new Extension();
			extension.setId(id);
			extension.setWebsite_id(website_id);
			extension.setChannel_id(channel_id);
			extension.setChannel_name(channel_name);
			extension.setExtension_link(extension_link);
			
			extensionMapper.updateExtension(extension);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改推广失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改推广失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/addSonLink.do")
	@ResponseBody
	public String addSonLink(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			String link) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","推广链接");
			
			SonLink sonLink=new SonLink();
			sonLink.setExtension_id(id);
			sonLink.setLink(link);
			
			extensionMapper.updateExtensionSonCount1(id);
			extensionMapper.addSonLink(sonLink);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("保存网站失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存网站失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/getLinks.do")
	@ResponseBody
	public List<SonLink> getLinks(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer extension_id){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","推广链接");
		
		List<SonLink> sonLinks=extensionMapper.getLinks(extension_id);
		
		return sonLinks;
	}
	
	@RequestMapping("/deleteSonLink.do")
	@ResponseBody
	public String deleteSonLink(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			Integer extension_id) throws Exception{
		try{
			
			PrivilegeUtil.checkPriviege(session,request,response,"删除","推广链接");
			
			extensionMapper.updateExtensionSonCount2(extension_id);
			extensionMapper.deleteSonLink(id);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("删除子链接失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除子链接失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/deleteExtension.do")
	@ResponseBody
	public String deleteExtension(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id) throws Exception{
		try{
			
			PrivilegeUtil.checkPriviege(session,request,response,"删除","推广链接");
			
			extensionMapper.deleteSonLinkByExtensionId(id);
			extensionMapper.deleteExtension(id);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("删除子链接失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除子链接失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
}
