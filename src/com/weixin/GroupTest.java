package com.weixin;

import com.util.UrlUtil;
import com.util.WxInfoUtil;

public class GroupTest {
	
	public static void main(String args[]){
		String url = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token="+WxInfoUtil.getInstance().getAccessToken().getAccess_token();
		
		String outputStr = "{\"openid\":\"o0sIIt23b_ANoRGk8UkdCKcnJPQY\"}";
		System.out.println(UrlUtil.httpsRequest(url, "POST", outputStr));

//		String url = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token="+WxInfoUtil.getInstance().getAccessToken().getAccess_token();
//		System.out.println(UrlUtil.httpsRequest(url, "GET", ""));
	}

}
