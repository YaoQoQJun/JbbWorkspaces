package com.jybb.pojo;

/**
 * 渠道实体类
 * @author 姚俊
 *
 */
public class AppVersion {
	
	/**
	 * 渠道ID
	 */
	private Integer id;
	/**
	 * 渠道名称
	 */
	private String app_version_name;
	
	public String getApp_version_name() {
		return app_version_name;
	}
	public void setApp_version_name(String app_version_name) {
		this.app_version_name = app_version_name;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
