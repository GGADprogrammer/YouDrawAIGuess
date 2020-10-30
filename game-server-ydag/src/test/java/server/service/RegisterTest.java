package server.service;
import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONObject;

/*import com.google.gson.JsonObject;
import com.google.gson.JsonParser;*/

/*import com.google.gson.Gson;
*/
import server.util.*;
import server.BaseTest;
import server.dto.LocalAuthExecution;
import server.entity.LocalAuth;
import server.entity.PersonInfo;
import server.enums.WechatAuthStateEnum;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.*;
import org.apache.http.util.EntityUtils;
//import org.json.JSONObject;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RegisterTest extends BaseTest{
	 public static String getRequsetData(String url,String json) throws Exception{
   	  System.out.println("-----------传入json：---------------------------"+json);
   	  HttpPost httpPost = new HttpPost(url);
   	  HttpClient httpClient = HttpClients.createDefault();
   	  StringEntity postEntity = new StringEntity(json, "UTF-8");
   	  httpPost.addHeader("Content-Type", "application/json");
   	  httpPost.setEntity(postEntity);
   	  HttpResponse httpResponse = httpClient.execute(httpPost);
   	  HttpEntity httpEntity = httpResponse.getEntity();
   	  String b = EntityUtils.toString(httpEntity, "UTF-8");
   	  System.out.println("-----------返回值：---------------------------"+b);
   	  //JSONObject JS = JSONObject.fromObject(b);
   	  //return JS ;
   	  return b;
   	  }
      @Test
      public void reTest() {
      String path="http://localhost:8080/game-server/local/bindlocalauth";
              // http://192.168.1.100:8080/game-server/local/bindlocalauth
      String MULTIPART_FROM_DATA = "multipart/form-data";
      String BOUNDARY = java.util.UUID.randomUUID().toString();
      String encoding="UTF-8";
      try {
          Map<String, Object> map = new HashMap<>();
           //username必须无重复,不写反馈了，直接在演示的时候注意一下
           map.put("userName","huaiying4");//O
           map.put("password","test123");//O
           map.put("name","huaiying2");//O
           map.put("email","@testforme");//o
           map.put("gender","女");//o
           map.put("profileimg","img_addr");
           String param = GsonUtils.toJson(map);
           String accessToken="0";
           String ss=getRequsetData(path,param);
           System.out.println(ss);
			/*
			 * if(ss.contains("<!doctype html>")) System.out.println("用户名重复");
			 */
           JSONObject JS = JSONObject.fromObject(ss);
           String msg = JS.getString("success");
           
           //JsonObject jsonObject1 = (JsonObject) new JsonParser().parse(ss).getAsJsonObject();
   		//String userName = HttpServletRequestUtil.getString(request, "userName");
   		// 获取输入的密码
          // String suc= jsonObject1.get("success").getAsString();
           if(msg.contentEquals("true"))System.out.println("true");
      }catch (Exception e) {
              e.printStackTrace();
          }
  	}
     
}

