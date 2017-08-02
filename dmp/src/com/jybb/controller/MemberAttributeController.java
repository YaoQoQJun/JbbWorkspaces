package com.jybb.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.jybb.membersMapper.MemberMapper;
import com.jybb.pojo.Data;
import com.jybb.pojo.DataItem;
import com.jybb.pojo.Extension;
import com.jybb.util.PrivilegeUtil;

@Controller
@RequestMapping("/memberAttribute")
public class MemberAttributeController {

	private Logger logger = LoggerFactory
			.getLogger(MemberAttributeController.class);

	@Autowired
	private AdminMapper adminMapper;

	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private DataItemMapper dataItemMapper;

	
	/**
	 * 跳转到性别属性统计
	 */
	@RequestMapping("toSexAttribute.do")
	public ModelAndView toSexAttribute(String startDate, String endDate,String regFrom)
			throws IOException {

		DataItem di =dataItemMapper.getDataItemByDataItemId(90);

		JSONObject jsonObject1 = JSONObject.fromObject(di);
		
		if(regFrom==null||"".equals(regFrom)){
			regFrom="0";
		}

		return new ModelAndView("memberAttribute/sexAttribute")
			.addObject("startDate", startDate)
			.addObject("endDate", endDate)
			.addObject("regFrom", regFrom)
			.addObject("dataItem", jsonObject1);

	}
	
	/**
	 * 跳转到年龄属性统计
	 */
	@RequestMapping("toAgeAttribute.do")
	public ModelAndView toAgeAttribute(String startDate, String endDate,String regFrom)
			throws IOException {

		DataItem di =dataItemMapper.getDataItemByDataItemId(91);

		JSONObject jsonObject1 = JSONObject.fromObject(di);
		
		if(regFrom==null||"".equals(regFrom)){
			regFrom="0";
		}

		return new ModelAndView("memberAttribute/ageAttribute")
			.addObject("startDate", startDate)
			.addObject("endDate", endDate)
			.addObject("regFrom", regFrom)
			.addObject("dataItem", jsonObject1);

	}
	
	/**
	 * 跳转到状态属性统计
	 */
	@RequestMapping("toStateAttribute.do")
	public ModelAndView toStateAttribute(String startDate, String endDate,String regFrom)
			throws IOException {

		DataItem di =dataItemMapper.getDataItemByDataItemId(92);

		JSONObject jsonObject1 = JSONObject.fromObject(di);
		
		if(regFrom==null||"".equals(regFrom)){
			regFrom="0";
		}

		return new ModelAndView("memberAttribute/stateAttribute")
			.addObject("startDate", startDate)
			.addObject("endDate", endDate)
			.addObject("regFrom", regFrom)
			.addObject("dataItem", jsonObject1);

	}
	
	/**
	 * 跳转到地域属性统计
	 */
	@RequestMapping("toLocalAttribute.do")
	public ModelAndView toLocalAttribute(String startDate, String endDate,String regFrom)
			throws IOException {

		DataItem di =dataItemMapper.getDataItemByDataItemId(93);

		JSONObject jsonObject1 = JSONObject.fromObject(di);
		
		if(regFrom==null||"".equals(regFrom)){
			regFrom="0";
		}

		return new ModelAndView("memberAttribute/localAttribute")
			.addObject("startDate", startDate)
			.addObject("endDate", endDate)
			.addObject("regFrom", regFrom)
			.addObject("dataItem", jsonObject1);

	}

	@RequestMapping("/getSexDatas.do")
	@ResponseBody
	public Map<String, Object> getSexDatas(Integer dataItem_id, String startDate,String endDate,String regFrom)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date1=null;
		Date date2=null;
		if(startDate==null||"".equals(startDate)){
			date1=new Date();
		}else{
			date1=sdf.parse(startDate);
		}
		
		if(endDate==null||"".equals(endDate)){
			date2=new Date();
		}else{
			date2=sdf.parse(endDate);
		}
		
		long t1 = getStartTime(date1.getTime())/1000;
		long t2 = getEndTime(date2.getTime())/1000;

		List<Data> datas1 = new ArrayList<Data>();// 标准数据
		datas1 = dataItemMapper.getDatasByDataItemId(dataItem_id);

