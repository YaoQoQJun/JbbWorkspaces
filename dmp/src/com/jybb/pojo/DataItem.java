package com.jybb.pojo;

/**
 * 数据项实体类
 * @author 姚俊
 *
 */
public class DataItem {
	
	/**
	 * 数据项ID
	 */
	private Integer id;
	/**
	 * 数据项名称
	 */
	private String data_item_name;
	/**
	 * 对应HBASE列名
	 */
	private String hbase_name;
	/**
	 * 异常范围
	 */
	private float exception_range;
	/**
	 * 数据来源
	 */
	private String data_source;
	/**
	 * 数据项类型  1-软件标准数据，2-硬件标准数据，3-行为标准数据
	 */
	private Integer state;
	
	public String getHbase_name() {
		return hbase_name;
	}
	public void setHbase_name(String hbase_name) {
		this.hbase_name = hbase_name;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getData_item_name() {
		return data_item_name;
	}
	public void setData_item_name(String data_item_name) {
		this.data_item_name = data_item_name;
	}
	public float getException_range() {
		return exception_range;
	}
	public void setException_range(float exception_range) {
		this.exception_range = exception_range;
	}
	public String getData_source() {
		return data_source;
	}
	public void setData_source(String data_source) {
		this.data_source = data_source;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "DataItem [id=" + id + ", data_item_name=" + data_item_name
				+ ", exception_range=" + exception_range + ", data_source="
				+ data_source + ", state=" + state + "]";
	}
	
	
}
