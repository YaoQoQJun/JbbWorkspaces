package com.jybb.controller;



import java.io.IOException;
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
import com.jybb.pojo.Disable;
import com.jybb.pojo.Extension;
import com.jybb.pojo.Seo;


@Controller
@RequestMapping("/index")
public class IndexController {
	
	private Logger logger=LoggerFactory.getLogger(MonitorController.class);
	
	@Autowired
	private ExtensionMapper extensionMapper;
	
	@Autowired
	private HbaseMapper hbaseMapper;
	
	@Autowired
	private DisableMapper disableMapper;
	
	@RequestMapping("toIndex.do")
	public ModelAndView toIndex(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,String url,String startDate,String endDate) throws IOException{
		
		List<Extension> extensions=extensionMapper.findExtensions();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(startDate==null||"".equals(startDate)){
			startDate=sdf.format(new Date());
		}
		if(endDate==null||"".equals(endDate)){
			endDate=sdf.format(new Date());
		}
		
		return new ModelAndView("index/index")
		.addObject("extensions", extensions)
		.addObject("endDate", endDate)
		.addObject("startDate", startDate)
		.addObject("url", url);
		
	}
	
	@RequestMapping("/getDatas.do")
	@ResponseBody
	public Map<String,Object> getDatas(String state,String url,String startDate,String endDate) throws IOException{
		try {
		
		List<Extension> extensions=null;
		List<Disable> disables=null;
		
		if(url==null||"".equals(url)){
			extensions=extensionMapper.findExtensions();
			disables=disableMapper.findDisables();
		}else{
			extensions=extensionMapper.findExtensionsByUrl(url);
			disables=disableMapper.findDisablesByUrl(url);
		}
		
		List<String>  arr=new ArrayList<String>();
		List<Integer> pvs=new ArrayList<Integer>();
		List<Integer> uvs=new ArrayList<Integer>();
		List<Integer> ips=new ArrayList<Integer>();
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date sDate=sdf.parse(startDate);
		Date eDate=sdf.parse(endDate);
	
		if(eDate.getTime()-sDate.getTime()==0){
			
			for (int i = 0; i < 24; i++) {
				Date date2=new Date(getHourTime(sDate.getTime(), i));
				long t5=date2.getTime();
				Integer pv=0;
				Integer uv=0;
				Integer ip=0;
				
				for (int j = 0; j < extensions.size(); j++) {
					Extension extension=extensions.get(j);
					Seo seo=hbaseMapper.getExtensionData(extension.getId(),t5);
					pv+=seo.getPvs();
					uv+=seo.getUvs();
					ip+=seo.getIps();
				}
				
				for (int j = 0; j < disables.size(); j++) {
					Disable disable=disables.get(j);
					if(disable.getState()==1){
						Seo seo=hbaseMapper.getDisableData(disable.getId(),t5);
						pv-=seo.getPvs();
						uv-=seo.getUvs();
						ip-=seo.getIps();
					}
					
				}
				
				String str=i+"点";
				pvs.add(pv);
				uvs.add(uv);
				ips.add(ip);
				arr.add(str); 
			}
		}else if(eDate.getTime()-sDate.getTime()==24*60*60*1000l){
			
			for (int i = 0; i < 24; i++) {
				Date date2=new Date(getHourTime(sDate.getTime(), i));
				long t5=date2.getTime();
				Integer pv=0;
				Integer uv=0;
				Integer ip=0;
				
				for (int j = 0; j < extensions.size(); j++) {
					Extension extension=extensions.get(j);
					Seo seo=hbaseMapper.getExtensionData(extension.getId(),t5);
					pv+=seo.getPvs();
					uv+=seo.getUvs();
					ip+=seo.getIps();
				}
				
				for (int j = 0; j < disables.size(); j++) {
					Disable disable=disables.get(j);
					if(disable.getState()==1){
						Seo seo=hbaseMapper.getDisableData(disable.getId(),t5);
						pv-=seo.getPvs();
						uv-=seo.getUvs();
						ip-=seo.getIps();
					}
				}
				
				String str=i+"点";
				pvs.add(pv);
				uvs.add(uv);
				ips.add(ip);
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
				
				Integer pv=0;
				Integer uv=0;
				Integer ip=0;
				
				for (int j = 0; j < extensions.size(); j++) {
					Extension extension=extensions.get(j);
					Seo seo=hbaseMapper.getExtensionDayData(extension.getId(),t5,t6);
					pv+=seo.getPvs();
					uv+=seo.getUvs();
					ip+=seo.getIps();
				}
				
				for (int j = 0; j < disables.size(); j++) {
					Disable disable=disables.get(j);
					if(disable.getState()==1){
						Seo seo=hbaseMapper.getDisableDayData(disable.getId(),t5,t6);
						pv-=seo.getPvs();
						uv-=seo.getUvs();
						ip-=seo.getIps();
					}
				}
				
				pvs.add(pv);
				uvs.add(uv);
				ips.add(ip);
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
				
				Integer pv=0;
				Integer uv=0;
				Integer ip=0;
				
				for (int j = 0; j < extensions.size(); j++) {
					Extension extension=extensions.get(j);
					Seo seo=hbaseMapper.getExtensionDayData(extension.getId(),t5,t6);
					pv+=seo.getPvs();
					uv+=seo.getUvs();
					ip+=seo.getIps();
				}
				
				for (int j = 0; j < disables.size(); j++) {
					Disable disable=disables.get(j);
					if(disable.getState()==1){
						Seo seo=hbaseMapper.getDisableDayData(disable.getId(),t5,t6);
						pv-=seo.getPvs();
						uv-=seo.getUvs();
						ip-=seo.getIps();
					}
				}
				
				pvs.add(pv);
				uvs.add(uv);
				ips.add(ip);
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
				
				Integer pv=0;
				Integer uv=0;
				Integer ip=0;
				
				for (int j = 0; j < extensions.size(); j++) {
					Extension extension=extensions.get(j);
					Seo seo=hbaseMapper.getExtensionDayData(extension.getId(),t5,t6);
					pv+=seo.getPvs();
					uv+=seo.getUvs();
					ip+=seo.getIps();
				}
				
				for (int j = 0; j < disables.size(); j++) {
					Disable disable=disables.get(j);
					if(disable.getState()==1){
						Seo seo=hbaseMapper.getDisableDayData(disable.getId(),t5,t6);
						pv-=seo.getPvs();
						uv-=seo.getUvs();
						ip-=seo.getIps();
					}
				}
				
				pvs.add(pv);
				uvs.add(uv);
				ips.add(ip);
				
				arr.add(str); 
			}
		}
		
		
		
		List<Integer> today=new ArrayList<Integer>();
		long t1=getStartTime(System.currentTimeMillis());
		long t2=getEndTime(System.currentTimeMillis());
		
		Integer today_pvs=0;
		Integer today_uvs=0;
		Integer today_ips=0;
		
		for (int i = 0; i < extensions.size(); i++) {
			Extension extension=extensions.get(i);
			Seo seo=hbaseMapper.getExtensionDayData(extension.getId(),t1,t2);
			today_pvs+=seo.getPvs();
			today_uvs+=seo.getUvs();
			today_ips+=seo.getIps();
		}
		
		for (int j = 0; j < disables.size(); j++) {
			Disable disable=disables.get(j);
			if(disable.getState()==1){
				Seo seo=hbaseMapper.getDisableDayData(disable.getId(),t1,t2);
				today_pvs-=seo.getPvs();
				today_uvs-=seo.getUvs();
				today_ips-=seo.getIps();
			}
		}
		
		today.add(today_pvs);
		today.add(today_uvs);
		today.add(today_ips);
		
		List<Integer> yesterday=new ArrayList<Integer>();
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.DATE, cal.get(Calendar.DATE)-1);
		long t3=getStartTime(cal.getTimeInMillis());
		long t4=getEndTime(cal.getTimeInMillis());
		
		Integer yesterday_pvs=0;
		Integer yesterday_uvs=0;
		Integer yesterday_ips=0;
		
		for (int i = 0; i < extensions.size(); i++) {
			Extension extension=extensions.get(i);
			Seo seo=hbaseMapper.getExtensionDayData(extension.getId(),t3,t4);
			yesterday_pvs+=seo.getPvs();
			yesterday_uvs+=seo.getUvs();
			yesterday_ips+=seo.getIps();
		}
		
		for (int j = 0; j < disables.size(); j++) {
			Disable disable=disables.get(j);
			if(disable.getState()==1){
				Seo seo=hbaseMapper.getDisableDayData(disable.getId(),t3,t4);
				yesterday_pvs-=seo.getPvs();
				yesterday_uvs-=seo.getUvs();
				yesterday_ips-=seo.getIps();
			}
		}
		
		yesterday.add(yesterday_pvs);
		yesterday.add(yesterday_uvs);
		yesterday.add(yesterday_ips);
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("pvs", pvs);
		map.put("uvs", uvs);
		map.put("ips", ips);
		map.put("arr", arr);
		map.put("today", today);
		map.put("yesterday", yesterday);
		
		return map;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
