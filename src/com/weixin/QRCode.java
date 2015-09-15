package com.weixin;

import java.io.IOException;

import com.util.CommonUtils;
import com.util.UrlUtil;
import com.util.WxInfoUtil;
/**
 * 生成二维码
 * @author liyan
 *
 */
public class QRCode {
	/**
	 * 获取二维码Ticket
	 * @param scene_id 场景id
	 * @param group_id 分组id
	 * @return
	 */
	private TicketInfo getTicket(String scene_id,String group_id){
		WxInfoUtil wxInfoUtil = WxInfoUtil.getInstance();
		String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+wxInfoUtil.getAccessToken().getAccess_token();
		String outputStr = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+scene_id+":::"+group_id+"\"}}}";
		String result = UrlUtil.httpsRequest(url, "POST", outputStr);
		return CommonUtils.parseJson(result, TicketInfo.class);
	}
	/**
	 * 生成二维码
	 * @param scene_id 场景id
	 * @param group_id 分组id
	 * @return 图片地址
	 * @throws IOException
	 */
	public String getQRImage(String scene_id,String group_id,String path,String fileName) throws IOException{
		TicketInfo ticketInfo = getTicket(scene_id,group_id);
		String url = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticketInfo.getTicket();
		UrlUtil.downLoadFile(url,path,fileName);
		return path+fileName;
	}
	
	public static void main(String args[]) throws IOException{
		QRCode code = new QRCode();
		code.getQRImage("scene1","2","d:/","qrCode.jpg");
	}
}
