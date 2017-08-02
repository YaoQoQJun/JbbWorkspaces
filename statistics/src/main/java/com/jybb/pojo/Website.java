package com.jybb.pojo;

import java.sql.Timestamp;

public class Website {
	
	private Integer id;
	private String name;
	private String domain_name;
	private Timestamp add_time;
	private String code;
	private Integer state;
	private Timestamp enable_time;
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
