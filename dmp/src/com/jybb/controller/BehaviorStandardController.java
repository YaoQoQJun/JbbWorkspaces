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
import com.jybb.mapper.DataItemMapper;
import com.jybb.pojo.Data;
import com.jybb.pojo.DataItem;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/behaviorStandard")
public class BehaviorStandardController {
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private DataItemMapper dataItemMapper;
	
	private Logger logger=LoggerFactory.getLogger(BehaviorStandardController.class);
	
	
	@RequestMapping("/toBehaviorStandard.do")
	public ModelAndView toBehaviorStandard(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","行为标准数据");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		List<DataItem> dataItems=dataItemMapper.findAll(3);
		
		return new ModelAndView("behavior_standard/behavior_standard")
		.addObject("privileges", privileges)
		.addObject("dataItems", dataItems);
	}
	
	@RequestMapping("/getDatasByDataItemId.do")
	@ResponseBody
	public List<Data> getDatasByDataItemId(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer data_item_id){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","行为标准数据");
		List<Data> datas=dataItemMapper.getDatasByDataItemId(data_item_id);
		return datas;
		
	}
	
	
	@RequestMapping("/addDataItem.do")
	@ResponseBody
	public String addDataItem(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String data_item_name,
			String hbase_name,
			String exception_range,
			String data_source) throws Exception{
		try {
			
			PrivilegeUtil.checkPriviege(session,request,response,"增加","行为标准数据");
			
			DataItem dataItem=new DataItem();
			dataItem.setData_item_name(data_item_name);
			dataItem.setHbase_name(hbase_name);
			dataItem.setException_range(Float.parseFloat(exception_range));
			dataItem.setData_source(data_source);
			dataItem.setState(3);
			
			dataItemMapper.addDataItem(dataItem);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("添加行为标准数据项失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("添加行为标准数据项失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/updateDataItem.do")
	@ResponseBody
	public String updateDataItem(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer data_item_id,
			String data_item_name,
			String hbase_name,
			String exception_range,
			String data_source) throws Exception{
		try {
			
			PrivilegeUtil.checkPriviege(session,request,response,"修改","行为标准数据");
			
			DataItem dataItem=new DataItem();
			dataItem.setId(data_item_id);
			dataItem.setData_item_name(data_item_name);
			dataItem.setHbase_name(hbase_name);
			dataItem.setException_range(Float.parseFloat(exception_range));
			dataItem.setData_source(data_source);
			
			dataItemMapper.updateDataItem(dataItem);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改行为标准数据项失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改行为标准数据项失败(异常)--"+e.getMessage());
			throw e;
		}
		
	}
	
	
	@RequestMapping("/addData.do")
	@ResponseBody
	public Map<String,Object> addData(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String data_name,
			String proportion,
			Integer data_item_id) throws Exception{
		try {
			Map<String,Object> map=new HashMap<String,Object>();
			PrivilegeUtil.checkPriviege(session,request,response,"增加","行为标准数据");
			
			Data data=new Data();
			data.setData_name(data_name);
			data.setProportion(Float.parseFloat(proportion));
			data.setData_item_id(data_item_id);
			
			dataItemMapper.addData(data);
			
			map.put("result", "ok");
			map.put("data_id", data.getId());
			
			return map;
		} catch (DataAccessException e) {
			logger.warn("添加行为标准数据失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("添加行为标准数据失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/deleteData.do")
	@ResponseBody
	public String deleteData(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer data_id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"删除","行为标准数据");
			dataItemMapper.deleteData(data_id);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("删除行为标准数据失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除行为标准数据失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	@RequestMapping("/deleteDataItem.do")
	@ResponseBody
	public String deleteDataItem(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer data_item_id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"删除","行为标准数据");
			dataItemMapper.deleteDataByDataItemId(data_item_id);
			dataItemMapper.deleteDataItem(data_item_id);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("删除行为标准数据项失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除行为标准数据项失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	
}
