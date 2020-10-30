package server.service.impl;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.dao.PersonInfoDao;
import server.entity.PersonInfo;
import server.service.PersonInfoService;

@Service
public class PersonInfoServiceImpl implements PersonInfoService {
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public PersonInfo getPersonInfoById(Long userId) {
		return personInfoDao.queryPersonInfoById(userId);
	}

	@Override
	public int insertPersonInfo(PersonInfo personInfo) {
	    int i= personInfoDao.insertPersonInfo(personInfo);
		return i;
	}

	@Override
	public PersonInfo getPersonInfoByCreateTime(Date date) {
		
		return personInfoDao.queryPersonInfoByCreateTime(date);
	}
}
