package com.jybb.pojo;

/**
 * 渠道实体类
 * @author 姚俊
 *
 */
public class AppEventSet {
	
	private Integer id;
	private String app_event_name;
	private String app_event_remark;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getApp_event_name() {
		return app_event_name;
	}
	public void setApp_event_name(String app_event_name) {
		this.app_event_name = app_event_name;
	}
	public String getApp_event_remark() {
		return app_event_remark;
	}
	public void setApp_event_remark(String app_event_remark) {
		this.app_event_remark = app_event_remark;
	}
	
}
