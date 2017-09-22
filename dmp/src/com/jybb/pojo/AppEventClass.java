package com.jybb.pojo;

/**
 * 渠道实体类
 * @author 姚俊
 *
 */
public class AppEventClass {
	
	/**
	 * 分类ID
	 */
	private Integer id;
	/**
	 * 分类名称
	 */
	private String app_event_class_name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getApp_event_class_name() {
		return app_event_class_name;
	}
	public void setApp_event_class_name(String app_event_class_name) {
		this.app_event_class_name = app_event_class_name;
	}
	
}
