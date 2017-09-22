package com.jybb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.AppChannel;
import com.jybb.pojo.AppEventClass;
import com.jybb.pojo.Channel;

@MybatisRepository
public interface AppEventClassMapper{
	
	List<AppEventClass> findAll()throws DataAccessException;

	List<AppEventClass> findAllByPage(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer count()throws DataAccessException;
	
	void addAppEventClass(AppEventClass appEventClass)throws DataAccessException;

	void updateAppEventClass(AppEventClass appEventClass)throws DataAccessException;

	void deleteAppEventClass(Integer id)throws DataAccessException;
	
	AppEventClass findAppEventClassByAppEventClassName(@Param("app_event_class_name")String app_event_class_name,@Param("id")Integer id)throws DataAccessException;

}
