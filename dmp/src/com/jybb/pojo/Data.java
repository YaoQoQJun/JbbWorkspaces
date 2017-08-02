package com.jybb.pojo;

/**
 * 数据实体类
 * @author 姚俊
 *
 */
public class Data{
	
	/**
	 * 数据ID
	 */
	private Integer id;
	/**
	 * 数据名
	 */
	private String data_name;
	/**
	 * 数据占比
	 */
	private Float proportion;
	/**
	 * 数据项ID
	 */
	private Integer data_item_id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getData_name() {
		return data_name;
	}
	public void setData_name(String data_name) {
		this.data_name = data_name;
	}
	public Float getProportion() {
		return proportion;
	}
	public void setProportion(Float proportion) {
		this.proportion = proportion;
	}
	public Integer getData_item_id() {
		return data_item_id;
	}
	public void setData_item_id(Integer data_item_id) {
		this.data_item_id = data_item_id;
	}
	@Override
	public String toString() {
		return "Data [id=" + id + ", data_name=" + data_name + ", proportion="
				+ proportion + ", data_item_id=" + data_item_id + "]";
	}
}
