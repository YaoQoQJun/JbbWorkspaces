package com.jybb.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.sf.json.JSONObject;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyun.odps.Column;
import com.aliyun.odps.Odps;
import com.aliyun.odps.PartitionSpec;
import com.aliyun.odps.TableSchema;
import com.aliyun.odps.account.Account;
import com.aliyun.odps.account.AliyunAccount;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.RecordWriter;
import com.aliyun.odps.tunnel.TableTunnel;
import com.aliyun.odps.tunnel.TableTunnel.UploadSession;
import com.jybb.util.SendLog;


@Controller
@RequestMapping("/aliyun")
public class Aliyun {
	
	private static String accessId = "LTAICqlD5Y0Xv9he";
	private static String accessKey = "NTxHeNde58VzKme78y5NvwBGyVy9Yd";
	private Logger logger = LoggerFactory.getLogger(Aliyun.class);
	
	@RequestMapping(value="/getrec.do",produces="text/html;charset=UTF-8")
	@ResponseBody
	public String getrec(String bizCode,String scnCode,Integer recnum,String userId,String itemId,String category){
		String params="bizCode="+bizCode+"&scnCode="+scnCode+"&recnum="+recnum+"&userId="+userId+"&itemId="+itemId+"&category="+category;
		String result = "";
        BufferedReader in = null;
        int statusCode = 200;
		try {
				String url_prefix = "https://dtplus-cn-shanghai.data.aliyuncs.com/dataplus_139315/re/doRec?"+params;
				URL realUrl = new URL(url_prefix);
		        /*
		         * http header 参数
		         */
		        String method = "GET";
		        String accept = "application/json";
		        String content_type = "application/json";
		        String path = realUrl.getFile();
		        String date = SendLog.toGMTString(new Date());
		        // 1.对body做MD5+BASE64加密
		        // String bodyMd5 = MD5Base64(body);
		        String stringToSign = method + "\n" + accept + "\n" + "" + "\n" + content_type + "\n" + date + "\n" + path;
		        // 2.计算 HMAC-SHA1
		        String signature = SendLog.HMACSha1(stringToSign, accessKey);
		        // 3.得到 authorization header
		        String authHeader = "Dataplus " + accessId + ":" + signature;
		        // 打开和URL之间的连接
		        URLConnection connection = realUrl.openConnection();
		        // 设置通用的请求属性
		        connection.setRequestProperty("accept", accept);
		        connection.setRequestProperty("content-type", content_type);
		        connection.setRequestProperty("date", date);
		        connection.setRequestProperty("Authorization", authHeader);
		        connection.setRequestProperty("Connection", "keep-alive");
		        // 建立实际的连接
		        connection.connect();
		        // 定义 BufferedReader输入流来读取URL的响应
		        statusCode = ((HttpURLConnection)connection).getResponseCode();
		        if(statusCode != 200) {
		            in = new BufferedReader(new InputStreamReader(((HttpURLConnection)connection).getErrorStream()));
		        } else {
		            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		        }
		        String line;
		        while ((line = in.readLine()) != null) {
		            result += line;
		        }
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		    } finally {
	        try {
	            if (in != null) {
	                in.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		return result;
	}
	
	@RequestMapping("/articleLog.do")
	public void sendLog(String json){
		if(json==null||"".equals(json.trim())){
			return;
		}
		JSONObject jsonObject=JSONObject.fromObject(json);
		/*上传用户日志api--参数*/
        String biz_code = "jbb_article_code";
        String token = "alidatac275fa604300609b6e350870e";
        String url_prefix = "https://dtplus-cn-shanghai.data.aliyuncs.com/dataplus_139315/re/uploadlog?businessName=recommend";
        /*上传用户行为离线数据--参数*/
		String odpsUrl = "http://service.odps.aliyun.com/api";
		String project = "Jbb_Rec_Compute";
		String table = "user_behavior";
		String partition = "ds=20170804";
		
		/*上传用户行为离线数据*/
		try {
			Account account = new AliyunAccount(accessId, accessKey);
			Odps odps = new Odps(account);
			odps.setEndpoint(odpsUrl);
			odps.setDefaultProject(project);
			TableTunnel tunnel = new TableTunnel(odps);
			PartitionSpec partitionSpec = new PartitionSpec(partition);
			UploadSession uploadSession = tunnel.createUploadSession(project,table,partitionSpec);
			TableSchema schema = uploadSession.getSchema();
			  // 准备数据后打开Writer开始写入数据，准备数据后写入一个Block
			  // 单个Block内写入数据过少会产生大量小文件 严重影响计算性能， 强烈建议每次写入64MB以上数据(100GB以内数据均可写入同一Block)
			  // 可通过数据的平均大小与记录数量大致计算总量即 64MB < 平均记录大小*记录数 < 100GB
			RecordWriter recordWriter = uploadSession.openRecordWriter(0);
			Record record = uploadSession.newRecord();

			for (int j = 0; j < schema.getColumns().size(); j++) {
                Column column = schema.getColumn(j);
                
                String columnName=column.getName();
                if(columnName.equals("user_id")){
                	record.setString(j, jsonObject.getString("user_id"));
                	continue;
                }
                
                if(columnName.equals("act_obj")){
                	record.setString(j, jsonObject.getString("act_obj"));
                	continue;
                }
                
                if(columnName.equals("obj_type")){
                	record.setString(j, "item");
                	continue;
                }
                
                if(columnName.equals("bhv_type")){
                	record.setString(j, jsonObject.getString("bhv_type"));
                	continue;
                }
                
                if(columnName.equals("bhv_amt")){
                	record.set(j, jsonObject.getDouble("bhv_amt"));
                	continue;
                }
                
                if(columnName.equals("bhv_cnt")){
                	record.set(j, jsonObject.getDouble("bhv_cnt"));
                	continue;
                }
                
                if(columnName.equals("bhv_datetime")){
                	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                	record.setDatetime(j,sdf.parse(jsonObject.getString("bhv_datetime")));
                	continue;
                }
                
	        }
			
			// Write数据至服务端，每写入8KB数据会进行一次网络传输
            // 若120s没有网络传输服务端将会关闭连接，届时该Writer将不可用，需要重新写入
			recordWriter.write(record);
			
	        recordWriter.close();
	        uploadSession.commit(new Long[]{0L});
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*上传用户日志api*/
        List<String> logs = new LinkedList<String>();
		logs.add(json);
        if (logs == null || logs.size() == 0) {
            return;
        }
        // 积累的多条日志JSONArray数组
        JSONArray content = new JSONArray();
        // 用户向content中填写多条日志(要么积累一分钟的量, 要么积累到2000~3000条)
        for (String log : logs) {
            content.put(log);
        }
        
        try {
			SendLog.sendPostHTTPS(content, biz_code, token, url_prefix);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return;
	}

}
