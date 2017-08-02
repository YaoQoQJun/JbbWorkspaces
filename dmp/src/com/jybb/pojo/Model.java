package com.jybb.pojo;

/**
 * 模块实体类
 * @author 姚俊
 *
 */
public class Model {
	
	/**
	 * 模块ID
	 */
	private Integer id;
	/**
	 * 模块名
	 */
	private String model_name;
	/**
	 * 模块地址
	 */
	private String model_url;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public String getModel_url() {
		return model_url;
	}
	public void setModel_url(String model_url) {
		this.model_url = model_url;
	}
	
}
