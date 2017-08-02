package com.jybb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.AppVersion;

@MybatisRepository
public interface AppVersionMapper{
	
	List<AppVersion> findAll()throws DataAccessException;

	List<AppVersion> findAllByPage(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer count()throws DataAccessException;
	
	void addAppVersion(AppVersion appVersion)throws DataAccessException;

	void updateAppVersion(AppVersion appVersion)throws DataAccessException;

	void deleteAppVersion(Integer id)throws DataAccessException;
	
	AppVersion findAppVersionByAppVersionName(@Param("app_version_name")String app_version_name,@Param("id")Integer id)throws DataAccessException;
}
