package com.jybb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.AppEventSet;

@MybatisRepository
public interface AppEventSetMapper{
	

	List<Map<String,String>> findAllByPage(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer count()throws DataAccessException;

	void addAppEventSet(AppEventSet appEventSet)throws DataAccessException;
				
	AppEventSet findAppEventSetByAppEventName(@Param("app_event_name")String app_event_name, @Param("id")Integer id);

	void deleteAppEventSet(@Param("id")Integer id)throws DataAccessException;

	void updateAppEventSet(AppEventSet appEventSet)throws DataAccessException;

	List<Map<String, String>> findAllByPageAndAppEventClassId(
			@Param("app_event_class_id")Integer app_event_class_id, @Param("start")Integer start, @Param("pageSize")Integer pageSize);
	Integer countByAppEventClassId(@Param("app_event_class_id")Integer app_event_class_id);

}
