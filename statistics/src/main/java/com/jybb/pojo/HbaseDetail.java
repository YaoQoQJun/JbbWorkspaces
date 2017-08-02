package com.jybb.pojo;

public class HbaseDetail {
	
	private String url;
	private Integer count_pv;
	private Integer count_uv;
	private Integer count_ip;
	private Double  count_ov;
	private String count_at;
	private Double count_br;
	private Integer count_op;
	
	private long time;
	
	
	public String getCount_at() {
		return count_at;
	}
	public void setCount_at(String count_at) {
		this.count_at = count_at;
	}
	public Integer getCount_op() {
		return count_op;
	}
	public void setCount_op(Integer count_op) {
		this.count_op = count_op;
	}
	public Double getCount_br() {
		return count_br;
	}
	public void setCount_br(Double count_br) {
		this.count_br = count_br;
	}
	public Double getCount_ov() {
		return count_ov;
	}
	public void setCount_ov(Double count_ov) {
		this.count_ov = count_ov;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getCount_pv() {
		return count_pv;
	}
	public void setCount_pv(Integer count_pv) {
		this.count_pv = count_pv;
	}
	public Integer getCount_uv() {
		return count_uv;
	}
	public void setCount_uv(Integer count_uv) {
		this.count_uv = count_uv;
	}
	public Integer getCount_ip() {
		return count_ip;
	}
	public void setCount_ip(Integer count_ip) {
		this.count_ip = count_ip;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
