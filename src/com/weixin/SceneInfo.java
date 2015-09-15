package com.weixin;

import java.util.Date;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 场景记录
 * @author liyan
 *
 */
public class SceneInfo implements Comparable<SceneInfo>{
	//保存操作日志
	public static ConcurrentSkipListSet<SceneInfo> logInfo = new ConcurrentSkipListSet<SceneInfo>();
	
	private String sceneId;	
	private String openId;	//用户id
	private Date creatTime;	//当前时间
	private String groupId;	//分组
	private String operate;	//具体操作  scan 浏览  ，subscribe 关注
	
	public String getSceneId() {
		return sceneId;
	}
	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getOperate() {
		return operate;
	}
	public void setOperate(String operate) {
		this.operate = operate;
	}
	/**
	 * 保存操作日志
	 */
	public void save(){
		logInfo.add(this);
	}
	@Override
	public int compareTo(SceneInfo o) {
		Date c = o.creatTime;
		return creatTime.after(c)?1:(creatTime.before(c)?-1:0);
	}
	
	public String toString(){
		return "SceneInfo [ sceneId :"+sceneId+",openId : "+openId+", creatTime: "+creatTime+", groupId: "+groupId+", operate : "+operate+" ]";
	}
	
}
