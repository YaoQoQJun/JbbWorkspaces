package com.jybb.controller;



import java.io.IOException;
import java.net.URLDecoder;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jybb.mapper.AppChannelMapper;
import com.jybb.mapper.AppEventMapper;
import com.jybb.mapper.AppEventSetMapper;
import com.jybb.mapper.AppVersionMapper;
import com.jybb.mapper.DisableMapper;
import com.jybb.mapper.ExtensionMapper;
import com.jybb.mapper.HbaseMapper;
import com.jybb.pojo.AppChannel;
import com.jybb.pojo.AppEventSet;
import com.jybb.pojo.AppVersion;
import com.jybb.pojo.Disable;
import com.jybb.pojo.Extension;
import com.jybb.pojo.Seo;


@Controller
@RequestMapping("/appEvent")
public class AppEventController {
	
	private Logger logger=LoggerFactory.getLogger(AppEventController.class);
	
	@Autowired
	private ExtensionMapper extensionMapper;
	
	@Autowired
	private HbaseMapper hbaseMapper;
	
	@Autowired
	private DisableMapper disableMapper;
	
	@Autowired
	private AppChannelMapper appChannelMapper;
	
	@Autowired
	private AppVersionMapper appVersionMapper;
	
	@Autowired
	private AppEventMapper appEventMapper;
	
	@Autowired
	private AppEventSetMapper appEventSetMapper;
	
	private static final Integer PAGE_SIZE=7;
	
