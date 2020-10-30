package server.service;
import static org.junit.Assert.assertEquals;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONObject;
import server.dto.ImageHolder;
import server.dto.PeopleExecution;


import server.BaseTest;
import server.dto.LocalAuthExecution;
import server.entity.LocalAuth;
import server.entity.PersonInfo;
import server.enums.PeopleStateEnum;
import server.enums.WechatAuthStateEnum;
import server.util.GsonUtils;
import java.io.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import java.net.*;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImageSaveTest  extends BaseTest{
	@Autowired 
	private PeopleService peopleService;
	public static String getContentTypeByLocal(String fileUrl) {
		String contentType = null;
		Path path = Paths.get(fileUrl);
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {  
            e.printStackTrace();
        }
        System.out.println("getContentTypeByLocal, File ContentType is : " + contentType);
        return contentType;
	}


	   @Test
	   @SuppressWarnings("rawtypes")
	    public  void formUpload() {
		   String urlStr= "http://localhost:8080/game-server/shopadmin/addimage";
		           
		   //String urlStr= "http://localhost:8080/game-server/local/addimage";
		   Map<String, Integer> textMap = new HashMap<String, Integer>();
	        //可以设置多个input的name，value
		   //LoginTest.Logintest();
	        textMap.put("localAuthId", 20);
	        //textMap.put("type", "2");
	        //设置file的name，路径
	        Map<String, String> fileMap = new HashMap<String, String>();
	        String fileUrl="D:/practiceLesson/FaceTest1/timg.jpg";
	        fileMap.put("thumbnail", fileUrl);
	        String contentType =getContentTypeByLocal(fileUrl);//"";//image/png
	        String res = "";
	        HttpURLConnection conn = null;
	        // boundary就是request头和上传文件内容的分隔符
	        String BOUNDARY = "---------------------------123821742118716"; 
	        try {
	            URL url = new URL(urlStr);
	            conn = (HttpURLConnection) url.openConnection();
	            conn.setConnectTimeout(5000);
	            conn.setReadTimeout(30000);
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            conn.setUseCaches(false);
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            // conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
	            conn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
	            OutputStream out = new DataOutputStream(conn.getOutputStream());
	            // text
	            if (textMap != null) {
	                StringBuffer strBuf = new StringBuffer();
	                Iterator iter = textMap.entrySet().iterator();
	                while (iter.hasNext()) {
	                    Map.Entry entry = (Map.Entry) iter.next();
	                    String inputName = (String) entry.getKey();
	                    Integer inputValue = (Integer) entry.getValue();
	                    if (inputValue == null) {
	                        continue;
	                    }
	                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
	                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
	                    strBuf.append(inputValue);
	                }
	                out.write(strBuf.toString().getBytes());
	            }
	            // file
	            if (fileMap != null) {
	                Iterator iter = fileMap.entrySet().iterator();
	                while (iter.hasNext()) {
	                    Map.Entry entry = (Map.Entry) iter.next();
	                    String inputName = (String) entry.getKey();
	                    String inputValue = (String) entry.getValue();
	                    if (inputValue == null) {
	                        continue;
	                    }
	                    File file = new File(inputValue);
	                    String filename = file.getName();
	                    
	                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
	                    contentType = new MimetypesFileTypeMap().getContentType(file);
	                    //contentType非空采用filename匹配默认的图片类型
	                    if(!"".equals(contentType)){
	                        if (filename.endsWith(".png")) {
	                            contentType = "image/png"; 
	                        }else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
	                            contentType = "image/jpeg";
	                        }else if (filename.endsWith(".gif")) {
	                            contentType = "image/gif";
	                        }else if (filename.endsWith(".ico")) {
	                            contentType = "image/image/x-icon";
	                        }
	                    }
	                    if (contentType == null || "".equals(contentType)) {
	                        contentType = "application/octet-stream";
	                    }
	                    StringBuffer strBuf = new StringBuffer();
	                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
	                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
	                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
	                    out.write(strBuf.toString().getBytes());
	                    DataInputStream in = new DataInputStream(new FileInputStream(file));
	                    int bytes = 0;
	                    byte[] bufferOut = new byte[1024];
	                    while ((bytes = in.read(bufferOut)) != -1) {
	                        out.write(bufferOut, 0, bytes);
	                    }
	                    in.close();
	                }
	            }
	            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
	            out.write(endData);
	            out.flush();
	            out.close();
	            // 读取返回数据
	            StringBuffer strBuf = new StringBuffer();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                strBuf.append(line).append("\n");
	            }
	            res = strBuf.toString();
	            reader.close();
	            reader = null;
	        } catch (Exception e) {
	            System.out.println("发送POST请求出错。" + urlStr);
	            e.printStackTrace();
	        } finally {
	            if (conn != null) {
	                conn.disconnect();
	                conn = null;
	            }
	        }
	        System.out.println(res);
	        //return res;
	    }
}
