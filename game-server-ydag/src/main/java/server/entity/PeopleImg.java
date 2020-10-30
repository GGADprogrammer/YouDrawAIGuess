package server.entity;

import java.util.Date;

/**
 * 用户图画实体类
 * 
 * @author litong song
 */
public class PeopleImg {
	// 主键ID
	private Long peopleImgId;
	// 图片地址
	private String imgAddr;
	// 图片简介
	private String imgDesc;
	// 权重，越大越排前显示
	private Integer priority;
	// 创建时间
	private Date createTime;
	// 标明是属于哪个用户的图片
	private Long localAuthId;
	public Long getPeopleImgId() {
		return peopleImgId;
	}
	public void setPeopleImgId(Long peopleImgId) {
		this.peopleImgId = peopleImgId;
	}
	public String getImgAddr() {
		return imgAddr;
	}
	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}
	public String getImgDesc() {
		return imgDesc;
	}
	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getLocalAuthId() {
		return localAuthId;
	}
	public void setLocalAuthId(Long localAuthId) {
		this.localAuthId = localAuthId;
	}

	

}
