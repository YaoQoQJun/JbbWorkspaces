package com.jybb.controller;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jybb.mapper.AdminMapper;
import com.jybb.mapper.DataItemMapper;
import com.jybb.mapper.DataUVMapper;
import com.jybb.mapper.DisableMapper;
import com.jybb.mapper.ExtensionMapper;
import com.jybb.mapper.HbaseMapper;
import com.jybb.pojo.Admin;
import com.jybb.pojo.Data;
import com.jybb.pojo.DataItem;
import com.jybb.pojo.Disable;
import com.jybb.pojo.Extension;
import com.jybb.pojo.HbaseDetail;
import com.jybb.pojo.Seo;
import com.jybb.util.PrivilegeUtil;


@Controller
@RequestMapping("/monitor")
public class MonitorController {
	
	private Logger logger=LoggerFactory.getLogger(MonitorController.class);
	
	private static final Integer PAGE_SIZE=7;
	@Autowired
	private AdminMapper adminMapper;
	
	@Autowired
	private ExtensionMapper extensionMapper;
	
	@Autowired
	private DataItemMapper dataItemMapper;
	
	@Autowired
	private HbaseMapper hbaseMapper;
	
	@Autowired
	private DataUVMapper dataUVMapper;
	
	@Autowired
	private DisableMapper disableMapper;
	
	
	@RequestMapping("/toMonitor.do")
	public ModelAndView toMonitor(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			Integer page,
			String date) throws IOException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(date==null||""==date){
			date=sdf.format(new Date());
		}
		Admin admin=(Admin)session.getAttribute("admin");
		
		PrivilegeUtil.checkPriviege(session,request,response,"访问","数据监控");
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		
		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		List<Extension> extensions=null;
		Integer total=0;
		
		long t1=0;
		long t2=0;
		
