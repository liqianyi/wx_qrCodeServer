<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.security.*"%>
<%@ page import="java.util.Formatter"%>
<%@ page import="com.weixin.WxService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<% 	//随机字符
	String echostr=request.getParameter("echostr");
	if(echostr!=null||"".equals(echostr)){
		//微信加密签名signature
		String signature=request.getParameter("signature");
	  	String timestamp=request.getParameter("timestamp");
		String nonce=request.getParameter("nonce");
		String str = "123456"+timestamp+nonce;
		String sign = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(str.getBytes("UTF-8"));
			Formatter formatter = new Formatter();
			for (byte b : crypt.digest()) {
				formatter.format("%02x", b);
			}
			sign = formatter.toString();
			formatter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		PrintWriter ot=response.getWriter();
		if(sign.equals(signature)){
			ot.print(echostr);
		}
		ot.close();
	}else{
		//将请求响应编码均设置为UTF-8
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//调用核心业务类接收消息处理消息echostr
		String respMessage=WxService.processRequest(request);
	
		//响应消息
	    PrintWriter ot=response.getWriter();
	 	ot.print(respMessage);
	    ot.close();
	}
%>
</body>
</html>