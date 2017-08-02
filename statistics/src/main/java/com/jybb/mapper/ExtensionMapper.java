package com.jybb.mapper;



import java.util.List;
import org.springframework.dao.DataAccessException;
import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Extension;
@MybatisRepository
public interface ExtensionMapper{
	
	List<Extension> findExtensions()throws DataAccessException;
	
}
