package server.service;
import java.util.Date;

import server.entity.PersonInfo;
public interface PersonInfoService {

	/**
	 * 根据用户Id获取personInfo信息
	 * @param userId
	 * @return
	 */
	PersonInfo getPersonInfoById(Long userId);
	PersonInfo getPersonInfoByCreateTime(Date date);
	int insertPersonInfo(PersonInfo personInfo);           
}
