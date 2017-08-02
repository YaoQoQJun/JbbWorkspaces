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
import com.jybb.pojo.AppChannel;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/appChannel")
public class AppChannelController {
	
	private Logger logger=LoggerFactory.getLogger(AppChannelController.class);
	private static final Integer PAGE_SIZE=7;
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private AppChannelMapper appChannelMapper;
	
	@RequestMapping("toAppChannel.do")
	public ModelAndView toLogin(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","APP渠道");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		
		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		
		List<AppChannel> appChannels=appChannelMapper.findAllByPage(start,PAGE_SIZE);
		Integer total=appChannelMapper.count();
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		return new ModelAndView("appChannel/appChannel")
		.addObject("privileges", privileges)
		.addObject("page", page)
		.addObject("total", total)
		.addObject("appChannels", appChannels)
		.addObject("totalPage", totalPage);
	}
	
	@RequestMapping("/addAppChannel.do")
	@ResponseBody
	public String addAppChannel(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String app_channel_name) throws Exception{

		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","APP渠道");
			
			AppChannel appChannel=new AppChannel();
			appChannel.setApp_channel_name(app_channel_name);
			
			appChannelMapper.addAppChannel(appChannel);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("保存APP渠道失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存APP渠道失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/updateAppChannel.do")
	@ResponseBody
	public String updateAppChannel(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			String app_channel_name) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"修改","APP渠道");
			
			AppChannel appchannel=new AppChannel();
			appchannel.setId(id);
			appchannel.setApp_channel_name(app_channel_name);
			
			appChannelMapper.updateAppChannel(appchannel);
			
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("修改APP渠道失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改APP渠道失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/deleteAppChannel.do")
	@ResponseBody
	public String deleteAppChannel(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,Integer id) throws Exception{
		
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"删除","APP渠道");
			
			appChannelMapper.deleteAppChannel(id);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("删除APP渠道失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除APP渠道失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/checkAppChannelName.do")
	@ResponseBody
	public String checkAppChannelName(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String app_channel_name,
			Integer id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"访问","APP渠道");
			AppChannel appChannel=appChannelMapper.findAppChannelByAppChannelName(app_channel_name,id);
			if(appChannel==null){
				return "ok";
			}else{
				return "no";
			}
			
		} catch (DataAccessException e) {
			logger.warn("检查APP渠道名称是否存在(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("检查APP渠道名称是否存在(异常)--"+e.getMessage());
			throw e;
		}	
	}
	
	
}
