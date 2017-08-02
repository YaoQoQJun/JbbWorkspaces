package com.jybb.pojo;

/**
 * 角色实体类
 * @author 姚俊
 *
 */
public class Role {
	
	/**
	 * 角色ID
	 */
	private Integer id;
	/**
	 * 角色名
	 */
	private String role_name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	@Override
	public String toString() {
		return "Role [id=" + id + ", role_name=" + role_name + "]";
	}
	
}
