package com.weixin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.util.AccessToken;
import com.util.UrlUtil;
import com.util.WxInfoUtil;
/**
 * 微信服务事件监听
 * @author liyan
 *
 */
public class WxService {
	
	public static String processRequest(HttpServletRequest request) {
		String returnxml = "";	//返回信息
		//默认回复
		String respContent = "";
		try {
			String xml = getXmlString(request);
			//时间戳
			String nowTime = getElementText(xml, "CreateTime");
			//本帐号openId
			String myName = getElementText(xml, "ToUserName");
			//来源openId
			String sourceId = getElementText(xml, "FromUserName");
			//关注
			if("subscribe".equals(getElementText(xml, "Event"))){
				
				respContent = "您好，非常感谢您的关注";
				returnxml = "<xml> <ToUserName><![CDATA["
						+ sourceId
						+ "]]></ToUserName> <FromUserName><![CDATA["
						+ myName
						+ "]]></FromUserName> <CreateTime>"
						+ nowTime
						+ "</CreateTime> <MsgType><![CDATA[text]]></MsgType> <Content><![CDATA["
						+ respContent + "]]></Content> </xml>";
				String eventKey = getElementText(xml, "EventKey");
				if(eventKey != null){
					String str = eventKey.replace("qrscene_", "");
					String scene_id = str.split(":::")[0];	//二维码scene,渠道编号
					String group_id = str.split(":::")[1];	//业务分组
					//提交用户分组
					AccessToken accessToken = WxInfoUtil.getInstance().getAccessToken();
					String userGroupUrl = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token="+accessToken.getAccess_token();
					String outputStr = "{\"openid\":\""+sourceId+"\",\"to_groupid\":"+group_id+"}";
					UrlUtil.httpsRequest(userGroupUrl, "POST", outputStr);
					//保存操作信息
					Date date = new Date();
					date.setTime(Long.parseLong(nowTime));
					SceneInfo sceneInfo = new SceneInfo();
					sceneInfo.setSceneId(scene_id);
					sceneInfo.setOpenId(sourceId);
					sceneInfo.setGroupId(group_id);
					sceneInfo.setCreatTime(date);
					sceneInfo.setOperate("subscribe");	//关注
					sceneInfo.save();
				}
			}else if("SCAN".equals(getElementText(xml, "Event"))){
			//已经关注过，进行浏览	
				String eventKey = getElementText(xml, "EventKey");
				if(eventKey != null){
					String scene_id = eventKey.split(":::")[0];	//二维码scene,渠道编号
					String group_id = eventKey.split(":::")[1];	//业务分组
					//保存操作信息
					Date date = new Date();
					date.setTime(Long.parseLong(nowTime));
					SceneInfo sceneInfo = new SceneInfo();
					sceneInfo.setSceneId(scene_id);
					sceneInfo.setOpenId(sourceId);
					sceneInfo.setGroupId(group_id);
					sceneInfo.setCreatTime(date);
					sceneInfo.setOperate("scan");	//浏览
					sceneInfo.save();
				}
			}
			//测试用户扫描记录
//			ConcurrentSkipListSet<SceneInfo> logInfo = SceneInfo.logInfo;
//			for (SceneInfo sceneInfo : logInfo) {
//				System.out.println(sceneInfo.toString());
//			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return returnxml;
	}
	
	
	
	/**
	 * 处理xml格式字符串
	 * @param data
	 * @param elementName
	 * @return
	 */
	public static String getElementText(String data, String elementName) {
		try {
			Document document = DocumentHelper.parseText(data);
			Element root = document.getRootElement();
			Element res = (Element) root.selectSingleNode(elementName);
			if (res != null) {
				return res.getText();
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getXmlString(HttpServletRequest request)
			throws IOException {
		InputStream is = request.getInputStream();
		int size = request.getContentLength();
		byte[] buffer = new byte[size];
		is.read(buffer);
		is.close();
		String xmlDate = new String(buffer, "UTF-8");
		return xmlDate;
	}

}
