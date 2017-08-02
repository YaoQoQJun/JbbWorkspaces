package com.jybb.pojo;

/**
 * 渠道实体类
 * @author 姚俊
 *
 */
public class Channel {
	
	/**
	 * 渠道ID
	 */
	private Integer id;
	/**
	 * 渠道编号
	 */
	private String channel_id;
	/**
	 * 渠道名称
	 */
	private String channel_name;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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

	
}
