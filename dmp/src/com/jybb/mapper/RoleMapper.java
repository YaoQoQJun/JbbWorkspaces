package com.jybb.mapper;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Model;
import com.jybb.pojo.Role;


@MybatisRepository
public interface RoleMapper{


	List<Role> findAll(
			@Param("start")int start, 
			@Param("number")int number)throws DataAccessException;

	Integer countRole()throws DataAccessException;

	List<Model> getModels()throws DataAccessException;

	List<String> getPrivilegeNames(Integer model_id)throws DataAccessException;

	void addRole(Role role)throws DataAccessException;

	Integer getPrivilegeModelId(
			@Param("model_name")String model_name, 
			@Param("privilege_name")String privilege_name)throws DataAccessException;

	void addRolePrivilegeModel(
			@Param("role_id")Integer role_id,
			@Param("privilege_model_id")Integer privilege_model_id)throws DataAccessException;

	List<Model> getModelsByRoleId(Integer role_id)throws DataAccessException;

	List<String> getPrivilegeNamesByRoleId(
			@Param("model_id")Integer model_id, 
			@Param("role_id")Integer role_id)throws DataAccessException;

	void deleteRolePrivilegeModelByRoleId(Integer role_id)throws DataAccessException;

	void updateRole(
			@Param("role_id")Integer role_id, 
			@Param("role_name")String role_name)throws DataAccessException;

	void deleteRoleById(Integer role_id)throws DataAccessException;

	List<Role> getRoles()throws DataAccessException;

	List<Role> getRolesByAdminId(Integer admin_id)throws DataAccessException;

	Role findRoleByRoleName(
			@Param("role_name")String role_name, 
			@Param("role_id")Integer role_id)throws DataAccessException;
	
}
