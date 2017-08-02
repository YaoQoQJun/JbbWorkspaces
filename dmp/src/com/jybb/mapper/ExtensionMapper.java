package com.jybb.mapper;



import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jybb.annotation.MybatisRepository;
import com.jybb.pojo.Extension;
import com.jybb.pojo.SonLink;
import com.jybb.pojo.Website;
@MybatisRepository
public interface ExtensionMapper{

	List<Website> getWebsites()throws DataAccessException;

	Website getWebsite(Integer website_id)throws DataAccessException;

	void addExtension(Extension extension)throws DataAccessException;

	List<Extension> findExtensionsByPage(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize)throws DataAccessException;

	Integer countExtensions()throws DataAccessException;

	void updateWebsiteState(Extension extension)throws DataAccessException;

	void updateExtension(Extension extension)throws DataAccessException;

	void addSonLink(SonLink sonLink)throws DataAccessException;

	List<SonLink> getLinks(Integer extension_id)throws DataAccessException;

	void updateExtensionSonCount1(Integer extension_id)throws DataAccessException;

	void deleteSonLink(Integer id)throws DataAccessException;

	void updateExtensionSonCount2(Integer extension_id)throws DataAccessException;

	void deleteSonLinkByExtensionId(Integer extension_id)throws DataAccessException;

	void deleteExtension(Integer id)throws DataAccessException;

	List<Extension> findExtensionsByPageAndState(
			@Param("start")Integer start, 
			@Param("pageSize")Integer pageSize, 
			@Param("state")int state,@Param("channel_id")String channel_id)throws DataAccessException;

	Integer countExtensionsByState(@Param("state")int state,@Param("channel_id")String channel_id)throws DataAccessException;

	Extension getExtensionById(Integer extension_id)throws DataAccessException;

	List<Extension> findExtensions()throws DataAccessException;

	List<Extension> findExtensionsByUrl(@Param("url")String url)throws DataAccessException;


	
}
