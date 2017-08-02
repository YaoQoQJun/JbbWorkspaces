package com.jybb.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.DmpUV;
import com.jybb.pojo.HbaseDetail;
import com.jybb.pojo.Seo;

@MybatisRepository
public interface HbaseMapper{

	Seo getExtensionDayData(
			@Param("extension_id")Integer extension_id, 
			@Param("start")Long start, 
			@Param("end")Long end)throws DataAccessException;
	
	Seo getDisableDayData(
			@Param("disable_id")Integer disable_id, 
			@Param("start")Long start, 
			@Param("end")Long end)throws DataAccessException;

	Seo getExtensionData(
			@Param("extension_id")Integer extension_id, 
			@Param("start")long start)throws DataAccessException;
	
	Seo getDisableData(
			@Param("disable_id")Integer disable_id, 
			@Param("start")long start)throws DataAccessException;
	
	List<String> findUrlByPage(
			@Param("extension_link")String extension_link,
			@Param("tableName")String tableName,
			@Param("t1")long t1,
			@Param("t2")long t2,
			@Param("start")Integer start,
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer countUrl(
			@Param("extension_link")String extension_link,
			@Param("tableName")String tableName,
			@Param("t1")long t1,
			@Param("t2")long t2)throws DataAccessException;

	List<HbaseDetail> findHbaseDetailByPage(
			@Param("extension_link")String extension_link, 
			@Param("t1")long t1,
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer countHbaseDetail(
			@Param("extension_link")String extension_link, 
			@Param("t1")long t1)throws DataAccessException;
	
	List<HbaseDetail> findHbaseDetailBySearch_link(
			@Param("search_link")String search_link, 
			@Param("t1")long t1,
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer countHbaseDetailBySearch_link(
			@Param("search_link")String search_link, 
			@Param("t1")long t1)throws DataAccessException;

	
	
}
