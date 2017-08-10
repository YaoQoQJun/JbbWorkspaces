package com.jybb.cmsMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Baike;

@MybatisRepository
public interface BaikeMapper{

	List<Baike> getBaikesByInputtime(@Param("start")Integer start, @Param("end")Integer end);
	
}