		List<Data> datas2 = new ArrayList<Data>();// 真实数据
		List<Integer> counts = new ArrayList<Integer>();// 异常数据总数

		int count = 0;
		DataItem di = dataItemMapper.getDataItemByDataItemId(dataItem_id);// 当前数据项

		// 循环该数据项下面的数据
		float range = di.getException_range();
		
		List<Map<String, Object>> maps = memberMapper.getSexData(t1, t2,regFrom.trim());
		int a = 0;

		for (int i = 0; i < maps.size(); i++) {
			Map<String, Object> map = maps.get(i);
			a += Integer.parseInt(map.get("count").toString());
		}
		
		for (int i = 0; i < datas1.size(); i++) {
			Data data = datas1.get(i);
			Data d1 = new Data();
			d1.setData_name(data.getData_name());
			Integer dataName =null;
			if("男".equals(data.getData_name())){
				dataName=1;
			}else if("女".equals(data.getData_name())){
				dataName=-1;
			}else{
				dataName=0;
			}
			
			int x = 0;
			for (int j = 0; j < maps.size(); j++) {
				Map<String, Object> map = maps.get(j);
				if (dataName==Integer.parseInt(map.get("sex").toString())) {
					x += Integer.parseInt(map.get("count").toString());
					break;
				}
			}
			float f = 0;
			if (a > 0) {
				f = (((float) x) / (a)) * 100;
			}

			BigDecimal b = new BigDecimal(f);
			float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			d1.setProportion(f1);
			float c = Math.abs(f - data.getProportion());
			if (c < 0 || c > range) {
				count++;
				d1.setData_item_id(1);
			} else {
				d1.setData_item_id(0);
			}
			datas2.add(d1);

		}

		counts.add(count);

		JSONArray jsonArray2 = JSONArray.fromObject(datas1);

		JSONArray jsonArray3 = JSONArray.fromObject(datas2);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("datas1", jsonArray2);
		map.put("datas2", jsonArray3);
		map.put("counts", counts);

