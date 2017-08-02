package com.jybb.pojo;

import java.sql.Timestamp;

/**
 * 推广链接实体类
 * @author 姚俊
 *
 */
public class Extension {
	
	/**
	 * 推广链接ID
	 */
	private Integer id;
	/**
	 * 网站ID
	 */
	private Integer website_id;
	/**
	 * 渠道编号
	 */
	private String channel_id;
	/**
	 * 渠道名称
	 */
	private String channel_name;
	/**
	 * 推广链接
	 */
	private String extension_link;
	/**
	 * 开启状态  0-关闭，1-开启
	 */
	private Integer state;
	/**
	 * 添加时间
	 */
	private Timestamp add_time;
	/**
	 * 开启时间
	 */
	private Timestamp enable_time;
	/**
	 * 关闭时间
	 */
	private Timestamp disable_time;
	/**
	 * 网站实体类
	 */
	private Website website;
	
	
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
	public String getExtension_link() {
		return extension_link;
	}
	public void setExtension_link(String extension_link) {
		this.extension_link = extension_link;
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
