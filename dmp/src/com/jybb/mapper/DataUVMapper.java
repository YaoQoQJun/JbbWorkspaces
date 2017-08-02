package com.jybb.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jybb.annotation.MybatisRepository;

@MybatisRepository
public interface DataUVMapper{
	public List<Map<String,Object>> getSysData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String,Object>> getJ_languageData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String,Object>> getBroserData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);

	
	public List<Map<String,Object>> getJ_resolutionData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	
	public List<Map<String,Object>> getJ_color_depthData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	
	public List<Map<String,Object>> getJ_has_lied_browserData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String,Object>> getJ_has_lied_languagesData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);

	public List<Map<String,Object>> getJ_has_lied_osData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String,Object>> getJ_has_lied_resolutionData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String, Object>> getProvinceData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	
	
	
	public List<Map<String,Object>> getSysLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String,Object>> getJ_languageLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String,Object>> getBroserLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);

	public List<Map<String,Object>> getJ_color_depthLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	
	public List<Map<String,Object>> getJ_has_lied_browserLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String,Object>> getJ_has_lied_languagesLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);

	public List<Map<String,Object>> getJ_has_lied_osLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String,Object>> getJ_has_lied_resolutionLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
	public List<Map<String,Object>> getJ_resolutionLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);

	public List<Map<String, Object>> getProvinceLinkData(
			@Param("extension_link")String extension_link,
			@Param("start")long start,
			@Param("end")long end,
			@Param("tableName")String tableName);
	
}
