package com.jybb.mapper;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Disable;
@MybatisRepository
public interface DisableMapper{

	
	List<Disable> findDisablesByPage(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer countDisables()throws DataAccessException;
	
	Disable getDisableById(Integer disable_id)throws DataAccessException;
	
	void updateWebsiteState(Disable disable)throws DataAccessException;
	
	void addDisable(Disable disable)throws DataAccessException;
	
	void deleteDisable(Integer id)throws DataAccessException;

	void updateDisable(Disable disable)throws DataAccessException;

	List<Disable> findDisables()throws DataAccessException;

	List<Disable> findDisablesByUrl(String url)throws DataAccessException;
	
}
