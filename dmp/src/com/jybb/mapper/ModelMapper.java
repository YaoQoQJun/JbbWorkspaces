package com.jybb.mapper;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Model;


@MybatisRepository
public interface ModelMapper{


	List<Model> findAll(
			@Param("start")int start, 
			@Param("number")int number)throws DataAccessException;

	Integer countModel()throws DataAccessException;

	void addModel(Model model)throws DataAccessException;

	void addModelPrivilege(
			@Param("model_id")Integer model_id, 
			@Param("privilege_id")int privilege_id)throws DataAccessException;
	
	List<String> getPrivileges(Integer model_id)throws DataAccessException;

	void updateModel(Model model)throws DataAccessException;

	void deleteModelPrivilege(Integer model_id)throws DataAccessException;

	void deleteModel(Integer model_id)throws DataAccessException;

	List<Integer> getPrivilegeModelIds(Integer model_id)throws DataAccessException;

	void deleteRolePrivilegeModelByPrivilegeModelIds(Integer privilege_model_id)throws DataAccessException;

	Integer findPrivilegeIdByPrivilegeName(String privilege_name)throws DataAccessException;

	Integer getPrivilegeModelIdByModelIdAndPrivilegeId(
			@Param("model_id")Integer model_id, 
			@Param("privilege_id")Integer privilege_id)throws DataAccessException;

	void deleteModelPrivilegeByModelIdAndPrivilegeId(
			@Param("model_id")Integer model_id,
			@Param("privilege_id")Integer privilege_id)throws DataAccessException;



}
