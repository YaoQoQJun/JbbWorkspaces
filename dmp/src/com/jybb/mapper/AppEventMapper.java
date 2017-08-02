package com.jybb.mapper;


import org.apache.ibatis.annotations.Param;

import com.jybb.annotation.MybatisRepository;

@MybatisRepository
public interface AppEventMapper{

	Integer countEvent(
			@Param("event_name")String event_name, 
			@Param("client_model")String client_model, 
			@Param("channel_name")String channel_name,
			@Param("app_version")String app_version, 
			@Param("startTime")String startTime, 
			@Param("endTime")String endTime);

	Integer countUser(@Param("event_name")String event_name, 
			@Param("client_model")String client_model, 
			@Param("channel_name")String channel_name,
			@Param("app_version")String app_version, 
			@Param("startTime")String startTime, 
			@Param("endTime")String endTime);

}
