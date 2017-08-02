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
import com.jybb.mapper.ChannelMapper;
import com.jybb.pojo.Channel;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/channel")
public class ChannelController {
	
	private Logger logger=LoggerFactory.getLogger(ChannelController.class);
	private static final Integer PAGE_SIZE=7;
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private ChannelMapper channelMapper;
	
	@RequestMapping("toChannel.do")
	public ModelAndView toLogin(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","渠道管理");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		
		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		
		List<Channel> channels=channelMapper.findAll(start,PAGE_SIZE);
		Integer total=channelMapper.count();
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		return new ModelAndView("channel/channel")
		.addObject("privileges", privileges)
		.addObject("page", page)
		.addObject("total", total)
		.addObject("channels", channels)
		.addObject("totalPage", totalPage);
	}
	
	@RequestMapping("/addChannel.do")
	@ResponseBody
	public String addChannel(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String channel_id,
			String channel_name) throws Exception{

		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","渠道管理");
			
			Channel channel=new Channel();
			channel.setChannel_id(channel_id);
			channel.setChannel_name(channel_name);
			
			channelMapper.addChannel(channel);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("保存渠道失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存渠道失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/updateChannel.do")
	@ResponseBody
	public String updateChannel(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			String channel_id,
			String channel_name) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"修改","渠道管理");
			
			Channel channel=new Channel();
			channel.setId(id);
			channel.setChannel_id(channel_id);
			channel.setChannel_name(channel_name);
			
			channelMapper.updateChannel(channel);
			
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("修改渠道失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改渠道失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/deleteChannel.do")
	@ResponseBody
	public String deleteChannel(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,Integer id) throws Exception{
		
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"删除","渠道管理");
			
			channelMapper.deleteChannel(id);
			
			return "true";
		} catch (DataAccessException e) {
			logger.warn("删除渠道失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除渠道失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/checkChannelId.do")
	@ResponseBody
	public String checkChannelId(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String username,
			String channel_id,
			Integer id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"访问","渠道管理");
			Channel channel=channelMapper.findChannelByChannelId(channel_id,id);
			if(channel==null){
				return "ok";
			}else{
				return "no";
			}
			
		} catch (DataAccessException e) {
			logger.warn("检查渠道编号是否存在(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("检查渠道编号是否存在(异常)--"+e.getMessage());
			throw e;
		}	
	}
}
