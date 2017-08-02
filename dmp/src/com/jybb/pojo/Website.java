package com.jybb.pojo;

import java.sql.Timestamp;

/**
 * 网站实体类
 * @author 姚俊
 *
 */
public class Website {
	
	/**
	 * 网站ID
	 */
	private Integer id;
	/**
	 * 网站名
	 */
	private String name;
	/**
	 * 域名
	 */
	private String domain_name;
	/**
	 * 添加时间
	 */
	private Timestamp add_time;
	/**
	 * 统计代码
	 */
	private String code;
	/**
	 * 开启状态  0-关闭，1-开启
	 */
	private Integer state;
	/**
	 * 开启时间
	 */
	private Timestamp enable_time;
	/**
	 * 关闭时间
	 */
	private Timestamp disable_time;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDomain_name() {
		return domain_name;
	}
	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}
	public Timestamp getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Timestamp add_time) {
		this.add_time = add_time;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Timestamp getEnable_time() {
		return enable_time;
	}
	public void setEnable_time(Timestamp enable_time) {
		this.enable_time = enable_time;
	}
	public Timestamp getDisable_time() {
		return disable_time;
	}
	public void setDisable_time(Timestamp disable_time) {
		this.disable_time = disable_time;
	}
	
}