		return map;
	}
	
	
	@RequestMapping("/getAgeDatas.do")
	@ResponseBody
	public Map<String, Object> getAgeDatas(Integer dataItem_id, String startDate,String endDate,String regFrom)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date1=null;
		Date date2=null;
		if(startDate==null||"".equals(startDate)){
			date1=new Date();
		}else{
			date1=sdf.parse(startDate);
		}
		
		if(endDate==null||"".equals(endDate)){
			date2=new Date();
		}else{
			date2=sdf.parse(endDate);
		}
		
		long t1 = getStartTime(date1.getTime())/1000;
		long t2 = getEndTime(date2.getTime())/1000;

		List<Data> datas1 = new ArrayList<Data>();// 标准数据
		datas1 = dataItemMapper.getDatasByDataItemId(dataItem_id);

		List<Data> datas2 = new ArrayList<Data>();// 真实数据
		List<Integer> counts = new ArrayList<Integer>();// 异常数据总数

		int count = 0;
		DataItem di = dataItemMapper.getDataItemByDataItemId(dataItem_id);// 当前数据项

		// 循环该数据项下面的数据
		float range = di.getException_range();
		List<Integer> ages = memberMapper.getAgeData(t1, t2,regFrom);
		int a = ages.size();

		Integer year=new Date().getYear()+1900;
		
		Integer count_age1=0;//20岁以下
		Integer count_age2=0;//20-25岁
		Integer count_age3=0;//25-30岁
		Integer count_age4=0;//30-35岁
		Integer count_age5=0;//35-40岁
		Integer count_age6=0;//40岁以上
		Integer count_age7=0;//未填写
		for (int i = 0; i < ages.size(); i++) {
			Integer age=ages.get(i);
			if(age==null||year-age==0){
				count_age7++;
				continue;
			}
			
			if(age<20){
				count_age1++;
			}else if(age>=20&&age<25){
				count_age2++;
			}else if(age>=25&&age<30){
				count_age3++;
			}else if(age>=30&&age<35){
				count_age4++;
			}else if(age>=35&&age<40){
				count_age5++;
			}else{
				count_age6++;
			}
			
		}
		
		for (int i = 0; i < datas1.size(); i++) {
			Data data = datas1.get(i);
			Data d1 = new Data();
			d1.setData_name(data.getData_name());
			String date_name=data.getData_name();
			int x = 0;
			if(date_name.equals("20岁以下")){
				x=count_age1;
			}else if(date_name.equals("20-25岁")){
				x=count_age2;
			}else if(date_name.equals("25-30岁")){
				x=count_age3;
			}else if(date_name.equals("30-35岁")){
				x=count_age4;
			}else if(date_name.equals("35-40岁")){
				x=count_age5;
			}else if(date_name.equals("40岁以上")){
				x=count_age6;
			}else if(date_name.equals("未填写")){
				x=count_age7;
			}
			
			float f = 0;
			if (a > 0) {
				f = (((float) x) / (a)) * 100;
			}

			BigDecimal b = new BigDecimal(f);
			float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			d1.setProportion(f1);
			float c = Math.abs(f - data.getProportion());
			if (c < 0 || c > range) {
				count++;
				d1.setData_item_id(1);
			} else {
				d1.setData_item_id(0);
			}
			datas2.add(d1);

		}

		counts.add(count);

		JSONArray jsonArray2 = JSONArray.fromObject(datas1);

		JSONArray jsonArray3 = JSONArray.fromObject(datas2);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("datas1", jsonArray2);
		map.put("datas2", jsonArray3);
		map.put("counts", counts);

		return map;
	}
	
	
	@RequestMapping("/getLocalDatas.do")
	@ResponseBody
	public Map<String, Object> getLocalDatas(Integer dataItem_id, String startDate,String endDate,String regFrom)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date1=null;
		Date date2=null;
		if(startDate==null||"".equals(startDate)){
			date1=new Date();
		}else{
			date1=sdf.parse(startDate);
		}
		
		if(endDate==null||"".equals(endDate)){
			date2=new Date();
		}else{
			date2=sdf.parse(endDate);
		}
		
		long t1 = getStartTime(date1.getTime())/1000;
		long t2 = getEndTime(date2.getTime())/1000;

		List<Data> datas1 = new ArrayList<Data>();// 标准数据
		datas1 = dataItemMapper.getDatasByDataItemId(dataItem_id);

		List<Data> datas2 = new ArrayList<Data>();// 真实数据
		List<Integer> counts = new ArrayList<Integer>();// 异常数据总数

		int count = 0;
		DataItem di = dataItemMapper.getDataItemByDataItemId(dataItem_id);// 当前数据项

		// 循环该数据项下面的数据
		float range = di.getException_range();
		
		
		List<Map<String, Object>> maps = memberMapper.getLocalData(t1, t2,regFrom.trim());
		
		int a=0;
		
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
				Pattern pattern5 = Pattern.compile("^"+data.getData_name()+".*$");
				Matcher matcher5 = pattern5.matcher(dataName);
				if(matcher5.matches()){
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

		counts.add(count);

		JSONArray jsonArray2 = JSONArray.fromObject(datas1);

		JSONArray jsonArray3 = JSONArray.fromObject(datas2);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("datas1", jsonArray2);
		map.put("datas2", jsonArray3);
		map.put("counts", counts);
		map.put("cRegist", a);

		return map;
	}
	
	@RequestMapping("/getStateDatas.do")
	@ResponseBody
	public Map<String, Object> getStateDatas(Integer dataItem_id, String startDate,String endDate,String regFrom)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date1=null;
		Date date2=null;
		if(startDate==null||"".equals(startDate)){
			date1=new Date();
		}else{
			date1=sdf.parse(startDate);
		}
		
		if(endDate==null||"".equals(endDate)){
			date2=new Date();
		}else{
			date2=sdf.parse(endDate);
		}
		
		long t1 = getStartTime(date1.getTime())/1000;
		long t2 = getEndTime(date2.getTime())/1000;

		List<Data> datas1 = new ArrayList<Data>();// 标准数据
		datas1 = dataItemMapper.getDatasByDataItemId(dataItem_id);

		List<Data> datas2 = new ArrayList<Data>();// 真实数据
		List<Integer> counts = new ArrayList<Integer>();// 异常数据总数

		int count = 0;
		DataItem di = dataItemMapper.getDataItemByDataItemId(dataItem_id);// 当前数据项

		// 循环该数据项下面的数据
		float range = di.getException_range();
		
		
		List<Map<String, Object>> maps = memberMapper.getStateData(t1, t2,regFrom.trim());
		
		int a=0;
		
		for (int i = 0; i < maps.size(); i++) {
			Map<String,Object> map=maps.get(i);
	    	a+=Integer.parseInt(map.get("count").toString());
		}	
		for (int i = 0; i <datas1.size(); i++) {
			Data data=datas1.get(i);
			Data d1=new Data();
			d1.setData_name(data.getData_name());
			Integer flag=0;
			
			if("未填写".equals(data.getData_name())){
				flag=0;
			}else if("备孕中".equals(data.getData_name())){
				flag=1;
			}else if("怀孕中".equals(data.getData_name())){
				flag=2;
			}else{
				flag=3;
			}
			
			int x=0;
			for (int j = 0; j < maps.size(); j++) {
				Map<String,Object> map=maps.get(j);
				
				if (flag==Integer.parseInt(map.get("state").toString())) {
					x += Integer.parseInt(map.get("count").toString());
					break;
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

		counts.add(count);

		JSONArray jsonArray2 = JSONArray.fromObject(datas1);

		JSONArray jsonArray3 = JSONArray.fromObject(datas2);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("datas1", jsonArray2);
		map.put("datas2", jsonArray3);
		map.put("counts", counts);

		return map;
	}
	
	
	@RequestMapping("/getPregnancyDatas.do")
	@ResponseBody
	public Map<String, Object> getPregnancyDatas(Integer dataItem_id, String startDate,String endDate,String regFrom)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date1=null;
		Date date2=null;
		if(startDate==null||"".equals(startDate)){
			date1=new Date();
		}else{
			date1=sdf.parse(startDate);
		}
		
		if(endDate==null||"".equals(endDate)){
			date2=new Date();
		}else{
			date2=sdf.parse(endDate);
		}
		
		long t1 = getStartTime(date1.getTime())/1000;
		long t2 = getEndTime(date2.getTime())/1000;
		
		
		List<String> lists = memberMapper.getPregnancyDatas(t1, t2,regFrom.trim());
		List<Data> datas2 = new ArrayList<Data>();// 真实数据

		int a=0;
		int b=0;
		int c=0;
		
		for (int i = 0; i < lists.size(); i++) {
			
			String strDate=lists.get(i);
			if("".equals(strDate)||"0".equals(strDate)||null==startDate){
				continue;
			}
			Date d1=sdf.parse(strDate);
			Date d2=new Date();
			long l1=d1.getTime();
			long l2=d2.getTime();
			
			int flag=(int)((l1-l2)/1000/60/60/24/7);
			
			if(flag>=0&&flag<=13){//孕晚期
				a++;
			}else if(flag>13&&flag<=27){//孕中期
				b++;
			}else if(flag>28&&flag<=40){//孕早期
				c++;
			}
			
		}
		
		int d=a+b+c;
		
		if(a!=0){
			float f=0; 
			f=(((float)a)/(d))*100;
			BigDecimal bigDecimal=new BigDecimal(f);  
			float f1=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			Data data1=new Data();
			data1.setData_name("孕晚期");
			data1.setProportion(f1);
			datas2.add(data1);
		}else{
			Data data1=new Data();
			data1.setData_name("孕晚期");
			data1.setProportion(0.0f);
			datas2.add(data1);
		}
		
		if(b!=0){
			float f=0; 
			f=(((float)b)/(d))*100;
			BigDecimal bigDecimal=new BigDecimal(f);  
			float f1=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			Data data1=new Data();
			data1=new Data();
			data1.setData_name("孕中期");
			data1.setProportion(f1);
			datas2.add(data1);
		}else{
			Data data1=new Data();
			data1.setData_name("孕中期");
			data1.setProportion(0.0f);
			datas2.add(data1);
		}
		
		if(c!=0){
			float f=0; 
			f=(((float)c)/(d))*100;
			BigDecimal bigDecimal=new BigDecimal(f);  
			float f1=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			Data data1=new Data();
			data1=new Data();
			data1.setData_name("孕早期");
			data1.setProportion(f1);
			datas2.add(data1);
		}else{
			Data data1=new Data();
			data1.setData_name("孕早期");
			data1.setProportion(0.0f);
			datas2.add(data1);
		}
		
		
		JSONArray jsonArray3 = JSONArray.fromObject(datas2);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("datas2", jsonArray3);

		return map;
	}
	
	
	@RequestMapping("/getBabyAgeDatas.do")
	@ResponseBody
	public Map<String, Object> getBabyAgeDatas(Integer dataItem_id, String startDate,String endDate,String regFrom)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date1=null;
		Date date2=null;
		if(startDate==null||"".equals(startDate)){
			date1=new Date();
		}else{
			date1=sdf.parse(startDate);
		}
		
		if(endDate==null||"".equals(endDate)){
			date2=new Date();
		}else{
			date2=sdf.parse(endDate);
		}
		
		long t1 = getStartTime(date1.getTime())/1000;
		long t2 = getEndTime(date2.getTime())/1000;
		
		
		List<String> lists = memberMapper.getBabyAgeDatas(t1, t2,regFrom.trim());
		List<Data> datas2 = new ArrayList<Data>();// 真实数据

		int a=0;
		int b=0;
		int c=0;
		int d=0;
		
		for (int i = 0; i < lists.size(); i++) {
			
			String strDate=lists.get(i);
			if("".equals(strDate)||"0".equals(strDate)||null==startDate){
				continue;
			}
			Date d1=sdf.parse(strDate);
			Date d2=new Date();
			long l1=d1.getTime();
			long l2=d2.getTime();
			
			int flag=(int)((l2-l1)/1000/60/60/24/356);
			
			if(flag>=0&&flag<=1){//0-1岁
				a++;
			}else if(flag>1&&flag<=3){//1-3岁
				b++;
			}else if(flag>3&&flag<=6){//3-6岁
				c++;
			}else if(flag>6){//6岁以上
				d++;
			}
			
		}
		
		int e=a+b+c;
		
		if(a!=0){
			float f=0; 
			f=(((float)a)/(e))*100;
			BigDecimal bigDecimal=new BigDecimal(f);  
			float f1=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			Data data1=new Data();
			data1.setData_name("0-1岁");
			data1.setProportion(f1);
			datas2.add(data1);
		}else{
			Data data1=new Data();
			data1.setData_name("0-1岁");
			data1.setProportion(0.0f);
			datas2.add(data1);
		}
		
		if(b!=0){
			float f=0; 
			f=(((float)b)/(e))*100;
			BigDecimal bigDecimal=new BigDecimal(f);  
			float f1=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			Data data1=new Data();
			data1=new Data();
			data1.setData_name("1-3岁");
			data1.setProportion(f1);
			datas2.add(data1);
		}else{
			Data data1=new Data();
			data1.setData_name("1-3岁");
			data1.setProportion(0.0f);
			datas2.add(data1);
		}
		
		if(c!=0){
			float f=0; 
			f=(((float)c)/(e))*100;
			BigDecimal bigDecimal=new BigDecimal(f);  
			float f1=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			Data data1=new Data();
			data1=new Data();
			data1.setData_name("3-6岁");
			data1.setProportion(f1);
			datas2.add(data1);
		}else{
			Data data1=new Data();
			data1.setData_name("3-6岁");
			data1.setProportion(0.0f);
			datas2.add(data1);
		}
		
		if(d!=0){
			float f=0; 
			f=(((float)d)/(e))*100;
			BigDecimal bigDecimal=new BigDecimal(f);  
			float f1=bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
			Data data1=new Data();
			data1=new Data();
			data1.setData_name("6岁以上");
			data1.setProportion(f1);
			datas2.add(data1);
		}else{
			Data data1=new Data();
			data1.setData_name("6岁以上");
			data1.setProportion(0.0f);
			datas2.add(data1);
		}
		
		
		JSONArray jsonArray3 = JSONArray.fromObject(datas2);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("datas2", jsonArray3);

		return map;
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

}
