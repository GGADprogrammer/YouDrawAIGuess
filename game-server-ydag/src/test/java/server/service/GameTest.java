package server.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;

import server.BaseTest;

public class GameTest extends BaseTest{
	@Test
	public void gamejudgetest() {
	String username="testusername";//测试用户名
    //String password="testpassword";//密码
    username="newRegistertestman";
    //password="test123";
    int laid=20;
    String path="http://localhost:8080/game-server/game/gamejudge?localAuthId="+laid;
    try {
    	
    	String str;
        URL url = new URL(path);
        //URL url = new URL(URLEncoder.encode(path));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream is = conn.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((str = br.readLine()) != null) {
            str = new String(str.getBytes(), "UTF-8");
            System.out.println(str);
				/*
				 * if(str.equals("login_success")){ System.out.println("成功"); } else {
				 * System.out.println("失败"); }
				 */
        }
    }catch (Exception e) {
            e.printStackTrace();
        }
	}
}
