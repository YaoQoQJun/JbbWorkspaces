package com.jybb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.HbaseDetail;
import com.jybb.pojo.HbaseDisable;
import com.jybb.pojo.HbaseExtension;

@MybatisRepository
public interface HbaseMapper{
	void saveExtension(HbaseExtension hbaseExtension)throws DataAccessException;

	void saveDisable(HbaseDisable hbaseDisable)throws DataAccessException;
	
	void createDataTable(@Param("tableName")String tableName)throws DataAccessException;

	void saveDatasMap(Map<String, String> map)throws DataAccessException;
	
	List<String> findAllUrl(@Param("tableName")String tableName,@Param("time")long time)throws DataAccessException;

	HbaseDetail findHbaseDetailByUrlAndTime(@Param("url")String url,@Param("time")long time)throws DataAccessException;
	
	List<HbaseDetail> findHbaseDetail(@Param("time")Long time)throws DataAccessException;
	
	String getHbaseDetail_ats(@Param("url")String url,@Param("time")long time);
	
	void saveHbaseDetail(HbaseDetail hbaseDetail)throws DataAccessException;
	
	void updateHbaseDetail(HbaseDetail hbaseDetail)throws DataAccessException;

	void updateHbaseDetail_pvs(@Param("url")String url,@Param("time")long time)throws DataAccessException;

	void updateHbaseDetail_uvs(@Param("url")String url,@Param("time")long time)throws DataAccessException;

	void updateHbaseDetail_ips(@Param("url")String url,@Param("time")long time,@Param("count_ip")Integer count_ip)throws DataAccessException;

	void updateHbaseDetail_ats(@Param("url")String url,@Param("time")long time,@Param("count_at")String count_at)throws DataAccessException;

	void updateHbaseDetail_op(@Param("url")String url,@Param("time")long time)throws DataAccessException;

	List<String> countIps(@Param("j_url")String j_url, @Param("ctime")Long ctime,@Param("tableName")String tableName)throws DataAccessException;

	

	


}