	@RequestMapping("toAppEvent.do")
	public ModelAndView toAppEvent(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,String client_model,String channel_name,String app_version,Integer page) throws IOException{
		
		
		if(page==null||page==0){
			page=1;
		}
		Integer start=(page-1)*PAGE_SIZE;
		List<AppEventSet> appEventSets=appEventSetMapper.findAllByPage(start,PAGE_SIZE);
		Integer total=appEventSetMapper.count();
		Integer totalPage=total%PAGE_SIZE==0?total/PAGE_SIZE:total/PAGE_SIZE+1;
		
		
		if(client_model==null||client_model==""){
			client_model="0";
		}
		
		if(channel_name==null||channel_name==""){
			channel_name="0";
		}
		
		if(app_version==null||app_version==""){
			app_version="0";
		}
		
		String tStartTime=getStartTime(System.currentTimeMillis())+"";
		String tEndTime=getEndTime(System.currentTimeMillis())+"";
		
		String yStartTime=getStartTime(System.currentTimeMillis()-1000*60*60*24)+"";
		String yEndTime=getEndTime(System.currentTimeMillis()-1000*60*60*24)+"";
		
		List<Map<String,String>> lists=new ArrayList<Map<String,String>>();
		
		for (int i = 0; i < appEventSets.size(); i++) {
			Map<String,String> map=new HashMap<String,String>();
			AppEventSet appEventSet=appEventSets.get(i);
			String event_name=appEventSet.getApp_event_name();
			String event_remark=appEventSet.getApp_event_remark();
			
			//1--根据用户去重，统计用户在该事件上出发的消息量
			Integer todayCount=appEventMapper.countEvent(event_name,client_model,channel_name,app_version,tStartTime,tEndTime);
			Integer yesterdayCount=appEventMapper.countEvent(event_name,client_model,channel_name,app_version,yStartTime,yEndTime);
			Integer todayUserCount=appEventMapper.countUser(event_name,client_model,channel_name,app_version,tStartTime,tEndTime);
			Integer yesterdayUserCount=appEventMapper.countUser(event_name,client_model,channel_name,app_version,yStartTime,yEndTime);
			
			if(todayCount==null){
				todayCount=0;
			}
			if(yesterdayCount==null){
				yesterdayCount=0;
			}
			if(todayUserCount==null){
				todayUserCount=0;
			}
			if(yesterdayUserCount==null){
				yesterdayUserCount=0;
			}
			
			
			map.put("event_name", event_name);
			map.put("event_remark", event_remark);
			map.put("todayCount", todayCount+"");
			map.put("yesterdayCount", yesterdayCount+"");
			map.put("todayUserCount", todayUserCount+"");
			map.put("yesterdayUserCount", yesterdayUserCount+"");
			lists.add(map);
		}
	
		List<AppChannel> appChannels=appChannelMapper.findAll();
		List<AppVersion> appVersions=appVersionMapper.findAll();
		
		return new ModelAndView("appEvent/appEvent")
			.addObject("page", page)
			.addObject("total", total)
			.addObject("totalPage", totalPage)
			.addObject("appChannels", appChannels)
			.addObject("appVersions", appVersions)
			.addObject("lists", lists)
			.addObject("client_model", client_model)
			.addObject("channel_name", channel_name)
			.addObject("app_version", app_version);
	}
	
	
	@RequestMapping("toAppEventDetail.do")
	public ModelAndView toAppEventDetail(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String event_name,
			String client_model,
			String channel_name,
			String app_version,
			String startDate,
			String endDate) throws IOException{
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(startDate==null||"".equals(startDate)){
			startDate=sdf.format(new Date());
		}
		if(endDate==null||"".equals(endDate)){
			endDate=sdf.format(new Date());
		}
		
		
		if(client_model==null||client_model==""){
			client_model="0";
		}
		
		if(channel_name==null||channel_name==""){
			channel_name="0";
		}
		
		if(app_version==null||app_version==""){
			app_version="0";
		}
		
		
		List<AppChannel> appChannels=appChannelMapper.findAll();
		List<AppVersion> appVersions=appVersionMapper.findAll();
		AppEventSet appEventSet=appEventSetMapper.findAppEventSetByAppEventName(event_name, null);
		
		return new ModelAndView("appEvent/appEventDetail")
			.addObject("appChannels", appChannels)
			.addObject("appVersions", appVersions)
			.addObject("client_model", client_model)
			.addObject("channel_name", channel_name)
			.addObject("app_version", app_version)
			.addObject("event_name", event_name)
			.addObject("event_remark",appEventSet.getApp_event_remark())
			.addObject("endDate", endDate)
			.addObject("startDate", startDate);
	}
	
	
	@RequestMapping("/getDatas.do")
	@ResponseBody
	public Map<String,Object> getDatas(
			String state,String client_model,String channel_name,String app_version,
			String startDate,String endDate,String event_name) throws IOException{
		try {
		
		
		List<String>  arr=new ArrayList<String>();
		List<Integer> countEvents=new ArrayList<Integer>();
		List<Integer> countUsers=new ArrayList<Integer>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date sDate=sdf.parse(startDate);
		Date eDate=sdf.parse(endDate);
	
		if(eDate.getTime()-sDate.getTime()==0){
			
			for (int i = 0; i < 24; i++) {
				Date date2=new Date(getHourTime(sDate.getTime(), i));
				Date date3=new Date(getHourTime(sDate.getTime(), i+1));
				String t5=date2.getTime()+"";
				String t6=date3.getTime()+"";
				
				Integer countEvent=appEventMapper.countEvent(event_name, client_model, channel_name, app_version, t5, t6);
				Integer countUser=appEventMapper.countUser(event_name, client_model, channel_name, app_version, t5, t6);
				
				if(countEvent==null){
					countEvent=0;
				}
				if(countUser==null){
					countUser=0;
				}
				String str=i+"点";
				
				countEvents.add(countEvent);
				countUsers.add(countUser);
				
				arr.add(str); 
				
			}
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000l){
			
			for (int i = 0; i < 24; i++) {
				
				Date date2=new Date(getHourTime(sDate.getTime(), i));
				Date date3=new Date(getHourTime(sDate.getTime(), i+1));
				String t5=date2.getTime()+"";
				String t6=date3.getTime()+"";
				
				Integer countEvent=appEventMapper.countEvent(event_name, client_model, channel_name, app_version, t5, t6);
				Integer countUser=appEventMapper.countUser(event_name, client_model, channel_name, app_version, t5, t6);
				
				if(countEvent==null){
					countEvent=0;
				}
				if(countUser==null){
					countUser=0;
				}
				
				String str=i+"点";
				countEvents.add(countEvent);
				countUsers.add(countUser);
				
				arr.add(str); 
			}
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000*7l){
			
			for (int i = 7; i >0; i--) {
				
				Calendar cal2 = Calendar.getInstance(); 
				cal2.setTimeInMillis(eDate.getTime());
				cal2.set(Calendar.DATE, cal2.get(Calendar.DATE)-i);
				Date date2=new Date(getStartTime(cal2.getTimeInMillis()));
				Date date3=new Date(getEndTime(cal2.getTimeInMillis()));
				
				String t5=date2.getTime()+"";
				String t6=date3.getTime()+"";
				
				SimpleDateFormat sdf1=new SimpleDateFormat("M-d");
				String str=sdf1.format(date2);
				
				Integer countEvent=appEventMapper.countEvent(event_name, client_model, channel_name, app_version, t5, t6);
				Integer countUser=appEventMapper.countUser(event_name, client_model, channel_name, app_version, t5, t6);
				
				if(countEvent==null){
					countEvent=0;
				}
				if(countUser==null){
					countUser=0;
				}
				
				countEvents.add(countEvent);
				countUsers.add(countUser);
				
				arr.add(str); 
			}
			
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000*30l){
			for (int i = 30; i >0; i--) {
				
				Calendar cal2 = Calendar.getInstance(); 
				cal2.setTimeInMillis(eDate.getTime());
				cal2.set(Calendar.DATE, cal2.get(Calendar.DATE)-i);
				Date date2=new Date(getStartTime(cal2.getTimeInMillis()));
				Date date3=new Date(getEndTime(cal2.getTimeInMillis()));
				
				String t5=date2.getTime()+"";
				String t6=date3.getTime()+"";
				
				SimpleDateFormat sdf1=new SimpleDateFormat("M-d");
				String str=sdf1.format(date2);
				
				Integer countEvent=appEventMapper.countEvent(event_name, client_model, channel_name, app_version, t5, t6);
				Integer countUser=appEventMapper.countUser(event_name, client_model, channel_name, app_version, t5, t6);
				
				if(countEvent==null){
					countEvent=0;
				}
				if(countUser==null){
					countUser=0;
				}
				
				countEvents.add(countEvent);
				countUsers.add(countUser);
				
				arr.add(str); 
			}
		}else{
			int i=(int)((eDate.getTime()-sDate.getTime())/1000/60/60/24);
			for (; i >0; i--) {
				Calendar cal2 = Calendar.getInstance(); 
				cal2.setTimeInMillis(eDate.getTime());
				cal2.set(Calendar.DATE, cal2.get(Calendar.DATE)-i);
				Date date2=new Date(getStartTime(cal2.getTimeInMillis()));
				Date date3=new Date(getEndTime(cal2.getTimeInMillis()));
				
				String t5=date2.getTime()+"";
				String t6=date3.getTime()+"";
				
				SimpleDateFormat sdf1=new SimpleDateFormat("M-d");
				String str=sdf1.format(date2);
				
				Integer countEvent=appEventMapper.countEvent(event_name, client_model, channel_name, app_version, t5, t6);
				Integer countUser=appEventMapper.countUser(event_name, client_model, channel_name, app_version, t5, t6);
				
				if(countEvent==null){
					countEvent=0;
				}
				if(countUser==null){
					countUser=0;
				}
				
				countEvents.add(countEvent);
				countUsers.add(countUser);
				
				arr.add(str); 
			}
		}
		
		Map<String,Object> map=new HashMap<String, Object>();
		
		map.put("countEvents", countEvents);
		map.put("countUsers", countUsers);
		map.put("arr", arr);
		
		return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	//当日 00:00:00
	private static long getStartTime(Long time){
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTimeInMillis();
	}
	//当日小时时间
	private static long getHourTime(Long time,Integer Hour){
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, Hour); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTimeInMillis();
	}
	//当日 23:59:59
	private static long getEndTime(Long time){
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 23); 
		cal.set(Calendar.SECOND, 59); 
		cal.set(Calendar.MINUTE, 59); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTime().getTime();
	}

}
