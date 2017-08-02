package com.jybb.membersMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.jybb.annotation.MybatisRepository;

@MybatisRepository
public interface MemberMapper{
	
	Integer countPcReg();//获取PC总注册数
	Integer countWapReg();//获取wap总注册数
	Integer countAppReg();//获取APP总注册数
	
	Integer countReg(@Param("start")long start, @Param("end")long end,@Param("regfrom")String regfrom);//获取今日每小时的PC注册量
	
	Integer countAllReg();//获取总注册数
	
	Integer countAllPcWapAppReg(@Param("regfrom")String regfrom);//获取PC或WAP或APP注册总数
	
	List<Map<String, Object>> getSexData(@Param("startDate")long startDate, @Param("endDate")long endDate, @Param("regfrom")String regfrom);
	List<Integer> getAgeData(@Param("startDate")long startDate, @Param("endDate")long endDate,@Param("regfrom")String regfrom);
	List<Map<String, Object>> getLocalData(@Param("startDate")long startDate, @Param("endDate")long endDate,@Param("regfrom")String regfrom);
	List<Map<String, Object>> getStateData(@Param("startDate")long startDate, @Param("endDate")long endDate,@Param("regfrom")String regfrom);
	
	List<String> getPregnancyDatas(@Param("startDate")long startDate, @Param("endDate")long endDate,@Param("regfrom")String regfrom);
	List<String> getBabyAgeDatas(@Param("startDate")long startDate, @Param("endDate")long endDate,@Param("regfrom")String regfrom);
	
	
}
