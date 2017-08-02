package com.jybb.mapper;


import java.util.List;

import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Data;
import com.jybb.pojo.DataItem;
@MybatisRepository
public interface DataItemMapper{

	void addDataItem(DataItem dataItem)throws DataAccessException;

	List<DataItem> findAll(int state)throws DataAccessException;

	void addData(Data data)throws DataAccessException;
	
	List<Data> getDatasByDataItemId(Integer data_item_id)throws DataAccessException;

	void deleteData(Integer data_id)throws DataAccessException;

	void deleteDataItem(Integer data_item_id)throws DataAccessException;

	void deleteDataByDataItemId(Integer data_item_id)throws DataAccessException;

	void updateDataItem(DataItem dataItem)throws DataAccessException;

	DataItem getDataItemByDataItemId(Integer data_item_id)throws DataAccessException;

	Data getDataById(Integer id)throws DataAccessException;

	void updateData(Data data)throws DataAccessException;


	
}
