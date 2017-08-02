package com.jybb.controller;



import java.io.IOException;
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

import com.jybb.mapper.DisableMapper;
import com.jybb.mapper.ExtensionMapper;
import com.jybb.mapper.HbaseMapper;


@Controller
@RequestMapping("/appIndex")
public class AppIndexController {
	
	private Logger logger=LoggerFactory.getLogger(AppIndexController.class);
	
	@Autowired
	private ExtensionMapper extensionMapper;
	
	@Autowired
	private HbaseMapper hbaseMapper;
	
	@Autowired
	private DisableMapper disableMapper;
	
	@RequestMapping("toAppIndex.do")
	public ModelAndView toIndex(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,String state,String startDate,String endDate) throws IOException{
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(startDate==null||"".equals(startDate)){
			startDate=sdf.format(new Date());
		}
		if(endDate==null||"".equals(endDate)){
			endDate=sdf.format(new Date());
		}
		
		return new ModelAndView("appIndex/appIndex")
		.addObject("endDate", endDate)
		.addObject("startDate", startDate)
		.addObject("state", state);
		
	}
	
	@RequestMapping("/getDatas.do")
	@ResponseBody
	public Map<String,Object> getDatas(String day,String state,String startDate,String endDate) throws IOException{
		Integer AllstartCount=0;
		Integer AllactiveUser=0;
		Integer AllnewUser=0;
		Integer AllnewRegistUser=0;
		
		
		List<String>  arr=new ArrayList<String>();
		
		List<Integer> startCounts=new ArrayList<Integer>();
		List<Integer> activeUsers=new ArrayList<Integer>();
		List<Integer> newUsers=new ArrayList<Integer>();
		List<Integer> newRegistUsers=new ArrayList<Integer>();
		
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date sDate =null;
		Date eDate = null;
		try {
			sDate = sdf.parse(startDate);
			eDate = sdf.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	
		if(eDate.getTime()-sDate.getTime()==0){
			for (int i = 0; i < 24; i++) {
				Date date2=new Date(getHourTime(sDate.getTime(), i));
				long t5=date2.getTime();
				String str=i+"点";
				
				Integer startCount=(int)(Math.random()*1000);
				Integer activeUser=(int)(Math.random()*700);
				Integer newUser=(int)(Math.random()*300);
				Integer newRegistUser=(int)(Math.random()*50);
				
				AllstartCount+=startCount;
				AllactiveUser+=activeUser;
				AllnewUser+=newUser;
				AllnewRegistUser+=newRegistUser;
				
				startCounts.add(startCount);
				activeUsers.add(activeUser);
				newUsers.add(newUser);
				newRegistUsers.add(newRegistUser);
				arr.add(str); 
			}
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000l){
			
			for (int i = 0; i < 24; i++) {
				Date date2=new Date(getHourTime(sDate.getTime(), i));
				long t5=date2.getTime();
				String str=i+"点";
				
				Integer startCount=(int)(Math.random()*1000);
				Integer activeUser=(int)(Math.random()*700);
				Integer newUser=(int)(Math.random()*300);
				Integer newRegistUser=(int)(Math.random()*50);
				
				AllstartCount+=startCount;
				AllactiveUser+=activeUser;
				AllnewUser+=newUser;
				AllnewRegistUser+=newRegistUser;
				
				startCounts.add(startCount);
				activeUsers.add(activeUser);
				newUsers.add(newUser);
				newRegistUsers.add(newRegistUser);
				arr.add(str); 
				
			}
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000*7l){
			
			for (int i = 7; i >0; i--) {
				
				Calendar cal2 = Calendar.getInstance(); 
				cal2.setTimeInMillis(eDate.getTime());
				cal2.set(Calendar.DATE, cal2.get(Calendar.DATE)-i);
				Date date2=new Date(getStartTime(cal2.getTimeInMillis()));
				Date date3=new Date(getEndTime(cal2.getTimeInMillis()));
				
				long t5=date2.getTime();
				long t6=date3.getTime();
				
				SimpleDateFormat sdf1=new SimpleDateFormat("M-d");
				String str=sdf1.format(date2);
				
				Integer startCount=(int)(Math.random()*1000);
				Integer activeUser=(int)(Math.random()*700);
				Integer newUser=(int)(Math.random()*300);
				Integer newRegistUser=(int)(Math.random()*50);
				
				AllstartCount+=startCount;
				AllactiveUser+=activeUser;
				AllnewUser+=newUser;
				AllnewRegistUser+=newRegistUser;
				
				startCounts.add(startCount);
				activeUsers.add(activeUser);
				newUsers.add(newUser);
				newRegistUsers.add(newRegistUser);
				arr.add(str); 
			}
			
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000*30l){
			for (int i = 30; i >0; i--) {
				
				Calendar cal2 = Calendar.getInstance(); 
				cal2.setTimeInMillis(eDate.getTime());
				cal2.set(Calendar.DATE, cal2.get(Calendar.DATE)-i);
				Date date2=new Date(getStartTime(cal2.getTimeInMillis()));
				Date date3=new Date(getEndTime(cal2.getTimeInMillis()));
				
				long t5=date2.getTime();
				long t6=date3.getTime();
				
				SimpleDateFormat sdf1=new SimpleDateFormat("M-d");
				String str=sdf1.format(date2);
				
				Integer startCount=(int)(Math.random()*1000);
				Integer activeUser=(int)(Math.random()*700);
				Integer newUser=(int)(Math.random()*300);
				Integer newRegistUser=(int)(Math.random()*50);
				
				AllstartCount+=startCount;
				AllactiveUser+=activeUser;
				AllnewUser+=newUser;
				AllnewRegistUser+=newRegistUser;
				
				startCounts.add(startCount);
				activeUsers.add(activeUser);
				newUsers.add(newUser);
				newRegistUsers.add(newRegistUser);
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
				
				long t5=date2.getTime();
				long t6=date3.getTime();
				SimpleDateFormat sdf1=new SimpleDateFormat("M-d");
				String str=sdf1.format(date2);
				
				Integer startCount=(int)(Math.random()*1000);
				Integer activeUser=(int)(Math.random()*700);
				Integer newUser=(int)(Math.random()*300);
				Integer newRegistUser=(int)(Math.random()*50);
				
				AllstartCount+=startCount;
				AllactiveUser+=activeUser;
				AllnewUser+=newUser;
				AllnewRegistUser+=newRegistUser;
				
				startCounts.add(startCount);
				activeUsers.add(activeUser);
				newUsers.add(newUser);
				newRegistUsers.add(newRegistUser);
				arr.add(str); 
			}
		}
		
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("AllstartCount", AllstartCount);
		map.put("AllactiveUser", AllactiveUser);
		map.put("AllnewUser", AllnewUser);
		map.put("AllnewRegistUser", AllnewRegistUser);
		map.put("startCounts", startCounts);
		map.put("activeUsers", activeUsers);
		map.put("newUsers", newUsers);
		map.put("newRegistUsers", newRegistUsers);
		map.put("arr", arr);
		
		return map;
		
	}
	
	
	public ArrayList<String> singleElement(List<String> ips){
      //定义一个临时容器
      ArrayList<String> newAl = new ArrayList<String>();
      //在迭代是循环中next调用一次，就要hasNext判断一次
      Iterator<String> it = ips.iterator();

       while (it.hasNext()){
    	   String obj = it.next();//next()最好调用一次就hasNext()判断一次否则容易发生异常

         if (!newAl.contains(obj))
            newAl.add(obj);
        }
        return newAl;
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
