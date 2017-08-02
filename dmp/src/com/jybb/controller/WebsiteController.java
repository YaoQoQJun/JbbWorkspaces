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
import com.jybb.mapper.WebsiteMapper;
import com.jybb.pojo.Website;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/website")
public class WebsiteController {
	
	private Logger logger=LoggerFactory.getLogger(WebsiteController.class);
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private WebsiteMapper websiteMapper;
	
	private static final Integer PAGE_SIZE=7;
	
	@RequestMapping("/toWebsite.do")
	public ModelAndView toWebsite(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","网站管理");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		
		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		
		List<Website> websites=websiteMapper.findWebsitesByPage(start,PAGE_SIZE);
		Integer total=websiteMapper.countWebsites();
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		
		return new ModelAndView("website/website")
		.addObject("privileges", privileges)
		.addObject("page", page)
		.addObject("total", total)
		.addObject("websites", websites)
		.addObject("totalPage", totalPage);
		
	}
	
	@RequestMapping("/addWebsite.do")
	@ResponseBody
	public String addWebsite(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String name,
			String domain_name,
			String code) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","网站管理");
			Website website=new Website();
			website.setName(name);
			website.setDomain_name(domain_name);
			website.setCode(code);
			
			websiteMapper.addWebsite(website);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("保存网站失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存网站失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/updateWebsite.do")
	@ResponseBody
	public String updateWebsite(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			String name,
			String domain_name,
			String code) throws Exception{
		try{
			
			PrivilegeUtil.checkPriviege(session,request,response,"修改","网站管理");
			Website website=new Website();
			website.setId(id);
			website.setName(name);
			website.setDomain_name(domain_name);
			website.setCode(code);
			
			websiteMapper.updateWebsite(website);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改网站失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改网站失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/updateWebsiteState.do")
	@ResponseBody
	public String updateWebsiteState(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			Integer state) throws Exception{
		try{
			
			PrivilegeUtil.checkPriviege(session,request,response,"修改","网站管理");
			Website website=new Website();
			website.setId(id);
			website.setState(state);
			if(state==0){
				websiteMapper.updateExtensionState(id);
				website.setDisable_time(new Timestamp(System.currentTimeMillis()));
			}else{
				website.setEnable_time(new Timestamp(System.currentTimeMillis()));
			}
			
			websiteMapper.updateWebsiteState(website);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改网站状态失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改网站状态失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/deleteWebsite.do")
	@ResponseBody
	public String deleteWebsite(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id) throws Exception{
		try{
			
			PrivilegeUtil.checkPriviege(session,request,response,"删除","网站管理");
			
			websiteMapper.deleteWebsite(id);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("删除网站失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除网站失败(异常)--"+e.getMessage());
			throw e;
		}
	}
}
