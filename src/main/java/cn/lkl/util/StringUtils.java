package cn.lkl.util;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		return !isNotEmpty(str);
	}
	
	/**
	 * 判断字符串是否为非空的
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str){
		return str!=null && !str.equals("");
	}
	
	/**
	 * 返回UUID
	 * @return
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 根据属性名称返回set方法名称
	 * @param fieldName
	 * @return
	 */
	public static String getSetMethodName(String attrName){
		String setMethodName = "set"
				+ attrName.substring(0, 1).toUpperCase()
				+ attrName.substring(1);
		return setMethodName;
	}
	
	/**
	 * 根据属性名称返回get方法名称
	 * @param fieldName
	 * @return
	 */
	public static String getGetMethodName(String attrName){
		String getMethodName = "get"
				+ attrName.substring(0, 1).toUpperCase()
				+ attrName.substring(1);
		return getMethodName;
	}
	
	/**
	 * 根据set或get方法名称返回属性名称
	 * @param fieldName
	 * @return
	 */
	public static String getAttrName(String methodName){
		String attrName = methodName.substring(3, 4).toLowerCase()
				+ methodName.substring(4);
		return attrName;
	}
	
	/**
	 * 将字符串转换为Integer
	 * @param str 要转换的字符串
	 * @param defaultVal 转换失败后的默认值
	 * @return
	 */
	public static Integer stringToInteger(String str,Integer defaultVal){
		if(isNotEmpty(str)){
			try{
				return Integer.parseInt(str);
			}catch(Exception e){}
		}
		return defaultVal;
	}
	
	/**
	 * 将字符串转换为Double
	 * @param str 要转换的字符串
	 * @param defaultVal 转换失败后的默认值
	 * @return
	 */
	public static Double stringToDouble(String str,Double defaultVal){
		if(isNotEmpty(str)){
			try{
				return Double.parseDouble(str);
			}catch(Exception e){}
		}
		return defaultVal;
	}
	
	/**
	 * 将字符串转换为Boolean
	 * @param str 要转换的字符串
	 * @param defaultVal 转换失败后的默认值
	 * @return
	 */
	public static Boolean stringToBoolean(String str,Boolean defaultVal){
		if(isNotEmpty(str)){
			try{
				return Boolean.parseBoolean(str);
			}catch(Exception e){}
		}
		return defaultVal;
	}
	
	/**
	 * 将数组以指定分隔符拼接成字符串
	 * @param arr
	 * @param splitChar
	 * @return
	 */
	public static <T> String join(T[] arr,String splitChar){
		if(arr==null || arr.length==0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<arr.length;i++){
			builder.append(arr[i]);
			if(i<arr.length-1){
				builder.append(splitChar);
			}
		}
		return builder.toString();
	}
	
	/**
	 * 将集合以指定分隔符拼接成字符串
	 * @param arr
	 * @param splitChar
	 * @return
	 */
	public static String join(Collection<String> list,String splitChar){
		if(list==null || list.size()==0){
			return "";
		}
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for(String str : list){
			builder.append(str);
			if(i<list.size()-1){
				builder.append(splitChar);
			}
			i++;
		}
		return builder.toString();
	}
	
	/**
	 * 获得java字符串转义字符(所有\变为\\,所有"变为\")
	 * @param str
	 * @return
	 */
	public static String getJavaEscapeString(String str){
		return str.replace("\\", "\\\\").replace("\"", "\\\"");
	}
	
	/**
	 * 获得html字符串转义字符(所有<变为&lt;,所有>变为&gt;)
	 * @param str
	 * @param isReplSpace
	 * @return
	 */
	public static String getHtmlEscapeString(String str,boolean isReplSpace){
		str = str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
		if(isReplSpace){
			str = str.replace(" ", "&nbsp;");
		}
		return str;
	}
	
	/**
	 * 获得正则转义字符(\{[()
	 * @param str
	 * @return
	 */
	public static String getRegexEscapeString(String str){
		//将特殊字符转义 \ { } ( ) . * + ? $ | [ ] ^ 
		return str.replaceAll("([\\\\{}().*+?$|\\[\\]\\^])", "\\\\$1");
	}
	
	
	
	/**
	 * 将字符串后面添加空格到指定长度(最少要有10个空格)
	 * @param str
	 * @param allLength
	 * @return
	 */
	public static String fillSpace(String str,int allLength){
		if(str.length()>allLength-10){
			str = str.substring(0, allLength-10);
		}
		int c = allLength - str.length();
		String kongbai = "                                                  ";
		kongbai = kongbai + kongbai + kongbai + kongbai + kongbai;
		return str + kongbai.substring(0,c);
	}
	
	/**
	 * 将字符串后面添加空格到指定长度(最少要有10个空格)
	 * @param strArr
	 * @param allLengthArr
	 * @return
	 */
	public static String fillSpace(String[] strArr,int[] allLengthArr){
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<strArr.length;i++){
			builder.append(fillSpace(strArr[i],allLengthArr[i]));
		}
		return builder.toString();
	}
	
	/**
	 * 返回子字符串在字符串中出现的次数
	 * @param str
	 * @param subStr
	 * @return
	 */
	public static int getSubstrExistsCount(String str,String subStr){
		int count = 0;
		int index = -1;
		while( (index = str.indexOf(subStr,index+1))!=-1){
			count++;
		}
		return count;
	}
	
	/**
	 * @描述：首字母大写
	 * @开发人员：likaihao
	 * @开发时间：2015年9月9日 上午10:31:58
	 * @param str
	 * @return
	 */
	public static String firstCharUpperCase(String str){
		if(str==null || str.length()==0){
			return "";
		}
		return str.substring(0,1).toUpperCase()+str.substring(1);
	}
	
	/**
	 * @描述：首字母小写
	 * @开发人员：likaihao
	 * @开发时间：2015年9月9日 上午10:31:58
	 * @param str
	 * @return
	 */
	public static String firstCharLowerCase(String str){
		if(str==null || str.length()==0){
			return "";
		}
		return str.substring(0,1).toLowerCase()+str.substring(1);
	}
	
	/**
	 * 汉字转换为unicode
	 * @param inStr
	 * @return
	 */
	public static String stringToUnicode(String inStr) {
		char[] myBuffer = inStr.toCharArray();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < inStr.length(); i++) {
			UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
			if (ub == UnicodeBlock.BASIC_LATIN) {
				sb.append(myBuffer[i]);
			} else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				int j = (int) myBuffer[i] - 65248;
				sb.append((char) j);
			} else {
				short s = (short) myBuffer[i];
				String hexS = Integer.toHexString(s);
				if (hexS.length() > 4) {// 去掉前面多余的f,固定长度为4个,李凯昊添加
					hexS = hexS.substring(hexS.length() - 4);
				}
				String unicode = "\\u" + hexS;
				sb.append(unicode.toLowerCase());
			}
		}
		return sb.toString();
	}

	/**
	 * unicode转换为汉字
	 * @param theString
	 * @return
	 */
	public static String unicodeToString(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// "\\uxxxx"的格式
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						if (aChar >= 48 && aChar <= 57) {
							// 0-9
							value = (value << 4) + aChar - '0';
						} else if (aChar >= 97 && aChar <= 102) {
							// a-f
							value = (value << 4) + 10 + aChar - 'a';
						} else if (aChar >= 65 && aChar <= 70) {
							// A-F
							value = (value << 4) + 10 + aChar - 'A';
						} else {
							throw new IllegalArgumentException(
									"格式为:\\uxxxx,出现非法字符:" + aChar + ",附近字符:"
											+ theString.substring(x - 4, x + 4));
						}
					}
					outBuffer.append((char) value);
				} else {
					// 这里不需要转义\\ \t什么的,李凯昊添加
					outBuffer.append("\\").append(aChar);
				}
			} else {
				// 不以\\开头
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}
	
	/**
	 * @描述：urlencoding
	 * @开发人员：likaihao
	 * @开发时间：2015年9月18日 上午9:58:42
	 * @param str
	 * @return
	 */
	public static String urlEncoding(String str){
		try {
			return URLEncoder.encode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @描述：urldecoding
	 * @开发人员：likaihao
	 * @开发时间：2015年9月18日 上午9:58:42
	 * @param str
	 * @return
	 */
	public static String urlDecoding(String str){
		try {
			return URLDecoder.decode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @描述：urlencoding分段
	 * @开发人员：likaihao
	 * @开发时间：2015年9月18日 上午9:58:42
	 * @param str
	 * @return
	 */
	public static String urlEncoding_sub(String url){
		String host = null;
		String params = null;
		int index = url.indexOf("?");
		if(index!=-1){
			host = url.substring(0,index);
		}
		params = url.substring(index+1);
		
		//对参数分段urlencoding
		StringBuilder builder = new StringBuilder();
		String[] arr = params.split("&");
		for(String namevalue : arr){
			String[] arr2 = namevalue.split("=",2);
			builder.append(StringUtils.urlEncoding(arr2[0]));
			builder.append("=");
			builder.append(StringUtils.urlEncoding(arr2[1]));
			builder.append("&");
		}
		if(builder.length()>0){
			builder.deleteCharAt(builder.length()-1);
		}
		
		String result = "";
		if(host!=null){
			result = host + "?";
		}
		result += builder.toString();
		return result;
	}
	
	/**
	 * @描述：urldecoding分段
	 * @开发人员：likaihao
	 * @开发时间：2015年9月18日 上午9:58:42
	 * @param str
	 * @return
	 */
	public static String urlDecoding_sub(String url){
		String host = null;
		String params = null;
		int index = url.indexOf("?");
		if(index!=-1){
			host = url.substring(0,index);
		}
		params = url.substring(index+1);
		
		//对参数分段urlencoding
		StringBuilder builder = new StringBuilder();
		String[] arr = params.split("&");
		for(String namevalue : arr){
			String[] arr2 = namevalue.split("=",2);
			builder.append(StringUtils.urlDecoding(arr2[0]));
			builder.append("=");
			builder.append(StringUtils.urlDecoding(arr2[1]));
			builder.append("&");
		}
		if(builder.length()>0){
			builder.deleteCharAt(builder.length()-1);
		}
		
		String result = "";
		if(host!=null){
			result = host + "?";
		}
		result += builder.toString();
		return result;
	}
	
	/**
	 * @描述：将字节数组转换为字符串(为js准备的...)
	 * @开发人员：likaihao
	 * @开发时间：2015年11月16日 下午6:18:19
	 * @param bytes
	 * @return
	 */
	public static String byteToString(byte[] bytes){
		try {
			return new String(bytes,"utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * @描述：将字符串转换为字节数组(为js准备的...)
	 * @开发人员：likaihao
	 * @开发时间：2015年11月16日 下午6:18:19
	 * @param str
	 * @return
	 */
	public static byte[] stringToByte(String str){
		try {
			return str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * @描述：将url参数分割为map
	 * @开发人员：likaihao
	 * @开发时间：2015年12月9日 上午11:56:59
	 * @param params
	 * @return
	 */
	public static Map<String,String> splitUrlParamToMap(String params){
		Map<String,String> map = new HashMap<String,String>();
		String[] arr = params.split("&");
		for(String nameValueStr : arr){
			String[] arr2 = nameValueStr.split("=",2);
			map.put(arr2[0], arr2[1]);
		}
		return map;
	}
	
	/**
	 * @描述：将map拼装成url参数的形式
	 * @开发人员：likaihao
	 * @开发时间：2015年12月9日 上午9:49:02
	 * @param map 要拼装的参数
	 * @param allowEmptyStr 空字符串是否要拼接
	 * @param isUrlEncoding 参数值拼装前是否进行url编码
	 * @return
	 */
	public static String joinMapToUrlParam(Map<String,String> map, boolean allowEmptyStr, boolean isUrlEncoding){
		try {
			StringBuilder builder = new StringBuilder();
			for(String key : map.keySet()){
				String value = map.get(key);
				if(value!=null && (allowEmptyStr || value.length()>0)){
					if(isUrlEncoding){
						value = URLEncoder.encode(value, "utf-8");
					}
					builder.append(key+"="+value+"&");
				}
			}
			if(builder.length()>0){
				builder.deleteCharAt(builder.length()-1);
			}
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 返回格式化的java代码(本来存在换行,现在要添加\t)
	 * @param codeStr
	 */
	public static String getFormatJavaCode(String codeStr) {
		//结尾是{  				下一行添加一个\t
		//开头是}结尾不是{  		本行前减少一个\t
		//开头是}结尾是{			本行前减少一个\t,下一行添加一个\t
		//要判断{}是不是出现在字符串中 	判断后面的"数是否是偶数
		
		String[] arr = codeStr.split("\r?\n");
		String tStr = "";
		StringBuilder builder = new StringBuilder();
		Pattern p = Pattern.compile("((?:\\{|\\}.*?)\\s*$)");
		for(String lineStr : arr){
			//LoggerUtils.info(lineStr);
			Matcher m = p.matcher(lineStr);
			if(m.find()){
				String matchStr = m.group(1);
				if(matchStr.startsWith("{")){
					builder.append(tStr+lineStr+"\r\n");//在本行后添加\t
					tStr += "\t";
				}else if(matchStr.startsWith("}")){
					if(StringUtils.getSubstrExistsCount(matchStr, "\"")%2==0){//如果不是偶数,说明在字符串中,不起缩进作用
						//在本行前减少\t
						if(tStr.length()>=1){
							tStr = tStr.substring(1);
						}else{
							tStr = "";
						}
						builder.append(tStr+lineStr+"\r\n");
						//如果是}.*?{ 则在下一行添加\t
						if(matchStr.endsWith("{")){
							tStr += "\t";
						}
					}
				}
			}else{
				builder.append(tStr+lineStr+"\r\n");
			}
		}
		return builder.toString();
	}
	
	/**
	 * @描述：获得随机串
	 * @开发人员：likaihao
	 * @开发时间：2016年5月7日 下午12:02:34
	 * @return
	 */
    public static String getRandomStr(){
    	SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String randomStr = (int)(Math.random()*1000)+"";
		randomStr = "000".substring(randomStr.length()) + randomStr;
    	return formate.format(new Date())+randomStr;
    }
    
    private static Integer tempNum = 0;
    
    /**
	 * @描述：获得4位短期不重复的随机串
	 * @开发人员：likaihao
	 * @开发时间：2016年5月7日 下午12:02:34
	 * @return
	 */
    public static String getTempRandomStr(){
    	String randomStr = getTempRandomInteger() + "";
		randomStr = "0000".substring(randomStr.length()) + randomStr;
    	return randomStr;
    }
    
    /**
	 * @描述：获得4位短期不重复的随机串
	 * @开发人员：likaihao
	 * @开发时间：2016年5月7日 下午12:02:34
	 * @return
	 */
    public static Integer getTempRandomInteger(){
    	tempNum ++;
    	if(tempNum>9999){
    		tempNum = 1;
    	}
    	return tempNum;
    }
}
