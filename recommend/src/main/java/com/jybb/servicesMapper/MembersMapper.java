package com.jybb.servicesMapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Member;

@MybatisRepository
public interface MembersMapper{
	
	public List<Member> getMembersByRegDate(@Param("start")Integer start,@Param("end")Integer end);
	
}
