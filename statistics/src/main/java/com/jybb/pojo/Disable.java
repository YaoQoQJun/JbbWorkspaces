package com.jybb.pojo;

import java.sql.Timestamp;

public class Disable {
	
	private Integer id;
	private Integer website_id;
	private String channel_id;
	private String channel_name;
	private String disable_link;
	private Integer state;
	private Timestamp add_time;
	private Timestamp enable_time;
	private Timestamp disable_time;
	
	private Website website;
	
	
	public String getDisable_link() {
		return disable_link;
	}
	public void setDisable_link(String disable_link) {
		this.disable_link = disable_link;
	}
	public Website getWebsite() {
		return website;
	}
	public void setWebsite(Website website) {
		this.website = website;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWebsite_id() {
		return website_id;
	}
	public void setWebsite_id(Integer website_id) {
		this.website_id = website_id;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Timestamp getAdd_time() {
		return add_time;
	}
	public void setAdd_time(Timestamp add_time) {
		this.add_time = add_time;
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
