package com.util;
/**
 * 微信令牌
 * @author liyan
 *
 */
public class AccessToken {
	
	private String access_token;
	private int expires_in; 
	private long create_time;
	
	public AccessToken(){
		
	}
	
	public AccessToken(String access_token,int expires_in,long create_time){
		this.access_token = access_token;
		this.expires_in = expires_in;
		this.create_time = create_time;
	}
	
	/**
	 * 是否过期
	 * @return
	 */
	public boolean isExpires(){
		if(System.currentTimeMillis()-create_time<expires_in*1000-900){   //7200秒-900秒   提前15分钟需要重新获取
			return false;
		}else{
			return true;
		}
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

}
