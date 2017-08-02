package com.jybb.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.data.hadoop.hbase.ResultsExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jybb.pojo.Point;

@Controller
@RequestMapping("/points")
public class PointsController {
	
	private Logger logger=LoggerFactory.getLogger(PointsController.class);
	
	@Autowired
	private HbaseTemplate htemplate;
	
	@ResponseBody
	@RequestMapping("/getPoints.do")
	public Object getPointDatas(HttpServletRequest request,String url,String date,HttpServletResponse response){
		url=url.replaceAll("/%26/g", "&");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if(date==null||"".equals(date)){
			date=sdf.format(new Date());
		}
		
		final List<Point> points=new ArrayList<Point>();
		try {
			
			Date d1= sdf.parse(date);
			Scan scan=new Scan();
			
//			Date d2=new Date();
//			if(sdf.format(d2).equals(date)){
//				scan.setTimeRange(getStartTime(d1.getTime()),getHourTime(d2.getTime(), d2.getHours()));
//			}else{
//				scan.setTimeRange(getStartTime(d1.getTime()),getEndTime(d1.getTime()));
//			}
			
			scan.setTimeRange(getStartTime(d1.getTime()),getEndTime(d1.getTime()));
			
			FilterList filterList1=new FilterList(FilterList.Operator.MUST_PASS_ALL);
			
			SingleColumnValueExcludeFilter sf1=new SingleColumnValueExcludeFilter(
					"info".getBytes(),
					"url".getBytes(), 
					CompareOp.EQUAL,url.getBytes());
			
			sf1.setFilterIfMissing(true);
			
			filterList1.addFilter(sf1);
			
			scan.setFilter(filterList1);
			htemplate.find("points", scan, new ResultsExtractor<Boolean>() {
				public Boolean extractData(ResultScanner rs) throws Exception {
					for (Result result : rs) {
						for (KeyValue kv : result.raw()) {
							if("points".equals(new String(kv.getQualifier()))){
								Point p=new Point();
								p.setPoints(new String(kv.getValue()));
								points.add(p);
							}
						}
					}
					return true;
				}
			});
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
		return points;
	}
	
	private static long getStartTime(Long time){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 0); 
		cal.set(Calendar.SECOND, 0); 
		cal.set(Calendar.MINUTE, 0); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTimeInMillis();
	}
		
	private static long getEndTime(Long time){
		Calendar cal = Calendar.getInstance(); 
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, 23); 
		cal.set(Calendar.SECOND, 59); 
		cal.set(Calendar.MINUTE, 59); 
		cal.set(Calendar.MILLISECOND, 0); 
		return cal.getTimeInMillis();
	}
	// 当日小时时间
	private static long getHourTime(Long time, Integer Hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, Hour);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime().getTime();
	}
}
