package com.jybb.mapper;



import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Admin;


@MybatisRepository
public interface AdminMapper{

	Admin findAdmin(@Param("username")String username,@Param("password")String password)throws DataAccessException;

	List<Map<String,String>> getPrivileges(@Param("admin_id")Integer admin_id)throws DataAccessException;

	List<Admin> findAll(
			@Param("start")int start, 
			@Param("number")int number)throws DataAccessException;

	Integer countAdmin()throws DataAccessException;

	void addAdmin(Admin admin)throws DataAccessException;

	void deleteAdmin(Integer id)throws DataAccessException;

	void updateAdmin(Admin admin)throws DataAccessException;

	void addAdminRole(@Param("admin_id")Integer admin_id, @Param("role_id")Integer role_id)throws DataAccessException;

	void deleteAdminRoleByAdminId(Integer admin_id)throws DataAccessException;

	void deleteAdminRoleByRoleId(Integer role_id)throws DataAccessException;

	Admin findAdminByUsername(
			@Param("username")String username,
			@Param("admin_id")Integer admin_id)throws DataAccessException;
	
}
