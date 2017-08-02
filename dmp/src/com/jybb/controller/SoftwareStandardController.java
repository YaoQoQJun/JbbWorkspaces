package com.jybb.controller;




import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
@RequestMapping("/softwareStandard")
public class SoftwareStandardController {
	
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private DataItemMapper dataItemMapper;
	
	private Logger logger=LoggerFactory.getLogger(SoftwareStandardController.class);
	
	
	@RequestMapping("/toSoftwareStandard.do")
	public ModelAndView toSoftwareStandard(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer dataItem_id){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","软件标准数据");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		
		List<DataItem> dataItems=dataItemMapper.findAll(1);
		DataItem dataItem=null;
		
		if(dataItems==null||dataItems.size()==0){
			return new ModelAndView("software_standard/software_standard")
			.addObject("privileges", privileges)
			.addObject("dataItems", dataItems);
		}
		
		if(dataItem_id==null||"".equals(dataItem_id)){
			dataItem=dataItemMapper.getDataItemByDataItemId(dataItems.get(0).getId());
		}else{
			dataItem=dataItemMapper.getDataItemByDataItemId(dataItem_id);
		} 
		
		List<Data> datas=dataItemMapper.getDatasByDataItemId(dataItem.getId());
		
		JSONObject jsonObject1=JSONObject.fromObject(dataItem);
		
		JSONArray jsonArray2 = JSONArray.fromObject(datas);
		
		return new ModelAndView("software_standard/software_standard")
		.addObject("privileges", privileges)
		.addObject("dataItems", dataItems)
		.addObject("dataItem",jsonObject1)
		.addObject("datas", jsonArray2);
	}
	
	@RequestMapping("/getDatasByDataItemId.do")
	@ResponseBody
	public List<Data> getDatasByDataItemId(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer data_item_id){
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","软件标准数据");
		List<Data> datas=dataItemMapper.getDatasByDataItemId(data_item_id);
		return datas;
		
	}
	
	
	@RequestMapping("/addDataItem.do")
	@ResponseBody
	public Map<String,Object> addDataItem(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String data_item_name,
			String hbase_name,
			String exception_range,
			String data_source) throws Exception{
		try {
			
			PrivilegeUtil.checkPriviege(session,request,response,"增加","软件标准数据");
			
			DataItem dataItem=new DataItem();
			dataItem.setData_item_name(data_item_name);
			dataItem.setHbase_name(hbase_name);
			dataItem.setException_range(Float.parseFloat(exception_range));
			dataItem.setData_source(data_source);
			dataItem.setState(1);
			
			dataItemMapper.addDataItem(dataItem);
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("result", "ok");
			map.put("dataItem_id", dataItem.getId());
			
			return map;
			
		} catch (DataAccessException e) {
			logger.warn("添加软件标准数据项失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("添加软件标准数据项失败(异常)--"+e.getMessage());
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
			
			PrivilegeUtil.checkPriviege(session,request,response,"修改","软件标准数据");
			
			DataItem dataItem=new DataItem();
			dataItem.setId(data_item_id);
			dataItem.setHbase_name(hbase_name);
			dataItem.setData_item_name(data_item_name);
			dataItem.setException_range(Float.parseFloat(exception_range));
			dataItem.setData_source(data_source);
			
			dataItemMapper.updateDataItem(dataItem);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改软件标准数据项失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改软件标准数据项失败(异常)--"+e.getMessage());
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
			PrivilegeUtil.checkPriviege(session,request,response,"增加","软件标准数据");
			
			Data data=new Data();
			data.setData_name(data_name);
			data.setProportion(Float.parseFloat(proportion));
			data.setData_item_id(data_item_id);
			
			dataItemMapper.addData(data);
			
			map.put("result", "ok");
			map.put("data_id", data.getId());
			
			return map;
		} catch (DataAccessException e) {
			logger.warn("添加软件标准数据失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("添加软件标准数据失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/updateData.do")
	@ResponseBody
	public String updateData(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String data_name,
			String proportion,
			Integer id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"修改","软件标准数据");
			
			Data data=new Data();
			data.setId(id);
			data.setData_name(data_name);
			data.setProportion(Float.parseFloat(proportion));
			
			dataItemMapper.updateData(data);
			
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("修改软件标准数据失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("修改软件标准数据失败(异常)--"+e.getMessage());
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
			PrivilegeUtil.checkPriviege(session,request,response,"删除","软件标准数据");
			dataItemMapper.deleteData(data_id);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("删除软件标准数据失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除软件标准数据失败(异常)--"+e.getMessage());
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
			PrivilegeUtil.checkPriviege(session,request,response,"删除","软件标准数据");
			dataItemMapper.deleteDataByDataItemId(data_item_id);
			dataItemMapper.deleteDataItem(data_item_id);
			
			return "ok";
		} catch (DataAccessException e) {
			logger.warn("删除软件标准数据项失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除软件标准数据项失败(异常)--"+e.getMessage());
			throw e;
		}
	}
	
	
	@RequestMapping("/getData.do")
	@ResponseBody
	public Map<String,Object> getData(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer id) throws Exception{
		try {
			PrivilegeUtil.checkPriviege(session,request,response,"修改","软件标准数据");
			Map<String,Object> map=new HashMap<String, Object>();
			Data data=dataItemMapper.getDataById(id);
			map.put("result", "ok");
			map.put("data", data);
			return map;
		} catch (DataAccessException e) {
			logger.warn("删除软件标准数据项失败(数据库)--"+e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.warn("删除软件标准数据项失败(异常)--"+e.getMessage());
			throw e;
		}
	}
}
