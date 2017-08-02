package com.jybb.controller;


import java.util.HashMap;
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
import com.jybb.mapper.RoleMapper;
import com.jybb.pojo.Admin;
import com.jybb.pojo.Role;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private Logger logger=LoggerFactory.getLogger(AdminController.class);
	private static final Integer PAGE_SIZE=7;
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private ChannelMapper channelMapper;
	
	/**
	 * 检查用户名是否存在
	 * @param session
	 * @param request
	 * @param response
	 * @return 用户名不存在 返回ok,用户名存在 返回no
	 * @throws Exception 
	 */
	@RequestMapping("/checkAdminByUsername.do")
	@ResponseBody
	public String checkAdminByUsername(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String username,
			Integer admin_id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"访问","用户管理");
			Admin admin=adminMapper.findAdminByUsername(username,admin_id);
			
			if(admin==null){
				return "ok";
			}else{
				return "no";
			}
			
		} catch (DataAccessException e) {
			logger.warn("检查用户名是否存在(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("检查用户名是否存在(异常)--"+e.getMessage());
			throw e;
		}	
	}
	
	/**
	 * 获取所有角色名称
	 * @param session
	 * @param request
	 * @param response
	 * @return 所有角色
	 * @throws Exception 
	 */
	@RequestMapping("/getRoles.do")
	@ResponseBody
	public Map<String, Object> getRoles(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,  "访问","用户管理");
			
			Map<String, Object> map=new HashMap<String,Object>();
			List<Role> roles=roleMapper.getRoles();
			map.put("roles", roles);
			return map;
		} catch (DataAccessException e) {
			logger.warn("获取所有角色失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("获取所有角色失败(异常)--"+e.getMessage());
			throw e;
		}	
	}
	
	
	
	/**
	 * 获取所有角色
	 * 以及
	 * 根据用户ID 获取用户所拥有的角色
	 * @param session
	 * @param request
	 * @param response
	 * @param admin_id 用户id
	 * @return 所有角色，用户拥有角色
	 */
	@RequestMapping("/getRolesForUpdate.do")
	@ResponseBody
	public Map<String, Object> getRolesForUpdate(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer admin_id){
		
		PrivilegeUtil.checkPriviege(session,request,response,  "访问","用户管理");
		Map<String, Object> map1=new HashMap<String,Object>();
		List<Role> roles=roleMapper.getRoles();
		map1.put("roles", roles);
		Map<String, Object> map2=new HashMap<String,Object>();
		List<Role> roles2=roleMapper.getRolesByAdminId(admin_id);
		map2.put("roles2", roles2);
		
		Map<String, Object> map3=new HashMap<String,Object>();
		map3.put("map1", map1);
		map3.put("map2", map2);
		
		return map3;
	}
	
	/**
	 * 跳转到用户管理页面
	 * @param session
	 * @param request
	 * @param response
	 * @return 页面，拥有的权限
	 * @throws Exception 
	 */
	@RequestMapping("/toAdmin.do")
	public ModelAndView toAdmin(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"访问","用户管理");
			List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
			
			if(page==null||page==0){
				page=1;
			}
			
			Integer start=(page-1)*PAGE_SIZE;
			
			List<Admin> admins=adminMapper.findAll(start,PAGE_SIZE);
			Integer total=adminMapper.countAdmin();
			
			Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
			
			
			return new ModelAndView("admin/admin")
			.addObject("privileges", privileges)
			.addObject("page", page)
			.addObject("total", total)
			.addObject("admins", admins)
			.addObject("totalPage", totalPage);
			
		} catch (DataAccessException e) {
			logger.warn("跳转到用户管理页面(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("跳转到用户管理页面(异常)--"+e.getMessage());
			throw e;
		}
		
	}
	/**
	 * 获取所有用户数据
	 * @param session
	 * @param request
	 * @param response
	 * @param rows 每页显示的记录数  
	 * @param page 当前第几页
	 * @return 所有用户，总记录数
	 */
	@RequestMapping(value="/getAdmins.do")
	@ResponseBody
	public Map<String, Object> getAdmins(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String rows,
			String page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","用户管理");
		
		Map<String, Object> map=new HashMap<String,Object>();
		 //当前页  
        int intPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
        //每页显示条数  
        int number = Integer.parseInt((rows == null || rows == "0") ? "15":rows);  
        //每页的开始记录  第一页为1  第二页为number +1   
        int start = (intPage-1)*number;  
        
        List<Admin> admins=adminMapper.findAll(start,number);
        Integer total=adminMapper.countAdmin();
        map.put("total", total);//total键 存放总记录数，必须的  
        map.put("rows", admins);//rows键 存放每页记录 list  
		return map;
		
	}
	
	/**
	 * 保存用户数据
	 * @param session
	 * @param request
	 * @param response
	 * @param username 用户名
	 * @param password 密码
	 * @param roles	         拥有的角色id
	 * @return 成功 ok
	 * @throws Exception 
	 */
	@RequestMapping("/addAdmin.do")
	@ResponseBody
	public String addUser(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String username,
			String password,
			String channel_id,
			Integer[] roles) throws Exception{

		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","用户管理");
			
			Admin admin=new Admin();
			admin.setUsername(username);
			admin.setPassword(password);
			admin.setChannel_id(channel_id);
			adminMapper.addAdmin(admin);
			
			if(roles!=null){
				for (int i = 0; i < roles.length; i++) {
					adminMapper.addAdminRole(admin.getId(),roles[i]);
				}
			}
			return "true";
		} catch (DataAccessException e) {
			logger.warn("保存用户失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("保存用户失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 根据id修改用户
	 * @param session
	 * @param request
	 * @param response
	 * @param id 		用户id
	 * @param username	用户名称
	 * @param password	用户密码
	 * @param roles		拥有角色id
	 * @return 成功返回true
	 * @throws Exception 
	 */
	@RequestMapping("/updateAdmin.do")
	@ResponseBody
	public String updateAdmin(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			String username,
			String password,
			String channel_id,
			Integer[] roles) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,  "修改","用户管理");
			
			Admin admin=new Admin();
			admin.setId(id);
			admin.setUsername(username);
			admin.setPassword(password);
			admin.setChannel_id(channel_id);
			
			adminMapper.updateAdmin(admin);
			adminMapper.deleteAdminRoleByAdminId(id);
			if(roles!=null){
				for (int i = 0; i < roles.length; i++) {
					adminMapper.addAdminRole(admin.getId(),roles[i]);
				}
			}
			return "true";
		} catch (DataAccessException e) {
			logger.warn("修改用户失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改用户失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 根据id删除用户
	 * @param session
	 * @param request
	 * @param response
	 * @param id	用户id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/deleteAdmin.do")
	@ResponseBody
	public String deleteAdmin(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,Integer id) throws Exception{
		
		try{
			PrivilegeUtil.checkPriviege(session,request,response,  "删除","用户管理");
			
			adminMapper.deleteAdminRoleByAdminId(id);
			adminMapper.deleteAdmin(id);
			return "true";
		} catch (DataAccessException e) {
			logger.warn("删除用户失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除用户失败(异常)--"+e.getMessage());
			throw e;
		}
	}
}
