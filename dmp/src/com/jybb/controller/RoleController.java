package com.jybb.controller;


import java.util.Arrays;
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
import com.jybb.mapper.RoleMapper;
import com.jybb.pojo.Model;
import com.jybb.pojo.Role;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/role")
public class RoleController {
	
	private Logger logger=LoggerFactory.getLogger(RoleController.class);
	private static final Integer PAGE_SIZE=7;
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@RequestMapping("toRole.do")
	public ModelAndView toRole(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","角色管理");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
	
		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		
	  	List<Role> roles=roleMapper.findAll(start,PAGE_SIZE);
        Integer total=roleMapper.countRole();
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		
		return new ModelAndView("role/role")
		.addObject("privileges", privileges)
		.addObject("page", page)
		.addObject("total", total)
		.addObject("roles", roles)
		.addObject("totalPage", totalPage);
		
	}
	
	
//  page--当前第几页  rows--每页显示的记录数  
	@RequestMapping(value="/getRoles.do")
	@ResponseBody
	public Map<String, Object> getRoles(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String rows,
			String page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","角色管理");
		
		Map<String, Object> map=new HashMap<String,Object>();
		 //当前页  
        int intPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
        //每页显示条数  
        int number = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
        //每页的开始记录  第一页为1  第二页为number +1   
        int start = (intPage-1)*number;  
        
        List<Role> roles=roleMapper.findAll(start,number);
        Integer total=roleMapper.countRole();
        map.put("total", total);//total键 存放总记录数，必须的  
        map.put("rows", roles);//rows键 存放每页记录 list  
		return map;
	}
	
	/**
	 * 添加角色
	 * @param session
	 * @param request
	 * @param response
	 * @param model 
	 * @param role 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/addRole.do")
	@ResponseBody
	public String addRole(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String[] model,
			Role role) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","角色管理");
			roleMapper.addRole(role);
			
			for (int i = 0; i < model.length; i++) {
				String[] str=model[i].split("-");
				String model_name=str[0];
				
				for (int j = 1; j < str.length; j++) {
					Integer privilege_model_id=roleMapper.getPrivilegeModelId(model_name,str[j]);
					roleMapper.addRolePrivilegeModel(role.getId(),privilege_model_id);
				}
				
			}
			return "true";
		} catch (DataAccessException e) {
			logger.warn("增加角色失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("增加角色失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	/**
	 * 修改角色
	 * @param session
	 * @param request
	 * @param response
	 * @param model
	 * @param role_id
	 * @param role_name
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/updateRole.do")
	@ResponseBody
	public String updateRole(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String[] model,
			Integer role_id,
			String role_name) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"修改","角色管理");
			
			roleMapper.updateRole(role_id,role_name);
			
			roleMapper.deleteRolePrivilegeModelByRoleId(role_id);
			System.out.println(Arrays.toString(model));
			
			for (int i = 0; i < model.length; i++) {
				String[] str=model[i].split("-");
				String model_name=str[0];
				
				for (int j = 1; j < str.length; j++) {
					Integer privilege_model_id=roleMapper.getPrivilegeModelId(model_name,str[j]);
					roleMapper.addRolePrivilegeModel(role_id,privilege_model_id);
				}
				
			}
			return "true";
		} catch (DataAccessException e) {
			logger.warn("修改角色失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改角色失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	/**
	 * 删除角色
	 * @param session
	 * @param request
	 * @param response
	 * @param role_id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/deleteRole.do")
	@ResponseBody
	public String deleteRole(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer role_id) throws Exception{
		
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"删除","角色管理");
			
			adminMapper.deleteAdminRoleByRoleId(role_id);
			roleMapper.deleteRolePrivilegeModelByRoleId(role_id);
			roleMapper.deleteRoleById(role_id);
			return "true";
		} catch (DataAccessException e) {
			logger.warn("删除角色失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除角色失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping(value="/getModelPrivilege.do")
	@ResponseBody
	public Map<String, Object> getModelPrivilege(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","角色管理");
		
		Map<String, Object> map=new HashMap<String,Object>();
		
		List<Model> models=roleMapper.getModels();
		
		for (int i = 0; i < models.size(); i++) {
			Model model=models.get(i);
			List<String> privilege_names=roleMapper.getPrivilegeNames(model.getId());
			map.put(model.getModel_name(), privilege_names);
		}
		
		return map;
	}
	
	@RequestMapping(value="/getModelPrivilegeByRole_id.do")
	@ResponseBody
	public Map<String, Object> getModelPrivilegeByRole_id(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer role_id){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","角色管理");
		
		
		Map<String, Object> map1=new HashMap<String,Object>();
		
		List<Model> models=roleMapper.getModels();
		
		for (int i = 0; i < models.size(); i++) {
			Model model=models.get(i);
			List<String> privilege_names=roleMapper.getPrivilegeNames(model.getId());
			map1.put(model.getModel_name(), privilege_names);
		}
		
		Map<String, Object> map2=new HashMap<String,Object>();
		
		List<Model> models2=roleMapper.getModelsByRoleId(role_id);
		
		for (int i = 0; i < models2.size(); i++) {
			Model model=models2.get(i);
			if(model==null){
				continue;
			}
			List<String> privilege_names=roleMapper.getPrivilegeNamesByRoleId(model.getId(),role_id);
			map2.put(model.getModel_name(), privilege_names);
		}
		
		Map<String, Object> map3=new HashMap<String,Object>();
		
		map3.put("m1", map1);
		map3.put("m2", map2);
		
		return map3;
	}
	
	/**
	 * 检查角色名称是否存在
	 * @param session
	 * @param request
	 * @param response
	 * @param role_name 角色名称
	 * @param role_id	角色id
	 * @return 角色名称存在返回no,不存在返回 ok
	 * @throws Exception 
	 */
	@RequestMapping("/checkRoleByRoleName.do")
	@ResponseBody
	public String checkRoleByRoleName(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String role_name,
			Integer role_id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"访问","角色管理");
			
			Role role=roleMapper.findRoleByRoleName(role_name,role_id);
			
			if(role==null){
				return "ok";
			}else{
				return "no";
			}
			
		} catch (DataAccessException e) {
			logger.warn("检查角色名称是否存在(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("检查角色名称是否存在(异常)--"+e.getMessage());
			throw e;
		}	
	}
}
