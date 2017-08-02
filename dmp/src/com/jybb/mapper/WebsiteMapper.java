package com.jybb.mapper;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Website;
@MybatisRepository
public interface WebsiteMapper{

	void addWebsite(Website website)throws DataAccessException;

	List<Website> findWebsitesByPage(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer countWebsites()throws DataAccessException;

	void updateWebsite(Website website)throws DataAccessException;

	void updateWebsiteState(Website website)throws DataAccessException;

	void deleteWebsite(Integer id)throws DataAccessException;

	void updateExtensionState(Integer website_id)throws DataAccessException;
	
	
}
