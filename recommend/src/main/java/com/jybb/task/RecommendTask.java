package com.jybb.task;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.odps.Column;
import com.aliyun.odps.Odps;
import com.aliyun.odps.PartitionSpec;
import com.aliyun.odps.TableSchema;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.RecordWriter;
import com.aliyun.odps.tunnel.TableTunnel;
import com.aliyun.odps.tunnel.TunnelException;
import com.aliyun.odps.tunnel.TableTunnel.UploadSession;
import com.jybb.cmsMapper.BaikeMapper;
import com.jybb.pojo.Baike;
import com.jybb.pojo.Member;
import com.jybb.servicesMapper.MembersMapper;
import com.jybb.util.SendLog;

@Component
public class RecommendTask {
	
	private static String accessId = "LTAICqlD5Y0Xv9he";
	private static String accessKey = "NTxHeNde58VzKme78y5NvwBGyVy9Yd";
	private static String odpsUrl = "http://service.odps.aliyun.com/api";
	private static String project = "Jbb_Rec_Compute";

	private Logger logger = LoggerFactory.getLogger(RecommendTask.class);
	
	@Autowired
	private MembersMapper membersMapper;
	
	@Autowired
	private BaikeMapper baikeMapper;

	/**
	 * 每10分钟 扫描数据库用户表，将新增用户添加到离线数据库中，以及通过日志API上传用户。
	 */
	public void doMembersJob(){
		System.out.println("每10分钟 扫描数据库用户表，将新增用户添加到离线数据库中，以及通过日志API上传用户。-----------------！");
        /*上传数据到离线数据库--参数*/
		String table = "user_meta";
		String partition = "ds=20170804";
		/*上传用户日志api--参数*/
        String biz_code = "jbb_article_code";
        String token = "alidatac275fa604300609b6e350870e";
        String url_prefix = "https://dtplus-cn-shanghai.data.aliyuncs.com/dataplus_139315/re/uploadlog?businessName=recommend";
		
		try {
			/*上传数据到离线数据库*/
			long l1=System.currentTimeMillis();
			long l2=(l1-1000*60*10)/1000; 
			Integer end=(int)(l1/1000);
			Integer start=(int)l2;
			List<Member> members=membersMapper.getMembersByRegDate(start, end);
			if(members==null||members.size()==0){
				return;
			}
			
			Account account = new AliyunAccount(accessId, accessKey);
			Odps odps = new Odps(account);
			odps.setEndpoint(odpsUrl);
			odps.setDefaultProject(project);
			TableTunnel tunnel = new TableTunnel(odps);
			
			PartitionSpec partitionSpec = new PartitionSpec(partition);
			UploadSession uploadSession = tunnel.createUploadSession(project,table,partitionSpec);
			System.out.println("Session Status is : " + uploadSession.getStatus().toString());
			TableSchema schema = uploadSession.getSchema();
			  // 准备数据后打开Writer开始写入数据，准备数据后写入一个Block
			  // 单个Block内写入数据过少会产生大量小文件 严重影响计算性能， 强烈建议每次写入64MB以上数据(100GB以内数据均可写入同一Block)
			  // 可通过数据的平均大小与记录数量大致计算总量即 64MB < 平均记录大小*记录数 < 100GB
			RecordWriter recordWriter = uploadSession.openRecordWriter(0);
			Record record = uploadSession.newRecord();
			
			for (int i = 0; i < members.size(); i++) {
				Member member=members.get(i);
				Integer birhyear=member.getBirthyear();
				
				Integer age=0;
				if(birhyear>0){
					Date d=new Date();
					Integer a=d.getYear();
					age=a+1900-birhyear;
				}
				
				StringBuilder sb=new StringBuilder("");
				sb.append("gender\003");
				sb.append(member.getSex());
				sb.append("\002age\003");
				sb.append(age);
				
				for (int j = 0; j < schema.getColumns().size(); j++) {
	                Column column = schema.getColumn(j);
	                
	                String columnName=column.getName();
	                
	                if(columnName.equals("user_id")){
	                	record.setString(j, member.getUid()+"");
	                	continue;
	                }
	                
	                if(columnName.equals("tags")){
	                	record.setString(j, sb.toString());
	                	continue;
	                }
	                
		        }
				recordWriter.write(record);
			}
	        recordWriter.close();
	        uploadSession.commit(new Long[]{0L});
	        System.out.println("upload success!");
	        
	        /*上传用户日志api*/
	        List<String> logs = new LinkedList<String>();
	        for (int i = 0; i <members.size(); i++) {
				Member member=members.get(i);
				
				Integer age=0;
				if(member.getBirthyear()>0){
					Date d=new Date();
					Integer a=d.getYear();
					age=a+1900-member.getBirthyear();
				}
				
				JSONObject obj = new JSONObject();
				obj.put("action","login");
				obj.put("user_id",member.getUid()+"");
				obj.put("tags","{'age':'"+age+"','gender':'"+member.getSex()+"'}");
				logs.add(obj.toString());
				
			}
	        
	        if (logs == null || logs.size() == 0) {
	            return;
	        }
	        // 积累的多条日志JSONArray数组
	        JSONArray content = new JSONArray();
	        // 用户向content中填写多条日志(要么积累一分钟的量, 要么积累到2000~3000条)
	        for (String log : logs) {
	            content.put(log);
	        }
	        SendLog.sendPostHTTPS(content, biz_code, token, url_prefix);
	        
		} catch (TunnelException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}catch (IOException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
		
	
	}
	
	/**
	 * 每10分钟 扫描数据库百科表，将新增百科添加到离线数据库中，以及通过日志API上传百科。
	 */
	public void doBaikeJob(){
		System.out.println("每10分钟 扫描数据库百科表，将新增百科添加到离线数据库中，以及通过日志API上传百科-----------------！");
		
		 /*上传数据到离线数据库--参数*/
		String table = "item_meta";
		String partition = "ds=20170804";
		/*上传用户日志api--参数*/
        String biz_code = "jbb_article_code";
        String token = "alidatac275fa604300609b6e350870e";
        String url_prefix = "https://dtplus-cn-shanghai.data.aliyuncs.com/dataplus_139315/re/uploadlog?businessName=recommend";
		
        try {
			/*上传数据到离线数据库*/
			long l1=System.currentTimeMillis();
			long l2=(l1-1000*60*10)/1000; 
			Integer end=(int)(l1/1000);
			Integer start=(int)l2;
			List<Baike> baikes=baikeMapper.getBaikesByInputtime(start, end);
			if(baikes==null||baikes.size()==0){
				return;
			}
			
			Account account = new AliyunAccount(accessId, accessKey);
			Odps odps = new Odps(account);
			odps.setEndpoint(odpsUrl);
			odps.setDefaultProject(project);
			TableTunnel tunnel = new TableTunnel(odps);
			PartitionSpec partitionSpec = new PartitionSpec(partition);
			UploadSession uploadSession = tunnel.createUploadSession(project,table,partitionSpec);
			
			System.out.println("Session Status is : " + uploadSession.getStatus().toString());
			
		  // 准备数据后打开Writer开始写入数据，准备数据后写入一个Block
		  // 单个Block内写入数据过少会产生大量小文件 严重影响计算性能， 强烈建议每次写入64MB以上数据(100GB以内数据均可写入同一Block)
		  // 可通过数据的平均大小与记录数量大致计算总量即 64MB < 平均记录大小*记录数 < 100GB
			TableSchema schema = uploadSession.getSchema();
			RecordWriter recordWriter = uploadSession.openRecordWriter(0);
			Record record = uploadSession.newRecord();
			
			for (int i = 0; i < baikes.size(); i++) {
				Baike baike=baikes.get(i);
				
				StringBuilder sb=new StringBuilder();
				String[] keywords=baike.getKeywords().split(" ");
				
				for (int j = 0; j < keywords.length; j++) {
					String str=keywords[j].trim();
					str=str.replace(" ", "");
					if(str==null||"".equals(str)||str.length()==0){
						continue;
					}
					if(j==0){
						sb.append(str);
						sb.append("\003");
						sb.append("1");
					}else{
						sb.append("\002");
						sb.append(str);
						sb.append("\003");
						sb.append("1");
					}
				}
				for (int j = 0; j < schema.getColumns().size(); j++) {
	                Column column = schema.getColumn(j);
	                
	                String columnName=column.getName();
	                
	                if(columnName.equals("item_id")){
	                	record.setString(j, baike.getId()+"");
	                	continue;
	                }
	                
	                if(columnName.equals("category")){
	                	record.setString(j, baike.getCatid()+"");
	                	continue;
	                }
	                
	                if(columnName.equals("keywords")){
	                	record.setString(j, sb.toString());
	                	continue;
	                }
	                
	                if(columnName.equals("description")){
	                	record.setString(j, baike.getDescription());
	                	continue;
	                }
	                
	                if(columnName.equals("properties")){
	                	record.setString(j, "title\003"+baike.getTitle());
	                	continue;
	                }
	                
	                if(columnName.equals("update_datetime")){
	                	Date d=new Date((long)(baike.getUpdatetime())*1000l);
	                	record.setDatetime(j, d);
	                	continue;
	                }
	                
		        }
				
				// Write数据至服务端，每写入8KB数据会进行一次网络传输
	            // 若120s没有网络传输服务端将会关闭连接，届时该Writer将不可用，需要重新写入
				recordWriter.write(record);
				
			}
	        recordWriter.close();
	        uploadSession.commit(new Long[]{0L});
	        System.out.println("upload success!");
	        
	        /*上传百科日志api*/
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        List<String> logs = new LinkedList<String>();
	        for (int i = 0; i <baikes.size(); i++) {
				Baike baike=baikes.get(i);
				
				StringBuilder sb=new StringBuilder();
				String[] keywords=baike.getKeywords().split(" ");
				sb.append("[");
				for (int j = 0; j < keywords.length; j++) {
					String str=keywords[j].trim();
					str=str.replace(" ", "");
					if(str==null||"".equals(str)||str.length()==0){
						continue;
					}
					sb.append("\"");
					sb.append(str);
					sb.append("\",");
					
				}
				sb.delete(sb.length()-1, sb.length());
				sb.append("]");
				
				JSONObject obj = new JSONObject();
				obj.put("action","item");
				obj.put("item_id",baike.getId()+"");
				obj.put("category",baike.getCatid()+"");
				obj.put("keywords",sb.toString());
				obj.put("description",baike.getDescription());
				obj.put("properties","{'title':'"+baike.getTitle()+"'}");
				obj.put("deleted",false);
				obj.put("update_datetime",sdf.format(new Date()));
				logs.add(obj.toString());
			}
	        
	        if (logs == null || logs.size() == 0) {
	            return;
	        }
	        // 积累的多条日志JSONArray数组
	        JSONArray content = new JSONArray();
	        // 用户向content中填写多条日志(要么积累一分钟的量, 要么积累到2000~3000条)
	        for (String log : logs) {
	            content.put(log);
	        }
	        SendLog.sendPostHTTPS(content, biz_code, token, url_prefix);
	        
        } catch (TunnelException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}catch (IOException e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			logger.warn(e.getMessage());
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 每天1点执行离线计算任务。
	 */
	public void doComputeJob(){
		System.out.println("每天凌晨执行一次计算任务-----------------！");
		String url_prefix = "https://dtplus-cn-shanghai.data.aliyuncs.com/dataplus_139315/re/tasks";
        /*上传用户日志api*/
        JSONObject obj = new JSONObject();
		obj.put("bizCode","jbb_article_code");
		obj.put("scnCode","jbb_article_code");
		obj.put("ds","20170804");
        
        HttpsURLConnection conn = null;
        try {
            BufferedReader in = null;
            URL upload_url = new URL(url_prefix);
            StringBuilder result = new StringBuilder();
            /*
             * http header 参数 必须设置
             */
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = upload_url.getFile();
            String date = SendLog.toGMTString(new Date());
            String content_encoding = "gzip";
            // 1. 对日志JSONArray进行FGZIP压缩
            byte[] body = SendLog.compressToByte(obj);
            // 2. 对body做MD5+BASE64加密
            String body_md5 = SendLog.MD5Base64(body);
            String string_to_sign = method + "\n" + accept + "\n" + body_md5 + "\n" + content_type + "\n" + date + "\n" + path;
            // 3.计算 HMAC-SHA1
            String signature = SendLog.HMACSha1(string_to_sign, accessKey);
            // 4.得到 authorization header
            String auth_header = "Dataplus " + accessId + ":" + signature;
            // 发起连接
            conn = (HttpsURLConnection) upload_url.openConnection();
            // 设置超时, 建议1分钟, 可以更大一点
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            // 设置请求方法
            conn.setRequestMethod("GET");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", auth_header);
            // 必须要设置为GZIP，否则服务器会不接受
            conn.setRequestProperty("Content-Encoding", content_encoding);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 设置是非缓存
            conn.setUseCaches(false);
            // 传输body流 必须GZIP加密后字节数组
            DataOutputStream data_stream = new DataOutputStream(conn.getOutputStream());
            data_stream.write(body);
            data_stream.flush();
            data_stream.close();
            
            System.out.println(conn);
            System.out.println(conn.getResponseMessage());
            System.out.println(conn.getResponseCode());
            if (conn.getResponseCode() != 200) {
                System.err.println("日志API连接失败!");
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
                if (result.length() > 0) {
                    JSONObject rsp_json = new JSONObject(result.toString());
                    System.out.println(rsp_json);
                    String success = rsp_json.getString("code");
                    if ("SUCCESS".equals(success)) {
                        System.out.println("计算任务启动成功");
                    } else {
                        System.out.println("计算任务启动失败, 出错信息: " + rsp_json.getString("message"));
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
	}

	
}
