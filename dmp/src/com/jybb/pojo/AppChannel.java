package com.jybb.pojo;

/**
 * 渠道实体类
 * @author 姚俊
 *
 */
public class AppChannel {
	
	/**
	 * 渠道ID
	 */
	private Integer id;
	/**
	 * 渠道名称
	 */
	private String app_channel_name;
	
	public String getApp_channel_name() {
		return app_channel_name;
	}
	public void setApp_channel_name(String app_channel_name) {
		this.app_channel_name = app_channel_name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
