
  package server.controller;
  
  import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList; 
  import java.util.HashMap; import java.util.List; import java.util.Map;
  
  import javax.servlet.http.HttpServletRequest;
  
  import org.springframework.beans.factory.annotation.Autowired; 
  import org.springframework.stereotype.Controller; import org.springframework.web.bind.annotation.RequestMapping; 
  import org.springframework.web.bind.annotation.RequestMethod; 
  import org.springframework.web.bind.annotation.RequestParam; 
  import org.springframework.web.bind.annotation.ResponseBody; 
  import org.springframework.web.multipart.MultipartHttpServletRequest; 
  import org.springframework.web.multipart.commons.CommonsMultipartFile; 
  import org.springframework.web.multipart.commons.CommonsMultipartResolver;
  
  import com.fasterxml.jackson.databind.ObjectMapper;

import server.dto.ImageHolder; import server.dto.PeopleExecution;
import server.entity.LocalAuth;
import server.entity.PeopleImg; //import com.imooc.o2o.entity.ProductCategory;
import server.enums.PeopleStateEnum;
//import com.imooc.o2o.entity.Shop; import server.enums.PeopleStateEnum;
  import server.exceptions.ProductOperationException; 
  import server.service.PeopleService;
import server.util.Base64Util;
//import com.imooc.o2o.service.ProductCategoryService; 
  //import com.imooc.o2o.service.ProductService; 
  import server.util.CodeUtil; import server.util.HttpServletRequestUtil;
  

@Controller
@RequestMapping("/shopadmin")
public class PeopleManagementController {
	@Autowired
	private PeopleService peopleService;
	//@Autowired
	//private ProductCategoryService productCategoryService;

	// 支持上传商品详情图的最大数量
	private static final int IMAGEMAXCOUNT = 6;

	/**
	 * 通过商品id获取商品信息
	 * 
	 * @param productId
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/getimagesbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int count = HttpServletRequestUtil.getInt(request, "count");
		// 获取输入的帐号
		 Long laid = HttpServletRequestUtil.getLong(request,"localAuthId");
		 LocalAuth localAuthCondition=new LocalAuth();
		 localAuthCondition.setLocalAuthId(laid);
		// 非空判断
		 String jsonImage="";
		if (laid> 0) {
			// 获取商品信息
			PeopleExecution pe = peopleService.getPeopleImgList(localAuthCondition, 0, 100);
			modelMap.put("size",pe.getPeopleImgList().size());
			//PeopleExecution pe = peopleService.getPeopleImgList(localAuthCondition, count, 1);
			// 获取该店铺下的商品类别列表
			List<PeopleImg> peopleImgList = pe.getPeopleImgList();
				//for(int i=0;i<peopleImgList.size();i++) {
                String fileName = "D:/projectdev/image"+peopleImgList.get(count).getImgAddr();//  mn4.jpg
                fileName=fileName.replaceAll("\\\\","/");
                System.out.println(fileName);
                File file=new File(fileName);
                modelMap.put("imagename",fileName.substring(fileName.lastIndexOf("\\")+1));
                //获取输入流
                FileInputStream fis = new FileInputStream(file);
     
                //新的 byte 数组输出流，缓冲区容量1024byte
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                //缓存
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                //改变为byte[]
                byte[] data = bos.toByteArray();
                //
                bos.close();
                jsonImage=Base64Util.encode(data);
                modelMap.put("image"+count, jsonImage);
                System.out.println(count);
				//}
			modelMap.put("peopleImgList", peopleImgList.get(count));
			//modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
			//System.out.println(modelMap.get("image33"));
			System.out.println(modelMap);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty localAuthId");
		}
		return modelMap;
	}

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

		// 若Product信息，缩略图以及详情图列表为非空，则开始进行商品添加操作
		//product != null && &&peopleImgList.size() > 0
		if (thumbnail != null) {
			try {
				// 从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
				 //Long currentLocalAuthId = (Long) request.getSession().getAttribute("localAuthId");
				 //modelMap.put("SessionID", request.getSession().getId());
				Long currentLocalAuthId= HttpServletRequestUtil.getLong(request, "localAuthId");
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
}
