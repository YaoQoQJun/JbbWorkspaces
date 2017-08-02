package com.jybb.controller;



import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

import com.jybb.membersMapper.MemberMapper;


@Controller
@RequestMapping("/regStatistics")
public class RegStatisticsController {
	
	private Logger logger=LoggerFactory.getLogger(MonitorController.class);
	
	@Autowired
	private MemberMapper memberMapper;
	
	@RequestMapping("toRegStatistics.do")
	public ModelAndView toRegStatistics(String startDate,String endDate) throws IOException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(startDate==null||"".equals(startDate)){
			startDate=sdf.format(new Date());
		}
		if(endDate==null||"".equals(endDate)){
			endDate=sdf.format(new Date());
		}
		
		return new ModelAndView("regStatistics/regStatistics")
			.addObject("endDate", endDate)
			.addObject("startDate", startDate);
	}
	
	
	@RequestMapping("/getDatas.do")
	@ResponseBody
	public Map<String,Object> getDatas(String state,String startDate,String endDate) throws IOException{
		try {
		
		List<String>  arr=new ArrayList<String>();
		List<Integer> pcs=new ArrayList<Integer>();
		List<Integer> waps=new ArrayList<Integer>();
		List<Integer> apps=new ArrayList<Integer>();
		List<Integer> androids=new ArrayList<Integer>();
		List<Integer> ioss=new ArrayList<Integer>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date sDate=sdf.parse(startDate);
		Date eDate=sdf.parse(endDate);
		
		if(eDate.getTime()-sDate.getTime()==0){//当日
			
			for (int i = 0; i < 24; i++) {
				Date date2=new Date(getHourTime(sDate.getTime(), i));
				Date date3=new Date(getHourTime(sDate.getTime(), i+1));
				
				long t5=date2.getTime()/1000;
				long t6=date3.getTime()/1000;
				
				Integer pc=0;
				Integer wap=0;
				Integer app=0;
				Integer android=0;
				Integer ios=0;
				
				pc=memberMapper.countReg(t5,t6,"pc");
				wap=memberMapper.countReg(t5,t6,"wap");
				
				android=memberMapper.countReg(t5,t6,"android");
				ios=memberMapper.countReg(t5,t6,"ios");
				
				app=android+ios;
				
				String str=i+"点";
				pcs.add(pc);
				waps.add(wap);
				apps.add(app);
				androids.add(android);
				ioss.add(ios);
				arr.add(str); 
			}
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000l){//昨日
			for (int i = 0; i < 24; i++) {
				Date date2=new Date(getHourTime(sDate.getTime(), i));
				Date date3=new Date(getHourTime(sDate.getTime(), i+1));
				
				long t5=date2.getTime()/1000;
				long t6=date3.getTime()/1000;
				
				Integer pc=0;
				Integer wap=0;
				Integer app=0;
				Integer android=0;
				Integer ios=0;
				
				pc=memberMapper.countReg(t5,t6,"pc");
				wap=memberMapper.countReg(t5,t6,"wap");
				android=memberMapper.countReg(t5,t6,"android");
				ios=memberMapper.countReg(t5,t6,"ios");
				app=android+ios;
				
				String str=i+"点";
				pcs.add(pc);
				waps.add(wap);
				apps.add(app);
				androids.add(android);
				ioss.add(ios);
				arr.add(str); 
				
			}
			
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000*7l){
			for (int i = 7; i >0; i--) {
				
				Calendar cal2 = Calendar.getInstance(); 
				cal2.setTimeInMillis(eDate.getTime());
				cal2.set(Calendar.DATE, cal2.get(Calendar.DATE)-i);
				Date date2=new Date(getStartTime(cal2.getTimeInMillis()));
				Date date3=new Date(getEndTime(cal2.getTimeInMillis()));
				
				long t5=date2.getTime()/1000;
				long t6=date3.getTime()/1000;
				
				SimpleDateFormat sdf1=new SimpleDateFormat("M-d");
				String str=sdf1.format(date2);
				
				Integer pc=0;
				Integer wap=0;
				Integer app=0;
				Integer android=0;
				Integer ios=0;
				
				pc=memberMapper.countReg(t5,t6,"pc");
				wap=memberMapper.countReg(t5,t6,"wap");
				android=memberMapper.countReg(t5,t6,"android");
				ios=memberMapper.countReg(t5,t6,"ios");
				app=android+ios;
				
				
				pcs.add(pc);
				waps.add(wap);
				apps.add(app);
				androids.add(android);
				ioss.add(ios);
				arr.add(str); 
			}
			
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000*30l){
			for (int i = 30; i >0; i--) {
				
				Calendar cal2 = Calendar.getInstance(); 
				cal2.setTimeInMillis(eDate.getTime());
				cal2.set(Calendar.DATE, cal2.get(Calendar.DATE)-i);
				Date date2=new Date(getStartTime(cal2.getTimeInMillis()));
				Date date3=new Date(getEndTime(cal2.getTimeInMillis()));
				
				long t5=date2.getTime()/1000;
				long t6=date3.getTime()/1000;
				
				SimpleDateFormat sdf1=new SimpleDateFormat("M-d");
				String str=sdf1.format(date2);
				
				Integer pc=memberMapper.countReg(t5,t6,"pc");
				Integer wap=memberMapper.countReg(t5,t6,"wap");
				Integer android=memberMapper.countReg(t5,t6,"android");
				Integer ios=memberMapper.countReg(t5,t6,"ios");
				Integer app=android+ios;
				
				
				pcs.add(pc);
				waps.add(wap);
				apps.add(app);
				androids.add(android);
				ioss.add(ios);
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
				
				long t5=date2.getTime()/1000;
				long t6=date3.getTime()/1000;
				SimpleDateFormat sdf1=new SimpleDateFormat("M-d");
				String str=sdf1.format(date2);
				
				Integer pc=memberMapper.countReg(t5,t6,"pc");
				Integer wap=memberMapper.countReg(t5,t6,"wap");
				Integer android=memberMapper.countReg(t5,t6,"android");
				Integer ios=memberMapper.countReg(t5,t6,"ios");
				Integer app=android+ios;
				
				
				pcs.add(pc);
				waps.add(wap);
				apps.add(app);
				androids.add(android);
				ioss.add(ios);
				arr.add(str); 
			}
			
		}
		
		
		
		List<Integer> today=new ArrayList<Integer>();
		long t1=getStartTime(System.currentTimeMillis())/1000;
		long t2=getEndTime(System.currentTimeMillis())/1000;  
		
		Integer today_pc=memberMapper.countReg(t1,t2,"pc");
		Integer today_wap=memberMapper.countReg(t1,t2,"wap");
		Integer today_android=memberMapper.countReg(t1,t2,"android");
		Integer today_ios=memberMapper.countReg(t1,t2,"ios");
		Integer today_app=today_android+today_ios;
		
		today.add(today_pc);
		today.add(today_wap);
		today.add(today_app);
		today.add(today_android);
		today.add(today_ios);
		
		List<Integer> yesterday=new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.DATE, cal.get(Calendar.DATE)-1);
		long t3=getStartTime(cal.getTimeInMillis())/1000;
		long t4=getEndTime(cal.getTimeInMillis())/1000;
		
		Integer yesterday_pc=memberMapper.countReg(t3,t4,"pc");
		Integer yesterday_wap=memberMapper.countReg(t3,t4,"wap");
		Integer yesterday_android=memberMapper.countReg(t3,t4,"android");
		Integer yesterday_ios=memberMapper.countReg(t3,t4,"ios");
		Integer yesterday_app=yesterday_android+yesterday_ios;
		
		
		yesterday.add(yesterday_pc);
		yesterday.add(yesterday_wap);
		yesterday.add(yesterday_app);
		yesterday.add(yesterday_android);
		yesterday.add(yesterday_ios);
		
		Integer all=memberMapper.countAllReg();
		Integer allPcReg=memberMapper.countAllPcWapAppReg("pc");
		Integer allWapReg=memberMapper.countAllPcWapAppReg("wap");
		Integer allAndroidReg=memberMapper.countAllPcWapAppReg("android");
		Integer allIosReg=memberMapper.countAllPcWapAppReg("ios");
		Integer allAppReg=allAndroidReg+allIosReg;
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("pcs", pcs);
		map.put("waps", waps);
		map.put("apps", apps);
		map.put("androids", androids);
		map.put("ioss", ioss);
		map.put("arr", arr);
		map.put("today", today);
		map.put("yesterday", yesterday);
		map.put("all", all);
		map.put("allPcReg", allPcReg);
		map.put("allWapReg", allWapReg);
		map.put("allAppReg", allAppReg);
		map.put("allAndroidReg", allAndroidReg);
		map.put("allIosReg", allIosReg);
		
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
