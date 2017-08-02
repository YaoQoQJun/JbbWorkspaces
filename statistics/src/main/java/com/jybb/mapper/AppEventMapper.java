package com.jybb.mapper;



import java.util.HashMap;
import org.springframework.dao.DataAccessException;
import com.jybb.annotation.MybatisRepository;

@MybatisRepository
public interface AppEventMapper{
	void AddAppEvent(HashMap<String,String> appEventMap)throws DataAccessException;
}
