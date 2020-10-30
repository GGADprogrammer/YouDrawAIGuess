package server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

//import com.imooc.o2o.entity.Product;

import server.entity.LocalAuth;
import server.entity.PeopleImg;

public interface PeopleImgDao {

	/**
	 * 列出某个人的详情图列表
	 * 
	 * @param productId
	 * @return
	 */
	List<PeopleImg> queryPeopleImgList(long localAuthId);
	
	List<PeopleImg> queryPeopleImgListForIndex(@Param("localAuthCondition") LocalAuth localAuthCondition, @Param("rowIndex") int rowIndex,
			@Param("pageSize") int pageSize);
	/**
	 * 批量添加商品详情图片
	 * 
	 * @param productImgList
	 * @return
	 *//*
		 * int batchInsertPeopleImg(List<PeopleImg> peopleImgList);
		 */
	int insertImg(List<PeopleImg> peopleImgList);
	/**
	 * 删除指定商品下的所有详情图
	 * 
	 * @param productId
	 * @return
	 */
	int deletePeopleImgByLocalAuthId(long localAuthId);
	int queryPeopleImgCount(@Param("localAuthCondition") LocalAuth localAuthCondition);
}
