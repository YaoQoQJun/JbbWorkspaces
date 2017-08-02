package com.jybb.tag;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;


public class PromotionDaysTag extends SimpleTagSupport{
	
	private String date;
	
	public void doTag() throws JspException, IOException {
		PageContext pc=(PageContext)this.getJspContext();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		int a=0;
		try {
			Date d1=sdf.parse(date);
			Date d2=new Date();
			long time=d2.getTime()-d1.getTime();
			a=(int)(time/1000/60/60/24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		pc.getOut().write(""+a);
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
