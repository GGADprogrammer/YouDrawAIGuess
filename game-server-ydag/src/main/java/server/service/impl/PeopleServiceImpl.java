package server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.imooc.o2o.dao.ProductDao;
import server.dao.PeopleImgDao;
import server.dto.ImageHolder;
import server.dto.PeopleExecution;
import server.entity.LocalAuth;
//import com.imooc.o2o.entity.Product;
import server.entity.PeopleImg;
import server.enums.PeopleStateEnum;
import server.exceptions.ProductOperationException;
import server.service.PeopleService;

import server.util.ImageUtil;
import server.util.PageCalculator;
import server.util.PathUtil;

@Service
public class PeopleServiceImpl implements PeopleService {
	@Autowired
	private PeopleImgDao peopleImgDao;
	//@Autowired
	//private ProductImgDao productImgDao;

	@Override
	public PeopleExecution getPeopleImgList(LocalAuth localAuthCondition, int pageIndex, int pageSize) {
		// 页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<PeopleImg> peopleImgList = peopleImgDao.queryPeopleImgListForIndex(localAuthCondition, rowIndex, pageSize);
		// 基于同样的查询条件返回该查询条件下的商品总数
		int count = peopleImgDao.queryPeopleImgCount(localAuthCondition);
		PeopleExecution pe = new PeopleExecution();
		pe.setPeopleImgList(peopleImgList);   //setProductList(peopleImgList);
		pe.setCount(count);
		return pe;
	}

	
	@Override
	@Transactional
	// 1.处理缩略图，获取缩略图相对路径并赋值给product
	// 2.往tb_product写入商品信息，获取productId
	// 3.结合productId批量处理商品详情图
	// 4.将商品详情图列表批量插入tb_product_img中
	//, List<ImageHolder> peopleImgList
	public PeopleExecution addImg(PeopleImg peopleImg,ImageHolder thumbnail)
			throws ProductOperationException {
		// 空值判断
		//PeopleImg peopleImg=new PeopleImg();
		//peopleImg.setLocalAuthId(request.getSession().getAttribute("localAuthId"););
		if (thumbnail!=null&&peopleImg != null && peopleImg.getLocalAuthId() != null) {
			// 给商品设置上默认属性
			peopleImg.setCreateTime(new Date());
			//product.setLastEditTime(new Date());
			// 默认为上架的状态
			//product.setEnableStatus(1);
			// 若商品缩略图不为空则添加
			if (thumbnail != null) {
			addThumbnail(peopleImg, thumbnail);
			}
			List<PeopleImg> li=new ArrayList<>();
			li.add(peopleImg);
			try { // 创建商品信息
				int effectedNum = peopleImgDao.insertImg(li);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建图片失败:" + e.toString());
			}
			 
			
			return new PeopleExecution(PeopleStateEnum.SUCCESS, peopleImg);
		} else {
			// 传参为空则返回空值错误信息
			return new PeopleExecution(PeopleStateEnum.EMPTY);
		}
	}

	/**
	 * 添加缩略图
	 * 
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(PeopleImg peopleImg, ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(peopleImg.getLocalAuthId());
		System.out.println(dest);
		String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		System.out.println(thumbnailAddr);
		peopleImg.setImgAddr(thumbnailAddr);
	}

	/**
	 * 批量添加图片
	 * 
	 * @param product
	 * @param productImgHolderList
	 */
	/*
	 * private void addPeopleImgList(Product product, List<ImageHolder>
	 * productImgHolderList) { // 若商品详情图不为空则添加 if (productImgHolderList != null &&
	 * productImgHolderList.size() > 0) {
	 * 
	 * // 获取图片存储路径，这里直接存放到相应店铺的文件夹底下 String dest =
	 * PathUtil.getShopImagePath(product.getShop().getShopId()); List<ProductImg>
	 * productImgList = new ArrayList<ProductImg>(); // 遍历图片一次去处理，并添加进productImg实体类里
	 * for (ImageHolder productImgHolder : productImgHolderList) { String imgAddr =
	 * ImageUtil.generateNormalImg(productImgHolder, dest); ProductImg productImg =
	 * new ProductImg(); productImg.setImgAddr(imgAddr);
	 * productImg.setProductId(product.getProductId()); productImg.setCreateTime(new
	 * Date()); productImgList.add(productImg); } // 如果确实是有图片需要添加的，就执行批量添加操作 if
	 * (productImgList.size() > 0) { try { int effectedNum =
	 * productImgDao.batchInsertProductImg(productImgList); if (effectedNum <= 0) {
	 * throw new ProductOperationException("创建商品详情图片失败"); } } catch (Exception e) {
	 * throw new ProductOperationException("创建商品详情图片失败:" + e.toString()); } } } }
	 */

	/**
	 * 删除某个商品下的所有详情图
	 * 
	 * @param productId
	 */
	private void deleteProductImgList(long localAuthId) {
		// 根据productId获取原来的图片
		List<PeopleImg> peopleImgList = peopleImgDao.queryPeopleImgList(localAuthId);
		// 干掉原来的图片
		for (PeopleImg productImg : peopleImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		// 删除数据库里原有图片的信息
		peopleImgDao.deletePeopleImgByLocalAuthId(localAuthId);//  .deleteProductImgByProductId(productId);
	}

	
}
