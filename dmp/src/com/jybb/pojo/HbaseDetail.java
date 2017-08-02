package com.jybb.pojo;

/**
 * Hbase统计详细信息
 * @author 姚俊
 *
 */
public class HbaseDetail {
	
	/**
	 * 链接URL地址
	 */
	private String url;
	/**
	 * PV总数
	 */
	private Integer count_pv;
	/**
	 * UV总数
	 */
	private Integer count_uv;
	/**
	 * IP总数
	 */
	private Integer count_ip;
	/**
	 * 老访客占比
	 */
	private Double count_ov;
	/**
	 * 平均访问时间
	 */
	private Integer count_at;
	/**
	 * 跳出率
	 */
	private Double count_br;
	/**
	 * PV输出总数
	 */
	private Integer count_op;
	/**
	 * 整点时间戳
	 */
	private long time;
	
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
	public Integer getCount_at() {
		return count_at;
	}
	public void setCount_at(Integer count_at) {
		this.count_at = count_at;
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
