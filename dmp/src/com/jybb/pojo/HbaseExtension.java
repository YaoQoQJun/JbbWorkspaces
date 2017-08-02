package com.jybb.pojo;

/**
 * Hbase推广链接统计实体类
 * @author Administrator
 *
 */
public class HbaseExtension {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 推荐链接ID
	 */
	private Integer extension_id;
	/**
	 * 推荐链接URL地址
	 */
	private String extension_link;
	/**
	 * 推荐链接PV
	 */
	private Integer pv;
	/**
	 * 推荐链接UV
	 */
	private Integer uv;
	/**
	 * 推荐链接IP
	 */
	private Integer ip;
	/**
	 * 每小时起始时间戳
	 */
	private Long start;
	/**
	 * 每小时结束时间戳
	 */
	private Long end;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getExtension_id() {
		return extension_id;
	}
	public void setExtension_id(Integer extension_id) {
		this.extension_id = extension_id;
	}
	public String getExtension_link() {
		return extension_link;
	}
	public void setExtension_link(String extension_link) {
		this.extension_link = extension_link;
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
