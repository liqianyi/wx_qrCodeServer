package com.util;

import java.io.IOException;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CommonUtils {
	
	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); // 默认的日期格式
	private static final SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final NumberFormat f = NumberFormat.getInstance();
	private static final ObjectMapper mapper = new ObjectMapper();
	
	static{
		f.setGroupingUsed(true);
		f.setMaximumFractionDigits(2);
		f.setMinimumFractionDigits(2);
	}
	
	/**
	 *取得UUID唯一字符
	 */
	public static String getUUID(){
		String s=UUID.randomUUID().toString();
		String uuid=s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
		return  uuid;
	}
	
	/**
	 * 获得日期格式字符串  格式为 ：yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date){
		return df.format(date);
	}
	
	/**
	 * 获得日期时间格式字符串  格式为 ：yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String timeToString(Date date){
		return time.format(date);
	}
	
	public static Date stringToTime(String dateStr){
		try {
			return time.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Date stringToDate(String dateStr){
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 判断数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)
	{
	    Pattern pattern = Pattern.compile("[0-9]*");
	    Matcher isNum = pattern.matcher(str);
	    if( !isNum.matches() ) {
	       return false;
	    }
	    return true;
	}
	
	/**
	 * 判定手机号
	 * @param phone
	 * @return
	 */
	public boolean checkPhone(String phone){
		String str1 = "^[1][0-9]{10,11}$";
        Pattern pattern = Pattern.compile(str1);
        Matcher matcher = pattern.matcher(phone);
       
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

	public static String RandomNum(){
        Random random = new Random(); 
        String result="";
        for(int i=0;i<6;i++){
            result+=random.nextInt(10);    
        }
		return result;
	}
	
	/**
	 * 读取参数文件的参数信息
	 * @param key
	 * @return
	 */
	public static String parseProperties(String key){
		return readProperties(key,"application");
	}
	
	public static String redisProperties(String key){
		return readProperties(key,"redis");
	}
	
	private static String readProperties(String key,String fileName){
		String returnValue = "";
		try{
			ResourceBundle rb= ResourceBundle.getBundle(fileName);
			returnValue = rb.getString(key);
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnValue;
	}
	
	public static String getRandomCode() {

		StringBuffer buf = new StringBuffer(
				"a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z");
		buf.append(",1,2,3,4,5,6,7,8,9,0");
		String[] arr = buf.toString().split(",");
		String random=getPswd(arr);
		System.out.println(random);
		return random;

	}

	private static String getPswd(String[] arr) {
		StringBuffer b = new StringBuffer();
		java.util.Random r;
		int k;
		for (int i = 0; i < 6; i++) {
			r = new java.util.Random();
			k = r.nextInt();
			b.append(String.valueOf(arr[Math.abs(k % 36)]));
		}
		return b.toString();
	}
	
	public static String numberToStr(Double number){
		if(number == null){
			return "0.00";
		}
		return f.format(number);
	}
	/**
	 * 转换json格式
	 * @param object
	 * @return
	 */
	public static String toJsonStr(Object object){
	    StringWriter str = new StringWriter();
	    try {
			mapper.writeValue(str, object);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return str.toString();
	}
	
	public static <T> T parseJson(String content,Class<T> valueType){
	    try {
			return mapper.readValue(content, valueType);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
}
