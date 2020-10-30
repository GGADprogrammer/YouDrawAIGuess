package server.service;
import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import server.BaseTest;
import server.dto.LocalAuthExecution;
import server.entity.LocalAuth;
import server.entity.PersonInfo;
import server.enums.WechatAuthStateEnum;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginTest extends BaseTest {
	
	@Test//static
	public  void Logintest() {
	String username="testusername";//测试用户名
    String password="testpassword";//密码
    username="newRegistertestman";
    password="test123";
    String path="http://localhost:8080/game-server/local/logincheck?userName="+username+"&password="+password;

    try {
    	
    	String str;
        URL url = new URL(path);
        //URL url = new URL(URLEncoder.encode(path));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((str = br.readLine()) != null) {
            str = new String(str.getBytes(), "UTF-8");
            
            System.out.println(str);
            if(str.equals("login_success")){
            	System.out.println("成功");
            }
            else
            {
              System.out.println("失败");
            }
        }
    }catch (Exception e) {
            e.printStackTrace();
        }
	}
}
