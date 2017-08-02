package com.jybb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.AppChannel;
import com.jybb.pojo.Channel;

@MybatisRepository
public interface AppChannelMapper{
	
	List<AppChannel> findAll()throws DataAccessException;

	List<AppChannel> findAllByPage(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer count()throws DataAccessException;
	
	void addAppChannel(AppChannel appChannel)throws DataAccessException;

	void updateAppChannel(AppChannel appChannel)throws DataAccessException;

	void deleteAppChannel(Integer id)throws DataAccessException;
	
	AppChannel findAppChannelByAppChannelName(@Param("app_channel_name")String app_channel_name,@Param("id")Integer id)throws DataAccessException;
}
