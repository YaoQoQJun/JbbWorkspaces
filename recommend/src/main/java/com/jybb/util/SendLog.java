package com.jybb.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;

public class SendLog{
	
//https://dtplus-cn-shanghai.data.aliyuncs.com/dataplus_139315/re/uploadlog?businessName=recommend&customerName=jbb_article_code&token=alidatac275fa604300609b6e350870e
	

    private static String ak_id = "LTAICqlD5Y0Xv9he";
    private static String ak_secret = "NTxHeNde58VzKme78y5NvwBGyVy9Yd";
    
    public static void main(String[] args) throws IOException {
        String biz_code = "jbb_article_code";
        String token = "alidatac275fa604300609b6e350870e";
        String url_prefix = "https://dtplus-cn-shanghai.data.aliyuncs.com/dataplus_139315/re/uploadlog?businessName=recommend";
        JSONArray content = new JSONArray();
    	SendLog.sendPostHTTPS(content, biz_code, token, url_prefix);
    }
    
    /**
     * 日志API
     * @throws IOException 
     */
    public static void sendLogAPI(String biz_code,String token,String url_prefix) throws IOException{
    	System.out.println("--------------------------1");
        List<String> logs = new LinkedList<String>();
        System.out.println("--------------------------2");
        
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        logs.add("{\"user_id\":\"15001\",\"action\":\"login\",\"tags\": \"{\"age\":\"1\",\"gender\":\"23\"}\"}");
        
        logs.add("{\"user_id\":\"15001\", \"item_id\":802, \"action\":\"use\",\"bhv_datetime\": \""+sdf.format(new Date())+"\"}");
        System.out.println("--------------------------3");
        if (logs == null || logs.size() == 0) {
            return;
        }
        System.out.println("--------------------------4");
        // 积累的多条日志JSONArray数组
        JSONArray content = new JSONArray();
        // 用户向content中填写多条日志(要么积累一分钟的量, 要么积累到2000~3000条)
        for (String log : logs) {
            content.put(log);
        }
        System.out.println("--------------------------5");
        // 或者
//        for(JSONObject log : logs) {
//            content.put(log);
//        }
        // 上传日志
        System.out.println(content);
        sendPostHTTPS(content,biz_code,token,url_prefix);
        System.out.println("--------------------------6");
    }
    
    /**
     * 使用HTTPS对多条日志进行GZIP压缩并上传
     * 
     * @param content 日志JSONArray数组(要么积累一分钟的量, 要么积累到2000~3000条)
     * @param ak_id
     * @param ak_secret
     * @throws IOException
     */
    public static void sendPostHTTPS(JSONArray content,String biz_code,String token,String url_prefix) throws IOException {
        if (content != null && content.length() > 0) {
            HttpsURLConnection conn = null;
            try {
                BufferedReader in = null;
                URL upload_url = new URL(url_prefix + "&customerName=" + biz_code + "&token=" + token);
                StringBuilder result = new StringBuilder();
                /*
                 * http header 参数 必须设置
                 */
                String method = "POST";
                String accept = "application/json";
                String content_type = "application/json";
                String path = upload_url.getFile();
                String date = toGMTString(new Date());
                String content_encoding = "gzip";
                // 1. 对日志JSONArray进行FGZIP压缩
                byte[] body = compressToByte(content);
                // 2. 对body做MD5+BASE64加密
                String body_md5 = MD5Base64(body);
                String string_to_sign = method + "\n" + accept + "\n" + body_md5 + "\n" + content_type + "\n" + date + "\n" + path;
                // 3.计算 HMAC-SHA1
                String signature = HMACSha1(string_to_sign, ak_secret);
                // 4.得到 authorization header
                String auth_header = "Dataplus " + ak_id + ":" + signature;
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
                    // 返回jsonobject
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        result.append(line);
                    }
                    if (result.length() > 0) {
                        JSONObject rsp_json = new JSONObject(result.toString());
                        System.out.println(rsp_json);
                        int success = rsp_json.getInt("success");
                        if (success == 1) {
                            System.out.println("数据上传成功");
                        } else {
                            System.out.println("数据上传失败, 出错信息: " + rsp_json.getString("errMsg"));
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
    
    /*
     * GET请求
     */
    public static String sendGet(String url, String ak_id, String ak_secret) throws Exception {
        String result = "";
        BufferedReader in = null;
        int statusCode = 200;
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "GET";
            String accept = "json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            // String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + "" + "\n" + content_type + "\n" + date + "\n" + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
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
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }
    
    /*
     * 计算MD5+BASE64
     */
    public static String MD5Base64(byte[] utfBytes) {
        if (utfBytes == null)
            return null;
        String encodeStr = "";
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            BASE64Encoder b64Encoder = new BASE64Encoder();
            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }
    /*
     * 计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
    /*
     * 等同于javaScript中的 new Date().toUTCString();
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }
    /*
     * 字符串压缩为字节数组
     */
    public static byte[] compressToByte(JSONArray content) {
        try {
            int len1 = content.toString().getBytes("utf-8").length;
            System.out.println("压缩前数据大小(B): " + len1);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(content.toString().getBytes("utf-8"));
            gzip.finish();
            gzip.flush();
            gzip.close();
            byte[] data = out.toByteArray();
            int len2 = data.length;
            System.out.println("压缩后数据大小(B): " + len2);
            System.out.println("压缩率: " + len2 * 1.0 / len1);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static byte[] compressToByte(JSONObject content) {
        try {
            int len1 = content.toString().getBytes("utf-8").length;
            System.out.println("压缩前数据大小(B): " + len1);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(content.toString().getBytes("utf-8"));
            gzip.finish();
            gzip.flush();
            gzip.close();
            byte[] data = out.toByteArray();
            int len2 = data.length;
            System.out.println("压缩后数据大小(B): " + len2);
            System.out.println("压缩率: " + len2 * 1.0 / len1);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}