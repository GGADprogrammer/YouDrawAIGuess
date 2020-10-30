package server.service;

import java.util.List;

import server.dto.ImageHolder;
import server.dto.PeopleExecution;
import server.entity.LocalAuth;
import server.entity.PeopleImg;
import server.exceptions.ProductOperationException;

public interface PeopleService {
	/**
	 * 查询商品列表并分页，可输入的条件有： 商品名（模糊），商品状态，店铺Id,商品类别
	 * 
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	PeopleExecution getPeopleImgList(LocalAuth localAuthCondition, int pageIndex, int pageSize);
	/*
		*//**
			 * 通过商品Id查询唯一的商品信息
			 * 
			 * @param productId
			 * @return
			 *//*
				 * Product getProductById(long productId);
				 */

	/**
	 * 添加商品信息以及图片处理
	 * 
	 * @param product
	 * @param thumbnail
	 * @param productImgs
	 * @return
	 * @throws ProductOperationException 
	 */
	PeopleExecution addImg(PeopleImg peopleImg,ImageHolder thumbnail)
			throws ProductOperationException;

	/**
	 * 修改商品信息以及图片处理
	 * 
	 * @param product
	 * @param thumbnail
	 * @param productImgs
	 * @return
	 * @throws ProductOperationException
	 */
	/*
	 * PeopleExecution modifyProduct(Product product, ImageHolder thumbnail,
	 * List<ImageHolder> productImgHolderList) throws ProductOperationException;
	 */
}
