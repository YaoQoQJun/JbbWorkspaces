package com.jybb.pojo;

public class HbaseDisable {
	
	private Integer id;
	private Integer disable_id;
	private String disable_link;
	private Integer pv;
	private Integer uv;
	private Integer ip;
	private Long start;
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
