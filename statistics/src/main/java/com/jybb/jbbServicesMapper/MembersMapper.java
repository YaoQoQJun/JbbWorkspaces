package com.jybb.jbbServicesMapper;

import java.util.HashMap;
import com.jybb.annotation.MybatisRepository;

@MybatisRepository
public interface MembersMapper{
	
	public HashMap<String,String> getMemberByUid(Integer uid);	
	
}
