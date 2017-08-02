package com.jybb.pojo;

/**
 * Hbase禁用链接统计实体类
 * @author Administrator
 *
 */
public class HbaseDisable {
	
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 禁用链接ID
	 */
	private Integer disable_id;
	/**
	 * 禁用链接URL
	 */
	private String disable_link;
	/**
	 * pv数量
	 */
	private Integer pv;
	/**
	 * uv数量
	 */
	private Integer uv;
	/**
	 * ip数量
	 */
	private Integer ip;
	/**
	 * 每小时起始时间
	 */
	private Long start;
	/**
	 * 每小时结束时间
	 */
	private Long end;
	
	
	public Integer getDisable_id() {
		return disable_id;
	}
	public void setDisable_id(Integer disable_id) {
		this.disable_id = disable_id;
	}
	public String getDisable_link() {
		return disable_link;
	}
	public void setDisable_link(String disable_link) {
		this.disable_link = disable_link;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPv() {
		return pv;
	}
	public void setPv(Integer pv) {
		this.pv = pv;
	}
	public Integer getUv() {
		return uv;
	}
	public void setUv(Integer uv) {
		this.uv = uv;
	}
	public Integer getIp() {
		return ip;
	}
	public void setIp(Integer ip) {
		this.ip = ip;
	}
	public Long getStart() {
		return start;
	}
	public void setStart(Long start) {
		this.start = start;
	}
	public Long getEnd() {
		return end;
	}
	public void setEnd(Long end) {
		this.end = end;
	}
	
}
