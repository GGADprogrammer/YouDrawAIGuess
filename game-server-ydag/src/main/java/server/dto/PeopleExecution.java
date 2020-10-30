package server.dto;

import java.util.List;

import server.entity.LocalAuth;
import server.entity.PeopleImg;
import server.enums.PeopleStateEnum;

public class PeopleExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 商品数量
	private int count;

	// 操作的product（增删改商品的时候用）
	private PeopleImg peopleImg;

	// 获取的product列表(查询商品列表的时候用)
	private List<PeopleImg> peopleImgList;

	public PeopleExecution() {
	}

	// 失败的构造器
	public PeopleExecution(PeopleStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 成功的构造器
	public PeopleExecution(PeopleStateEnum stateEnum,PeopleImg peopleImg) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.peopleImg = peopleImg;
	}

	// 成功的构造器
	public PeopleExecution(PeopleStateEnum stateEnum, List<PeopleImg> peopleImgList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.peopleImgList = peopleImgList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	
	public PeopleImg getPeopleImg() {
		return peopleImg;
	}

	public void setPeopleImg(PeopleImg peopleImg) {
		this.peopleImg = peopleImg;
	}

	public List<PeopleImg> getPeopleImgList() {
		return peopleImgList;
	}

	public void setPeopleImgList(List<PeopleImg> peopleImgList) {
		this.peopleImgList = peopleImgList;
	}

	public void setCount(int count) {
		this.count = count;
	}

	

}
