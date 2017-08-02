package com.jybb.mapper;



import java.util.List;
import org.springframework.dao.DataAccessException;
import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Disable;
@MybatisRepository
public interface DisableMapper{
	List<Disable> findDisables()throws DataAccessException;
}
