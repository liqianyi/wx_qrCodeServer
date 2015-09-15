package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
/**
 * 微信基本信息单例类
 * @author liyan
 *
 */
public class WxInfoUtil {
	
	private static WxInfoUtil instance = new WxInfoUtil();
	
	private Properties p;
	private String appId ;
	private String secret;
	private AccessToken accessToken;
	
	private WxInfoUtil() {
		try {
			FileInputStream fis = new FileInputStream(new File(WxInfoUtil.class.getResource("/info.properties").getFile()));
			p = new Properties();
			p.load(fis);
			appId = p.getProperty("APPID");
			secret = p.getProperty("SECRET");
			int expires_in = 0;
			long create_time = 0;
			if(p.getProperty("expires_in") != null){
				expires_in = Integer.parseInt(p.getProperty("expires_in"));
			}
			if(p.getProperty("create_time") != null){
				create_time = Long.parseLong(p.getProperty("create_time"));
			}
			String access_token = p.getProperty("access_token");
			accessToken = new AccessToken(access_token,expires_in,create_time);
			FileMonitor monitor = new FileMonitor();
			monitor.process();
			Thread t = new Thread(monitor);
			t.start();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static WxInfoUtil getInstance() {
		return instance;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}



	class FileMonitor implements Runnable {

		private void process(){
			if(accessToken.getAccess_token()==null || accessToken.isExpires()){
				try {
					String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
							+ appId + "&secret=" + secret;
					String json = UrlUtil.httpsRequest(url, "GET", null);
					accessToken = CommonUtils.parseJson(json, AccessToken.class);
					accessToken.setCreate_time(System.currentTimeMillis());
					p.setProperty("access_token", accessToken.getAccess_token());
					p.setProperty("expires_in",accessToken.getExpires_in()+"");
					p.setProperty("create_time", accessToken.getCreate_time()+"");
					OutputStream fos = new FileOutputStream(new File(WxInfoUtil.class.getResource("/info.properties").getFile()));     
					p.store(fos, "update accessToken");
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		@Override
		public void run() {
			while (true) {
				process();
				try {
					Thread.sleep(900000); // 15分钟
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		
		
	}
}
