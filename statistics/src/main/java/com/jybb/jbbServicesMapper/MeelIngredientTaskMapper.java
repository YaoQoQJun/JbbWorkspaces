package com.jybb.jbbServicesMapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jybb.annotation.MybatisRepository;

@MybatisRepository
public interface MeelIngredientTaskMapper{
	
	/**
	 *得到每个用户当日勾选的任务总数和相应的用户ID，孕期时间
	 */
	public List<HashMap<String,Object>> userPeriodDetailCount(@Param("start")Integer start,@Param("end")Integer end);
	
	/**
	 * 根据孕期，得到该孕期应该完成的任务总数
	 */
	public String periodDetailCount(String periodDetail);
}
