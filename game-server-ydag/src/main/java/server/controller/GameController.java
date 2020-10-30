package server.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import server.dto.PeopleExecution;
import server.entity.LocalAuth;
import server.entity.PeopleImg;
import server.service.PeopleService;
import server.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "game", method = { RequestMethod.GET, RequestMethod.POST })
public class GameController {
	@Autowired
	private PeopleService peopleService;
	private String topiccurrent="";
	private Vector<String> ve=new Vector<String>(); 
	public void add() {
		//fake
	ve.add("hourse");
	ve.add("orange");
	ve.add("rectangle");
	//true
	ve.add("apple"); 
	ve.add("book"); 
	ve.add("bowtie"); 
	ve.add("candle"); 
	ve.add("cloud");
	ve.add("cup"); 
	ve.add("door"); 
	ve.add("envelope");
	ve.add("eyeglasses");
	ve.add("guitar");
	ve.add("hammer");
	ve.add("hat");
    ve.add("ice cream");
    ve.add("leaf");
    ve.add("scissors");
    ve.add("star");
    ve.add("t-shirt");
    ve.add("pants");
    ve.add("lightning");
    ve.add("tree");
	}
	@RequestMapping(value = "/gamestart", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> topicchoose(HttpServletRequest request) {
		add();
		Map<String, Object> modelMap = new HashMap<String, Object>();
	    modelMap.put("success", true);
	    Random rand = new Random();
	    int i=rand.nextInt(ve.size());
	    topiccurrent=ve.elementAt(i);
	    modelMap.put("topic",topiccurrent);
		return modelMap;
	}
	@RequestMapping(value = "/gamejudge", method = RequestMethod.GET)
	@ResponseBody
	//url传入localAuthId
	private Map<String, Object> judge(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 获取输入的帐号id
		Long localAuthId = HttpServletRequestUtil.getLong(request, "localAuthId");
		LocalAuth la=new LocalAuth();
		la.setLocalAuthId(localAuthId);
		//根据id找到他的全部图片
		PeopleExecution pe=peopleService.getPeopleImgList(la,0,100);
		List<PeopleImg> li=pe.getPeopleImgList();
		//取出最新图片
		PeopleImg peopleImg1=li.get(0);
		String basePath = "D:/projectdev/image";
        String seperator = System.getProperty("file.separator");
		String absolutepath=basePath+peopleImg1.getImgAddr();
		String new1=absolutepath.replaceAll("\\\\","/");
		//.replace("\",seperator);
		String path="http://localhost:12000/"+new1;
		System.out.println(path);
		String str1="";
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str;
			while ((str = br.readLine()) != null) {
				str1 += new String(str.getBytes(), "UTF-8");
				System.out.println(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    //TO-DO 调用model
		modelMap.put("answer", str1);
		modelMap.put("success", true);
	    modelMap.put("IMGnewest",peopleImg1);
	    modelMap.put("localAuthId",localAuthId);
		return modelMap;
	}
}