		if(date!=null&&!"".equals(date)){
			try {
				Date date2=sdf.parse(date);
				t1=getStartTime(date2.getTime());
				t2=getEndTime(date2.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}else{
			t1=getStartTime(System.currentTimeMillis());
			t2=getEndTime(System.currentTimeMillis());
		}
		
		extensions=extensionMapper.findExtensionsByPageAndState(start,PAGE_SIZE,1,admin.getChannel_id());
		List<Disable> disables=disableMapper.findDisables();
		total=extensionMapper.countExtensionsByState(1,admin.getChannel_id());
		
		List<Integer> pvs=new ArrayList<Integer>();
		List<Integer> uvs=new ArrayList<Integer>();
		List<Integer> ips=new ArrayList<Integer>();
		
		for (int i = 0; i < extensions.size(); i++) {
			Extension extension=extensions.get(i);
			Seo seo=hbaseMapper.getExtensionDayData(extension.getId(), t1, t2);
			pvs.add(seo.getPvs());
			uvs.add(seo.getUvs());
			ips.add(seo.getIps());
		}
		
		for (int j = 0; j < disables.size(); j++) {
			Disable disable=disables.get(j);
			if(disable.getState()==1){
				Seo seo=hbaseMapper.getDisableDayData(disable.getId(),t1,t2);
				if(pvs.size()==0){
					pvs.set(0, 0);
				}else{
					pvs.set(0, pvs.get(0)-seo.getPvs());
				}
				
				if(uvs.size()==0){
					uvs.set(0, 0);
				}else{
					uvs.set(0, uvs.get(0)-seo.getUvs());
				}
				
				if(ips.size()==0){
					ips.set(0, 0);
				}else{
					ips.set(0, ips.get(0)-seo.getIps());
				}
			}
		}
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		return new ModelAndView("monitor/monitor")
		.addObject("page", page)
		.addObject("privileges", privileges)
		.addObject("total", total)
		.addObject("totalPage", totalPage)
		.addObject("extensions", extensions)
		.addObject("pvs", pvs)
		.addObject("uvs", uvs)
		.addObject("ips", ips)
		.addObject("date",date);
	}
	
	/**
	 * 根据推广链接id 查询软件数据详情
	 * @param extension_id 推广链接ID
	 * @param state	 1 软件数据
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/seeSoftWare.do")
	public ModelAndView seeSoftWare(Integer extension_id,Integer state,Integer data_item_id,String date) throws IOException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(date==null||""==date){
			date=sdf.format(new Date());
		}
		state=1;
		Extension extension=extensionMapper.getExtensionById(extension_id);
		
		List<DataItem> dataItems=dataItemMapper.findAll(state);//所有数据项
		
		DataItem di=new DataItem();//当前数据项
		if(data_item_id==null||"".equals(data_item_id)){
			if(dataItems!=null&&dataItems.size()>0){
				di=dataItemMapper.getDataItemByDataItemId(dataItems.get(0).getId());
			}
		}else{
			di=dataItemMapper.getDataItemByDataItemId(data_item_id);
		}
		JSONObject jsonObject1=JSONObject.fromObject(di);
		return new ModelAndView("monitor/software")
			.addObject("dataItems", dataItems)//所有数据项
			.addObject("dataItem", jsonObject1)//当前数据项
			.addObject("extension", extension)
			.addObject("date", date);
		
	}
	
	@RequestMapping("/seeHardware.do")
	public ModelAndView seeHardware(Integer extension_id,Integer state,Integer data_item_id,String date) throws IOException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(date==null||""==date){
			date=sdf.format(new Date());
		}
		state=2;
		Extension extension=extensionMapper.getExtensionById(extension_id);
		
		List<DataItem> dataItems=dataItemMapper.findAll(state);//所有数据项
		
		DataItem di=new DataItem();//当前数据项
		if(data_item_id==null||"".equals(data_item_id)){
			if(dataItems!=null&&dataItems.size()>0){
				di=dataItemMapper.getDataItemByDataItemId(dataItems.get(0).getId());
			}
		}else{
			di=dataItemMapper.getDataItemByDataItemId(data_item_id);
		}
		JSONObject jsonObject1=JSONObject.fromObject(di);
		return new ModelAndView("monitor/hardware")
			.addObject("dataItems", dataItems)//所有数据项
			.addObject("dataItem", jsonObject1)//当前数据项
			.addObject("extension", extension)
			.addObject("date", date);
	}
	
    public ArrayList<String> singleElement(List<String> sonLinks){
      //定义一个临时容器
      ArrayList<String> newAl = new ArrayList<String>();
      //在迭代是循环中next调用一次，就要hasNext判断一次
      Iterator<String> it = sonLinks.iterator();

       while (it.hasNext()){
    	   String obj = it.next();//next()最好调用一次就hasNext()判断一次否则容易发生异常

         if (!newAl.contains(obj))
            newAl.add(obj);
        }
        return newAl;
    }
    
    
	@RequestMapping("/toDetailMonitor.do")
	public ModelAndView toDetailMonitor(
			Integer extension_id,
			String extension_link,
			String link,
			Integer page,
			String search_link,
			String referer_link,
			String date) throws IOException, ParseException{
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(date==null||""==date){
			date=sdf.format(new Date());
		}
		
		if(search_link!=null){
			
			if(page==null||page==0){
				page=1;
			}
			
			Integer start=(page-1)*PAGE_SIZE;
			
			long t1=getStartTime(sdf.parse(date).getTime());
			
			List<HbaseDetail> hbaseDetails=hbaseMapper.findHbaseDetailBySearch_link(search_link,t1,start,PAGE_SIZE);
			Integer total=hbaseMapper.countHbaseDetailBySearch_link(search_link,t1);
			
			Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
			
			return new ModelAndView("monitor/detailMonitor")
			.addObject("extension_id", extension_id)
			.addObject("extension_link", extension_link)
			.addObject("hbaseDetails", hbaseDetails)
			.addObject("page", page)
			.addObject("total", total)
			.addObject("totalPage", totalPage)
			.addObject("date", date)
			.addObject("search_link", search_link)
			.addObject("referer_link", referer_link);
			
		}
		if(page==null||page==0){
			page=1;
		}
		
		Integer start=(page-1)*PAGE_SIZE;
		
		long t1=getStartTime(sdf.parse(date).getTime());
		
		List<HbaseDetail> hbaseDetails=hbaseMapper.findHbaseDetailByPage(extension_link,t1,start,PAGE_SIZE);
		Integer total=hbaseMapper.countHbaseDetail(extension_link,t1);
		
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		return new ModelAndView("monitor/detailMonitor")
		.addObject("extension_id", extension_id)
		.addObject("extension_link", extension_link)
		.addObject("hbaseDetails", hbaseDetails)
		.addObject("page", page)
		.addObject("total", total)
		.addObject("totalPage", totalPage)
		.addObject("date", date)
		.addObject("search_link", search_link)
		.addObject("referer_link", referer_link);
		
	}
	
	@RequestMapping("/getSoftWareDatas.do")
	@ResponseBody
	public Map<String,Object> getSoftWareDatas(String extension_link,String channel_id,Integer dataItem_id,String date) throws IOException, ParseException{
		
		extension_link=extension_link.replaceAll("%26", "&");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(date==null||""==date){
			date=sdf.format(new Date());
		}
		
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMM");
		Date de=sdf.parse(date);
		String tableName="dmp_uv_"+sdf1.format(de);
		
		Date date2=sdf.parse(date);
		long t1=getStartTime(date2.getTime());
		long t2=getEndTime(date2.getTime());
		
		List<Data> datas1=new ArrayList<Data>();//标准数据
		datas1=dataItemMapper.getDatasByDataItemId(dataItem_id);
		
		List<Data> datas2=new ArrayList<Data>();//真实数据
		List<Integer> counts=new ArrayList<Integer>();//异常数据总数
		
		int count=0;
		DataItem di=dataItemMapper.getDataItemByDataItemId(dataItem_id);//当前数据项
		
		//循环该数据项下面的数据
		int a=0;
		String hbaseName=di.getHbase_name();
		float range=di.getException_range();
		if(hbaseName.equals("j_sys")){
			List<Map<String,Object>> maps=dataUVMapper.getSysData(extension_link,t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_sys").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}
				
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
			
		}else if(hbaseName.equals("j_resolution")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_resolutionData(extension_link,t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_resolution").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}else if(hbaseName.equals("j_language")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_languageData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_language").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}else if(hbaseName.equals("j_broser")){
			List<Map<String,Object>> maps=dataUVMapper.getBroserData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_broser").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}else if(hbaseName.equals("j_color_depth")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_color_depthData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_color_depth").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}else if(hbaseName.equals("j_has_lied_browser")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_has_lied_browserData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_has_lied_browser").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				} 
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}	
		}else if(hbaseName.equals("j_has_lied_languages")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_has_lied_languagesData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_has_lied_languages").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}	
		}else if(hbaseName.equals("j_has_lied_os")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_has_lied_osData(extension_link,t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_has_lied_os").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}	
		}else if(hbaseName.equals("j_has_lied_resolution")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_has_lied_resolutionData(extension_link,t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_has_lied_resolution").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}	
		}else if(hbaseName.equals("province")){
			List<Map<String,Object>> maps=dataUVMapper.getProvinceData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("province").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}
		
		counts.add(count);
		
		JSONArray jsonArray2 = JSONArray.fromObject(datas1);
		
        JSONArray jsonArray3 = JSONArray.fromObject(datas2);
        
        Map<String,Object> map=new HashMap<String, Object>();
        
        map.put("datas1", jsonArray2);
        map.put("datas2", jsonArray3);
        map.put("counts", counts);
        map.put("cVisitor", a);
        map.put("hbaseName", hbaseName);
        
		return map;
	}
	
	@RequestMapping("/getLinkSoftWareDatas.do")
	@ResponseBody
	public Map<String,Object> getLinkSoftWareDatas(String link,String channel_id,Integer dataItem_id,String date) throws IOException, ParseException{
		
		String extension_link=link.replaceAll("%26", "&");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(date==null||""==date){
			date=sdf.format(new Date());
		}
		
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyyMM");
		Date de=sdf.parse(date);
		String tableName="dmp_uv_"+sdf1.format(de);
		
		Date date2=sdf.parse(date);
		long t1=getStartTime(date2.getTime());
		long t2=getEndTime(date2.getTime());
		
		List<Data> datas1=new ArrayList<Data>();//标准数据
		datas1=dataItemMapper.getDatasByDataItemId(dataItem_id);
		
		List<Data> datas2=new ArrayList<Data>();//真实数据
		List<Integer> counts=new ArrayList<Integer>();//异常数据总数
		
		
		DataItem di=dataItemMapper.getDataItemByDataItemId(dataItem_id);//当前数据项
		
		//循环该数据项下面的数据
		
		int count=0;
		int a=0;
		
		String hbaseName=di.getHbase_name();
		float range=di.getException_range();
		if(hbaseName.equals("j_sys")){
			List<Map<String,Object>> maps=dataUVMapper.getSysLinkData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_sys").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}
				
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
			
		}else if(hbaseName.equals("j_resolution")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_resolutionLinkData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_resolution").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}else if(hbaseName.equals("j_language")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_languageLinkData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_language").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}else if(hbaseName.equals("j_broser")){
			List<Map<String,Object>> maps=dataUVMapper.getBroserLinkData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_broser").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}else if(hbaseName.equals("j_color_depth")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_color_depthLinkData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_color_depth").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}else if(hbaseName.equals("j_has_lied_browser")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_has_lied_browserLinkData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_has_lied_browser").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				} 
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}	
		}else if(hbaseName.equals("j_has_lied_languages")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_has_lied_languagesLinkData(extension_link,  t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_has_lied_languages").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}	
		}else if(hbaseName.equals("j_has_lied_os")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_has_lied_osLinkData(extension_link,t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_has_lied_os").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}	
		}else if(hbaseName.equals("j_has_lied_resolution")){
			List<Map<String,Object>> maps=dataUVMapper.getJ_has_lied_resolutionLinkData(extension_link,  t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("j_has_lied_resolution").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}	
		}else if(hbaseName.equals("province")){
			List<Map<String,Object>> maps=dataUVMapper.getProvinceLinkData(extension_link, t1, t2,tableName);
			
			for (int i = 0; i < maps.size(); i++) {
				Map<String,Object> map=maps.get(i);
		    	a+=Integer.parseInt(map.get("count").toString());
			}	
			for (int i = 0; i <datas1.size(); i++) {
				Data data=datas1.get(i);
				Data d1=new Data();
				d1.setData_name(data.getData_name());
				int x=0;
				for (int j = 0; j < maps.size(); j++) {
					Map<String,Object> map=maps.get(j);
					String dataName=map.get("province").toString();
					if(dataName.equalsIgnoreCase(data.getData_name())){
						x+=Integer.parseInt(map.get("count").toString());
					}
				}
				float f=0; 
				if(a>0){
					f=(((float)x)/(a))*100;
				}  
				BigDecimal b=new BigDecimal(f);  
				float f1=b.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				d1.setProportion(f1);
				float c=Math.abs(f-data.getProportion());
				if(c<0||c>range){
					count++;
					d1.setData_item_id(1);
				}else{
					d1.setData_item_id(0);
				}
				datas2.add(d1);
			}
		}
		
		
		counts.add(count);
		
		JSONArray jsonArray2 = JSONArray.fromObject(datas1);
		
        JSONArray jsonArray3 = JSONArray.fromObject(datas2);
        
        Map<String,Object> map=new HashMap<String, Object>();
        
        map.put("datas1", jsonArray2);
        map.put("datas2", jsonArray3);
        map.put("counts", counts);
        map.put("cVisitor", a);
        map.put("hbaseName", hbaseName);
        
		return map;
	}
	
	@RequestMapping("/seeLinkSoftware.do")
	public ModelAndView seeLinkSoftware(Integer extension_id,String link,Integer data_item_id,String date) throws IOException{ 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(date==null||""==date){
			date=sdf.format(new Date());
		}
		
		Extension extension=extensionMapper.getExtensionById(extension_id);
		
		List<DataItem> dataItems=dataItemMapper.findAll(1);//所有数据项
		
		DataItem di=new DataItem();//当前数据项
		if(data_item_id==null||"".equals(data_item_id)){
			if(dataItems!=null&&dataItems.size()>0){
				di=dataItemMapper.getDataItemByDataItemId(dataItems.get(0).getId());
			}
		}else{
			di=dataItemMapper.getDataItemByDataItemId(data_item_id);
		}
		JSONObject jsonObject1=JSONObject.fromObject(di);
		return new ModelAndView("monitor/detailSoftware")
			.addObject("dataItems", dataItems)//所有数据项
			.addObject("dataItem", jsonObject1)//当前数据项
			.addObject("extension", extension)
			.addObject("link", link)
			.addObject("date", date);
	}
	
	@RequestMapping("/seeLinkHardware.do")
	public ModelAndView seeLinkHardware(Integer extension_id,String link,Integer data_item_id,String date) throws IOException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(date==null||""==date){
			date=sdf.format(new Date());
		}
		Extension extension=extensionMapper.getExtensionById(extension_id);
		
		List<DataItem> dataItems=dataItemMapper.findAll(2);//所有数据项
		
		DataItem di=new DataItem();//当前数据项
		if(data_item_id==null||"".equals(data_item_id)){
			if(dataItems!=null&&dataItems.size()>0){
				di=dataItemMapper.getDataItemByDataItemId(dataItems.get(0).getId());
			}
		}else{
			di=dataItemMapper.getDataItemByDataItemId(data_item_id);
		}
		JSONObject jsonObject1=JSONObject.fromObject(di);
		return new ModelAndView("monitor/detailHardware")
			.addObject("dataItems", dataItems)//所有数据项
			.addObject("dataItem", jsonObject1)//当前数据项
			.addObject("extension", extension)
			.addObject("link", link)
			.addObject("date", date);
	}
	
	private static long getStartTime(Long time){
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTime().getTime();
	}
		
	private static long getEndTime(Long time){
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 23); 
		cal.set(Calendar.SECOND, 59); 
		cal.set(Calendar.MINUTE, 59); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTime().getTime();
	}
	
	public AdminMapper getAdminMapper() {
		return adminMapper;
	}

	public void setAdminMapper(AdminMapper adminMapper) {
		this.adminMapper = adminMapper;
	}

	public ExtensionMapper getExtensionMapper() {
		return extensionMapper;
	}

	public void setExtensionMapper(ExtensionMapper extensionMapper) {
		this.extensionMapper = extensionMapper;
	}

	public DataItemMapper getDataItemMapper() {
		return dataItemMapper;
	}

	public void setDataItemMapper(DataItemMapper dataItemMapper) {
		this.dataItemMapper = dataItemMapper;
	}

	public HbaseMapper getHbaseMapper() {
		return hbaseMapper;
	}

	public void setHbaseMapper(HbaseMapper hbaseMapper) {
		this.hbaseMapper = hbaseMapper;
	}

	public DataUVMapper getDataUVMapper() {
		return dataUVMapper;
	}

	public void setDataUVMapper(DataUVMapper dataUVMapper) {
		this.dataUVMapper = dataUVMapper;
	}

	public DisableMapper getDisableMapper() {
		return disableMapper;
	}

	public void setDisableMapper(DisableMapper disableMapper) {
		this.disableMapper = disableMapper;
	}
	
	
	
}
