package com.jybb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.AppEventSet;

@MybatisRepository
public interface AppEventSetMapper{
	

	List<AppEventSet> findAllByPage(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer count()throws DataAccessException;

	void addAppEventSet(AppEventSet appEventSet)throws DataAccessException;
				
	AppEventSet findAppEventSetByAppEventName(@Param("app_event_name")String app_event_name, @Param("id")Integer id);

	void deleteAppEventSet(@Param("id")Integer id)throws DataAccessException;

	void updateAppEventSet(AppEventSet appEventSet)throws DataAccessException;

}
