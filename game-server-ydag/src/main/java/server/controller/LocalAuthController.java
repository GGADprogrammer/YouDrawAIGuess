package server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import server.dto.ImageHolder;
import server.dto.LocalAuthExecution;
import server.dto.PeopleExecution;
import server.entity.LocalAuth;
import server.entity.PeopleImg;
import server.entity.PersonInfo;
import server.enums.LocalAuthStateEnum;
import server.enums.PeopleStateEnum;
import server.exceptions.LocalAuthOperationException;
import server.exceptions.ProductOperationException;
import server.service.LocalAuthService;
import server.service.PeopleService;
import server.service.PersonInfoService;
import server.util.CodeUtil;
import server.util.HttpServletRequestUtil;

@Controller
@RequestMapping(value = "local", method = { RequestMethod.GET, RequestMethod.POST })
public class LocalAuthController {
	@Autowired
	private LocalAuthService localAuthService;
	@Autowired
	private PeopleService peopleService;
    @Autowired
    private PersonInfoService personInfoService;
	@RequestMapping(value = "/bindlocalauth", method = RequestMethod.POST)
	@ResponseBody
	/**
	 * 将用户信息与平台帐号绑定
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		System.out.println("in");
		// 获取输入的帐号
		BufferedReader reader = null;
//        StringBuilder sb = new StringBuilder();
//        try{
//            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
//            String line = null;
//            while ((line = reader.readLine()) != null){
//                sb.append(line);
//            }
//        } catch (IOException e){
//            e.printStackTrace();
//        } finally {
//            try{
//                if (null != reader){ reader.close();
//                }
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//        }
        //System.out.println("json;"+sb.toString());
//        Gson gson = new Gson();
//        PersonInfo user1 = gson.fromJson(sb.toString(), PersonInfo.class);
//        JsonObject jsonObject1 = (JsonObject) new JsonParser().parse(sb.toString()).getAsJsonObject();
		//String userName = HttpServletRequestUtil.getString(request, "userName");
		PersonInfo user1=new PersonInfo();
		user1.setName(HttpServletRequestUtil.getString(request, "name"));
		user1.setEmail(HttpServletRequestUtil.getString(request, "email"));
		user1.setGender(HttpServletRequestUtil.getString(request, "gender"));
		user1.setProfileImg(HttpServletRequestUtil.getString(request, "profileimg"));
		// 获取输入的密码
		String userName= HttpServletRequestUtil.getString(request, "userName");
        //String userName= jsonObject1.get("userName").getAsString();
		System.out.println(userName);
		//String password= jsonObject1.get("password").getAsString();
		String password= HttpServletRequestUtil.getString(request, "password");
		//String password = HttpServletRequestUtil.getString(request, "password");
		System.out.println(password);
		Date d=new Date();
		user1.setCreateTime(d);
		user1.setUserType(1);
		user1.setEnableStatus(1);
		/*
		 * user1.setGender(HttpServletRequestUtil.getString(request, "gender"));
		 * user1.setProfileImg(HttpServletRequestUtil.getString(request, "profileImg"));
		 */
		//System.out.println(user1.getUserId());
		modelMap.put("errMsg5:运行插入结果", personInfoService.insertPersonInfo(user1));
		//user1.getCreateTime()
		//PersonInfo user=personInfoService.getPersonInfoByCreateTime(d);
		PersonInfo user=personInfoService.getPersonInfoById(user1.getUserId());
		System.out.println(user);
		//modelMap.put("errMsg4试图直接获取id",user1.getUserId());
		//modelMap.put("errMsg5查询结果",user);
		// 非空判断，要求帐号密码以及当前的用户session非空
		if (userName != null && password != null && user != null && user.getUserId() != null) {
			// 创建LocalAuth对象并赋值
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUsername(userName);
			localAuth.setPassword(password);
			localAuth.setPersonInfo(user);
			// 绑定帐号
			LocalAuthExecution le = localAuthService.bindLocalAuth(localAuth);
			if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", le.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}
//
//	@RequestMapping(value = "/changelocalpwd", method = RequestMethod.POST)
//	@ResponseBody
//	/**
//	 * 修改密码
//	 * 
//	 * @param request
//	 * @return
//	 */
//	private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
//		Map<String, Object> modelMap = new HashMap<String, Object>();
//		// 获取帐号
//		String userName = HttpServletRequestUtil.getString(request, "userName");
//		// 获取原密码
//		String password = HttpServletRequestUtil.getString(request, "password");
//		// 获取新密码
//		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
//		// 从session中获取当前用户信息(用户一旦通过微信登录之后，便能获取到用户的信息)
//		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
//		// 非空判断，要求帐号新旧密码以及当前的用户session非空，且新旧密码不相同
//		if (userName != null && password != null && newPassword != null && user != null && user.getUserId() != null
//				&& !password.equals(newPassword)) {
//			try {
//				// 查看原先帐号，看看与输入的帐号是否一致，不一致则认为是非法操作
//				LocalAuth localAuth = localAuthService.getLocalAuthByUserId(user.getUserId());
//				if (localAuth == null || !localAuth.getUsername().equals(userName)) {
//					// 不一致则直接退出
//					modelMap.put("success", false);
//					modelMap.put("errMsg", "输入的帐号非本次登录的帐号");
//					return modelMap;
//				}
//				// 修改平台帐号的用户密码
//				LocalAuthExecution le = localAuthService.modifyLocalAuth(user.getUserId(), userName, password,
//						newPassword);
//				if (le.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
//					modelMap.put("success", true);
//				} else {
//					modelMap.put("success", false);
//					modelMap.put("errMsg", le.getStateInfo());
//				}
//			} catch (LocalAuthOperationException e) {
//				modelMap.put("success", false);
//				modelMap.put("errMsg", e.toString());
//				return modelMap;
//			}
//
//		} else {
//			modelMap.put("success", false);
//			modelMap.put("errMsg", "请输入密码");
//		}
//		return modelMap;
//	}

	@RequestMapping(value = "/logincheck", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> logincheck(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		/*
		 * // 获取是否需要进行验证码校验的标识符 boolean needVerify =
		 * HttpServletRequestUtil.getBoolean(request, "needVerify"); if (needVerify &&
		 * !CodeUtil.checkVerifyCode(request)) { modelMap.put("success", false);
		 * modelMap.put("errMsg", "输入了错误的验证码"); return modelMap; }
		 */
		// 获取输入的帐号
		String userName = HttpServletRequestUtil.getString(request, "userName");
		// 获取输入的密码
		String password = HttpServletRequestUtil.getString(request, "password");
		// 非空校验
		System.out.println("controller得到:"+userName);
		if (userName != null && password != null) {
			// 传入帐号和密码去获取平台帐号信息
			LocalAuth localAuth = localAuthService.getLocalAuthByUsernameAndPwd(userName, password);
			if (localAuth != null) {
				// 若能取到帐号信息则登录成功
				modelMap.put("success", true);
				// 同时在session里设置用户信息
				modelMap.put("localAuthId",localAuth.getLocalAuthId());
				request.getSession().setAttribute("user", localAuth.getPersonInfo());
				//根据这个可以查询出用户的图片
				request.getSession().setAttribute("localAuthId", localAuth.getLocalAuthId());
				//modelMap.put("SESSION",request.getSession().getId());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码错误");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}
	//WEISHIYONG
	@RequestMapping(value = "/addimage", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addImage(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//modelMap.put("request:", request.toString());
		// 接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
		ObjectMapper mapper = new ObjectMapper();
		PeopleImg pi = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> peopleImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		modelMap.put("successin", "inthumbnail");
		try {
			// 若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
			if (multipartResolver.isMultipart(request)) {
			//if(1==1) {
				modelMap.put("successin", "inthumbnail");
				//thumbnail = handleImage(request, thumbnail, peopleImgList);
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				//modelMap.put("thum",multipartRequest);
				// 取出缩略图并构建ImageHolder对象
				CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
				//modelMap.put("thumbnail1FILE",thumbnailFile.getOriginalFilename());
				if (thumbnailFile != null) {
					modelMap.put("thumbnail1","thumbnailIN");
					//modelMap.put("thumbnail11",thumbnailFile);
					//modelMap.put("thumbnailINSTREAM",thumbnailFile.getInputStream());
					thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
				}
				//modelMap.put("thumbnail1",thumbnail);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
//		try {
//			String productStr = HttpServletRequestUtil.getString(request, "productStr");
//			// 尝试获取前端传过来的表单string流并将其转换成Product实体类
//			product = mapper.readValue(productStr, Product.class);
//		} catch (Exception e) {
//			modelMap.put("success", false);
//			modelMap.put("errMsg", e.toString());
//			return modelMap;
//		}
		// 若Product信息，缩略图以及详情图列表为非空，则开始进行商品添加操作
		//product != null && &&peopleImgList.size() > 0
		if (thumbnail != null) {
			try {
				// 从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
				 Long currentLocalAuthId = (Long) request.getSession().getAttribute("localAuthId");
				 modelMap.put("SessionID", request.getSession().getId());
				 PeopleImg peopleImg=new PeopleImg();
				 peopleImg.setLocalAuthId(currentLocalAuthId);
				//product.setShop(currentShop);
				// 执行添加操作
				 modelMap.put("PI",peopleImg);
				PeopleExecution pe = peopleService.addImg(peopleImg,thumbnail);
				if (pe.getState() == PeopleStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;
	}
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	@ResponseBody
	/**
	 * 当用户点击登出按钮的时候注销session
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private Map<String, Object> logout(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 将用户session置为空
		request.getSession().setAttribute("user", null);
		modelMap.put("success", true);
		return modelMap;
	}
}
