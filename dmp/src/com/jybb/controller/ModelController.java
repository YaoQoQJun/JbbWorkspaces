package com.jybb.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import com.jybb.mapper.ModelMapper;
import com.jybb.pojo.Model;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/model")
public class ModelController {
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private Logger logger=LoggerFactory.getLogger(ModelController.class);
	private static final Integer PAGE_SIZE=7;
	
	
	@RequestMapping("toModel.do")
	public ModelAndView toIndex(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","模块管理");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");

		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		
		List<Model> models=modelMapper.findAll(start, PAGE_SIZE);
		Integer total=modelMapper.countModel();
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		
		return new ModelAndView("model/model")
		.addObject("privileges", privileges)
		.addObject("page", page)
		.addObject("total", total)
		.addObject("models", models)
		.addObject("totalPage", totalPage);
	}
	
	
//  page--当前第几页  rows--每页显示的记录数  
	@RequestMapping(value="/getModels.do")
	@ResponseBody
	public Map<String, Object> getModels(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String rows,
			String page){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","模块管理");
		
		Map<String, Object> map=new HashMap<String,Object>();
		 //当前页  
        int intPage = Integer.parseInt((page == null || page == "0") ? "1":page);  
        //每页显示条数  
        int number = Integer.parseInt((rows == null || rows == "0") ? "10":rows);  
        //每页的开始记录  第一页为1  第二页为number +1   
        int start = (intPage-1)*number;  
        
        List<Model> models=modelMapper.findAll(start,number);
        Integer total=modelMapper.countModel();
        map.put("total", total);//total键 存放总记录数，必须的  
        map.put("rows", models);//rows键 存放每页记录 list  
		return map;
	}
	
	@RequestMapping(value="/addModel.do")
	@ResponseBody
	public String addModel(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Model model,
			String[] privilege_name) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"增加","模块管理");
			modelMapper.addModel(model);
			if(privilege_name!=null){
				for (int i = 0; i < privilege_name.length; i++) {
					if("访问".equals(privilege_name[i])){
						modelMapper.addModelPrivilege(model.getId(),4);
					}else if("删除".equals(privilege_name[i])){
						modelMapper.addModelPrivilege(model.getId(),2);
					}else if("修改".equals(privilege_name[i])){
						modelMapper.addModelPrivilege(model.getId(),3);
					}else if("增加".equals(privilege_name[i])){
						modelMapper.addModelPrivilege(model.getId(),1);
					}
				}
			}
			return "true";
		}catch (DataAccessException e) {
			e.printStackTrace();
			logger.warn("添加模块失败(数据库)---"+e.getMessage());
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			logger.warn("添加模块失败(异常)---"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping(value="/updateModel.do")
	@ResponseBody
	public String updateModel(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id,
			String model_name,
			String model_url,
			String[] privilege_name) throws Exception{
		try{
			PrivilegeUtil.checkPriviege(session,request,response,"修改","模块管理");
			
			if(privilege_name==null){
				privilege_name=new String[]{};
			}
			
			Model model=new Model();
			model.setId(id);
			model.setModel_name(model_name);
			model.setModel_url(model_url);
			
			List<String> privilegeNames=new ArrayList<String>();
			privilegeNames.add("增加");
			privilegeNames.add("修改");
			privilegeNames.add("访问");
			privilegeNames.add("删除");
			
			Iterator<String> iterator=privilegeNames.iterator();
			while(iterator.hasNext()){
				String privilegeName=iterator.next();
				for (int i = 0; i < privilege_name.length; i++) {
					if(privilegeName.equals(privilege_name[i])){
						iterator.remove();
						break;
					}
				}
			}
			
			for (int i = 0; i < privilegeNames.size(); i++) {
				Integer privilege_id=modelMapper.findPrivilegeIdByPrivilegeName(privilegeNames.get(i));
				Integer privilege_model_id=modelMapper.getPrivilegeModelIdByModelIdAndPrivilegeId(id,privilege_id);
				modelMapper.deleteRolePrivilegeModelByPrivilegeModelIds(privilege_model_id);
				modelMapper.deleteModelPrivilegeByModelIdAndPrivilegeId(id,privilege_id);
			}
			
			modelMapper.updateModel(model);
			for (int i = 0; i < privilege_name.length; i++) {
				try {
					if("访问".equals(privilege_name[i])){
						modelMapper.addModelPrivilege(model.getId(),4);
					}else if("删除".equals(privilege_name[i])){
						modelMapper.addModelPrivilege(model.getId(),2);
					}else if("修改".equals(privilege_name[i])){
						modelMapper.addModelPrivilege(model.getId(),3);
					}else if("增加".equals(privilege_name[i])){
						modelMapper.addModelPrivilege(model.getId(),1);
					}
				} catch (Exception e) {
					continue;
				}
			}
			return "true";
		}catch (DataAccessException e) {
			e.printStackTrace();
			logger.warn("修改模块失败(数据库)---"+e.getMessage());
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			logger.warn("修改模块失败(异常)---"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping(value="/getPrivileges.do")
	@ResponseBody
	public List<String> getPrivileges(Integer model_id){
		List<String> privileges=modelMapper.getPrivileges(model_id);
		return privileges;
	}
	
	@RequestMapping(value="/deleteModel.do")
	@ResponseBody
	public String deleteModel(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer model_id) throws Exception{
		PrivilegeUtil.checkPriviege(session,request,response,"删除","模块管理");
		try {
			
			List<Integer> privilegeModelIds=modelMapper.getPrivilegeModelIds(model_id);
			for (int i = 0; i < privilegeModelIds.size(); i++) {
				modelMapper.deleteRolePrivilegeModelByPrivilegeModelIds(privilegeModelIds.get(i));
			}
			
			modelMapper.deleteModelPrivilege(model_id);
			modelMapper.deleteModel(model_id);
			return "true";
		}catch (DataAccessException e) {
			e.printStackTrace();
			logger.warn("删除模块失败(数据库)---"+e.getMessage());
			throw e;
		}catch (Exception e) {
			e.printStackTrace();
			logger.warn("删除模块失败(异常)---"+e.getMessage());
			throw e;
		}
	}
	
	
}
