package com.jybb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Channel;

@MybatisRepository
public interface ChannelMapper{

	List<Channel> findAll(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer count()throws DataAccessException;

	Channel findChannelByChannelId(@Param("channel_id")String channel_id,@Param("id")Integer id)throws DataAccessException;

	void addChannel(Channel channel)throws DataAccessException;

	void updateChannel(Channel channel)throws DataAccessException;

	void deleteChannel(Integer id)throws DataAccessException;

}
