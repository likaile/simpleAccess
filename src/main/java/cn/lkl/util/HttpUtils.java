package cn.lkl.util;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;


public class HttpUtils {
	
	//默认超时时间(单位为毫秒)
	private int connectTimeout = 10000;
	private int readTimeout = 30000;
	
	//是否开启cookie
	private boolean openCookie = false;
	
	//存储cookie的map, bus365.com={username=likaihao,password=111111}
	private Map<String,Map<String,String>> cookieMap = new HashMap<String,Map<String,String>>();
	
	
	public HttpUtils(){
		
	}
	
	public HttpUtils(boolean openCookie){
		this.openCookie = openCookie;
	}

	public HttpUtils(int connectTimeout,int readTimeout,boolean openCookie){
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.openCookie = openCookie;
	}
	
	
	
	/**
	 * 发送请求
	 * @param method 请求方式
	 * @param url 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	public void send(String method,String url,Map<String,String> paramMap,Map<String,String> headerMap,boolean isUrlEncoding,OutputStream destout){
		method = method.toUpperCase();
		try {
			//get请求组织参数
			if(method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("DELETE")){
				String paramStr = getParamStr(paramMap, isUrlEncoding);
				if(paramStr!=null && paramStr.length()>0){
					if(url.contains("?")){
						url = url + "&" + paramStr;
					}else{
						url = url + "?" + paramStr;
					}
				}
			}
			
			//创建连接对象
			URL urlPath = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlPath.openConnection();
		/*	
			//忽略https的证书
			ignoreSSLCer(conn);
			*/
			//设置请求方式
			conn.setRequestMethod(method);
			
			//设置请求头
			if(headerMap!=null && headerMap.size()>0){
				for(String key : headerMap.keySet()){
					conn.setRequestProperty(key, headerMap.get(key));
				}
			}
			
			//设置超时时间(30秒)
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			
			//取消自动重定向(否则获取不到重定向请求中的cookie)
			conn.setInstanceFollowRedirects(false);
			
			//设置cookie
			if(openCookie){
				String lastCookie = null;
				if(headerMap!=null){
					lastCookie = headerMap.get("Cookie");
				}
				setCookie(conn,lastCookie);
			}
			
			//post请求组织参数
			if(method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")){
				String paramStr = getParamStr(paramMap,isUrlEncoding);
				if(paramStr!=null && paramStr.length()>0){
					conn.setDoOutput(true);//开启输出流,发送参数
					BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"utf-8"));
					out.write(paramStr);
					out.close();
				}
			}
			
			//保存cookie
			if(openCookie){
				saveCookie(conn);
			}
			
			//判断如果是重定向,则再发送一次get请求
			int status = conn.getResponseCode();
			if(status == 302){
				String location = conn.getHeaderField("Location");
				if(location.length()>0){
					LoggerUtils.info("http请求发现重定向,原地址:"+url+",重定向地址:"+location);
					send("GET", location, null, null, true, destout);
					return;
				}else{
					throw new RuntimeException("重定向没有发现location");
				}
			}
			
			//接收返回值
			InputStream in = null;
			//判断是否是500或404,如果是则读取error流,否则认为是正常,读取input流
			if(status/100!=5 && status/100!=4){
				in = conn.getInputStream();
				//是否是压缩的
				String enc = conn.getHeaderField("Content-Encoding");
				if(enc!=null && enc.equals("gzip")){
					in = new GZIPInputStream(in);
				}
			}else{
				in = conn.getErrorStream();
			}
			//读取流,并写入目标流
			byte[] b = new byte[1024*200];
			int len = -1;
			while( (len = in.read(b))!=-1){
				destout.write(b,0,len);
			}
			conn.disconnect();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 发送请求
	 * @param method 请求方式
	 * @param url 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	public byte[] sendReturnByteArr(String method,String url,Map<String,String> paramMap,Map<String,String> headerMap,boolean isUrlEncoding){
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		send(method,url,paramMap,headerMap,isUrlEncoding,byteOut);
		return byteOut.toByteArray();
	}
	
	/**
	 * 发送get请求
	 * @param urlPath 请求路径
	 * @return
	 */
	public String sendGet(String url){
		return parseHttpByteToString(sendReturnByteArr("GET",url,null,null,false),"utf-8");
	}
	
	/**
	 * 发送get请求
	 * @param urlPath 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	public String sendGet(String url, Map<String,String> paramMap,Map<String,String> headerMap){
		return parseHttpByteToString(sendReturnByteArr("GET",url,paramMap,headerMap,true),"utf-8");
	}
	
	/**
	 * 发送get请求,可指定是否进行url编码
	 * @param urlPath 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	public String sendGet(String url, Map<String,String> paramMap,Map<String,String> headerMap,boolean isUrlEncoding){
		return parseHttpByteToString(sendReturnByteArr("GET",url,paramMap,headerMap,isUrlEncoding),"utf-8");
	}
	
	/**
	 * 发送get请求返回字节数组
	 * @param urlPath 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	public byte[] sendGetReturnByteArray(String url, Map<String,String> paramMap,Map<String,String> headerMap){
		return sendReturnByteArr("GET",url,paramMap,headerMap,false);
	}
	
	/**
	 * 发送get请求并保存到文件
	 * @param urlPath 请求路径
	 * @return
	 */
	public void sendGetSaveFile(String url,Map<String,String> paramMap,Map<String,String> headerMap,String path){
		File file = new File(path);
		if(file.exists()){
			throw new RuntimeException("文件已存在:"+path);
		}
		try {
			OutputStream out = new FileOutputStream(file);
			new HttpUtils().send("get",url,paramMap,headerMap,true,out);
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 发送get请求并保存到文件,如果文件存在则直接替换
	 * @param urlPath 请求路径
	 * @return
	 */
	public void sendGetSaveFileReplace(String url,Map<String,String> paramMap,Map<String,String> headerMap,String path){
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		sendGetSaveFile(url,paramMap,headerMap,path);
	}
	
	/**
	 * 发送post请求获取字符串
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	public String sendPost(String url,Map<String,String> paramMap,Map<String,String> headerMap){
		return parseHttpByteToString(sendReturnByteArr("POST",url,paramMap,headerMap,false),"utf-8");
	}
	
	/**
	 * 发送post请求获取流
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	public byte[] sendPostReturnByteArray(String url,Map<String,String> paramMap,Map<String,String> headerMap){
		return sendReturnByteArr("POST",url,paramMap,headerMap,false);
	}
	
	/**
	 * 发送get请求,便捷方法
	 * @param urlPath 请求路径
	 * @return
	 */
	public static String sendHttpGet(String url){
		return new HttpUtils().sendGet(url);
	}
	
	/**
	 * 发送get请求,便捷方法
	 * @param urlPath 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	public static String sendHttpGet(String url, Map<String,String> paramMap,Map<String,String> headerMap){
		return new HttpUtils().sendGet(url,paramMap,headerMap);
	}
	
	/**
	 * 发送get请求,可指定是否进行url编码,便捷方法
	 * @param urlPath 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	public static String sendHttpGet(String url, Map<String,String> paramMap,Map<String,String> headerMap,boolean isUrlEncoding){
		return new HttpUtils().sendGet(url,paramMap,headerMap,isUrlEncoding);
	}
	
	/**
	 * 发送get请求返回字节数组,便捷方法
	 * @param urlPath 请求路径
	 * @param paramMap 参数
	 * @param headerMap 请求头
	 * @return
	 */
	public static byte[] sendHttpGetReturnByteArray(String url, Map<String,String> paramMap,Map<String,String> headerMap){
		return new HttpUtils().sendGetReturnByteArray(url,paramMap,headerMap);
	}
	
	/**
	 * 发送post请求获取字符串,便捷方法
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	public static String sendHttpPost(String url,Map<String,String> paramMap,Map<String,String> headerMap){
		return new HttpUtils().sendPost(url,paramMap,headerMap);
	}
	
	/**
	 * 发送post请求获取流,便捷方法
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 */
	public static byte[] sendHttpPostReturnByteArray(String url,Map<String,String> paramMap,Map<String,String> headerMap){
		return new HttpUtils().sendPostReturnByteArray(url,paramMap,headerMap);
	}
	
	/**
	 * 获取参数字符串
	 * @param paramMap
	 * @param isUrlEncoding
	 * @return
	 */
	public static String getParamStr(Map<String,String> paramMap,boolean isUrlEncoding){
		try {
			StringBuilder builder = new StringBuilder();
			if(paramMap!=null && paramMap.size()>0){
				//如果只有一个参数,且key为空,则当做请求体发送过去,没有参数名称
				if(paramMap.size()==1){
					String key = paramMap.keySet().iterator().next();
					if(key==null || key.length()==0){
						String value = paramMap.get(key);
						if(value==null){
							return "";
						}
						if(isUrlEncoding){
							//value = URLEncoder.encode(value, "utf-8");
						}
						return value;
					}
				}
				
				for(String key : paramMap.keySet()){
					String value = paramMap.get(key);
					if(value==null){
						continue;
					}
					if(isUrlEncoding){
						value = URLEncoder.encode(value, "utf-8");
					}
					builder.append(key+"="+value+"&");
				}
				if(builder.length()>0){
					builder.deleteCharAt(builder.length()-1);
				}
			}
			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 将字节数组解析为字符串
	 * @param bytes
	 * @return
	 */
	public static String parseHttpByteToString(byte[] bytes,String defaultEncoding){
		//先默认按utf-8转码
		try {
			String encoding = defaultEncoding;
			if(defaultEncoding == null || defaultEncoding.length()==0){
				encoding = "utf-8";
			}
			String str = new String(bytes,encoding);
			List<String> list = RegexUtils.getSubstrByRegexReturnList(str, " charset=(.*?)\"");
			if(list.size()>0){
				encoding = list.get(0);
			}
			if(encoding.length()>0 && !encoding.equalsIgnoreCase("utf-8")){
				str = new String(bytes,encoding);
			}
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 清除cookie
	 * @author likaihao
	 * @date 2016年7月20日 上午10:02:34
	 */
	public void cleanCookie(){
		cookieMap.clear();
	}
	
	/**
	 * 使用socket发送http请求
	 * @param filePath 请求头和请求内容的文件
	 * @return
	 *//*
	public String sendSocket(String filePath){
		
		 * 文件内容示例:
		 * POST /order/createorder HTTP/1.1
			Host: nmd.bus365.cn
			Connection: keep-alive
			Content-Length: 1097
			Pragma: no-cache
			Cache-Control: no-cache
			Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp;q=0.8
			Origin: http://nmd.bus365.cn
			User-Agent: Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36
			Content-Type: application/x-www-form-urlencoded
			Referer: http://nmd.bus365.cn/order/findpreparedata?ismock=0&scheduleid=79123620004427005&userid=&netname=nmd.bus365.cn&netaddress=192.168.3.61
			Accept-Encoding: gzip, deflate
			Accept-Language: zh-CN,zh;q=0.8
			Cookie: his_login_url=http://nmd.bus365.cn/schedule|http://nmd.bus365.cn/order/findpreparedata?ismock=0&scheduleid=79123620004427005&userid=&netname=nmd.bus365.cn&netaddress=192.168.3.61; posit=????o????; loginName=15210235251; his_login_url=http://nmd.bus365.cn/userinfo0|http://nmd.bus365.cn/schedule; sc=?￡???3???#220284000000#nmd.bus365.cn#192.168.3.61|; order_depcity=?￡???3???; CNZZDATA1000085926=711897924-1431866088-%7C1434463148; Hm_lvt_a831ab84f46124919024308edfb13ae0=1433054792,1434372590,1434458817; Hm_lpvt_a831ab84f46124919024308edfb13ae0=1434464561; nmdbus365_SESSION=fb9ee9ed8a1b6f4e11a1d316547b17f5b3ef71cc-%00___ID%3A77a7684d-378a-4121-a288-189fccf608e6%00%00___AT%3Af6e6cd035843d22c4ae72a9c694d867b06208561%00
			
			authenticityToken=f6e6cd035843d22c4ae72a9c694d867b06208561&order.scheduleid=79123620004427005&order.seattype=%E6%99%AE%E9%80%9A%E5%BA%A7&order.authenticitytoken=f6e6cd035843d22c4ae72a9c694d867b06208561&order.passengers=%5B%7B%22name%22%3A%22%E5%BC%A0%E4%B8%89%22%2C%22idnum%22%3A%22%22%2C%22phonenum%22%3A%2215210235251%22%2C%22passmark%22%3A%22tableDef%22%2C%22premiumstate%22%3A0%2C%22premiumcount%22%3A%220%22%7D%2C%7B%22name%22%3A%22%E6%9D%8E%E5%9B%9B%22%2C%22idnum%22%3A%22%22%2C%22phonenum%22%3A%2215210235251%22%2C%22passmark%22%3A%22tableDef1%22%2C%22premiumstate%22%3A0%2C%22premiumcount%22%3A%220%22%7D%5D&ismock=0&order.userid=&order.verifycode=aaaa&order.passengername=%E7%8E%8B%E4%BA%94&order.passengerphone=15210235251&order.passengeremail=gaoyuan%40bus365.com&order.issavepassenger=0&order.idnum=&clientinfo=%7B%22clienttype%22%3A%220%22%2C%22browsername%22%3A%22Netscape%22%2C%22browserversion%22%3A%225.0+%28Windows+NT+6.1%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F41.0.2272.101+Safari%2F537.36%22%2C%22osinfo%22%3A%22Win32%22%2C%22computerinfo%22%3A%22null%22%7D
			
		
		Socket socket = null;
		try {
			//获取文件内容
			String content = IOUtils.readFile(filePath);
			// * 获取Host和port
			String host = null;
			Integer port = null;
			String s = RegexUtils.getSubstrByRegexReturnList(content, "Host:(.*?)\r\n").get(0).trim();
			String[] arr = s.split(":");
			if(arr.length==1){
				host = arr[0];
				port = 80;
			}else{
				host = arr[0];
				port = new Integer(arr[1]);
			}
			if(host==null || port==null){
				throw new RuntimeException("host或port为空!");
			}
			// * 去掉Accept-Encoding一行,否则还要处理分块和乱码
			if(content.contains("Accept-Encoding:")){
				content = content.replaceAll("\r\n ?Accept-Encoding:.*?\r\n", "\r\n");
			}
			// * 请求头完毕后添加 Connection: Close,表示立即关闭连接,请求头完毕后,不管有没有请求体都要添加两个换行(\r\n\r\n)
			if(content.contains("\r\n\r\n")){
				//有请求体
				content = content.replaceFirst("\r\n\r\n", "\r\nConnection: Close\r\n\r\n");
			}else{
				//没有请求体
				content += "\r\nConnection: Close\r\n\r\n";
			}
			// * 将请求头和请求体中间的多余换行去掉
			content = content.replaceAll("(\r\n){2,}", "\r\n\r\n");
			// * 如果不是gbk编码,要注意文件头的几个字节,否则提示非法请求
			byte[] bytes = content.getBytes();
			int start = 0;
			if(bytes[0] == 63){
				start = 1;
			}
			if(bytes[0]==-17 && bytes[1]==-69 && bytes[2]==-65){
				start = 3;
			}
			
			//建立连接
			socket = new Socket(host,port);
			//发送请求
			OutputStream out = socket.getOutputStream();
			out.write(bytes,start,bytes.length-start);
			out.flush();
			//接收返回值(Connection: Close可能失效,需要获取请求头中的Content-Length,然后读取完毕后手动关闭连接)
			InputStream in = socket.getInputStream();
			// * 读取请求头
			StringBuilder headerBuilder = new StringBuilder();
			String str = null;
			int contentLength = -1;
			while( (str = readLine(in))!=null ){
				headerBuilder.append(str).append("\r\n");
				if(str.length()==0){
					break;
				}
			}
			// * 解析Content-Length
			String headerStr = headerBuilder.toString();
			if(headerStr.contains("Content-Length:")){
				String contentLengthStr = RegexUtils.getSubstrByRegexReturnList(headerStr, "Content-Length:(.*?)\r\n").get(0).trim();
				contentLength = new Integer(contentLengthStr);
			}else{
				contentLength = 100000;
				//throw new RuntimeException("在请求中没有找到请求头Content-Length");
			}
			// * 读取请求体
			byte[] b = new byte[contentLength];
			for(int i=0;i<contentLength;i++){
				b[i]=(byte) in.read();
			}
			String contentStr = new String(b);
			return headerStr + "\r\n" + contentStr;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}finally{
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}*/
	
	/**
	 * 字节流读取一行
	 * @param in
	 * @return
	 */
	private String readLine(InputStream in){
		try {
			List<Byte> byteList = new ArrayList<Byte>();
			byte b = -1;
			for(;;){
				b = (byte) in.read();
				if(b!=13){
					byteList.add(b);
				}else{
					b = (byte) in.read();
					if(b==10){
						break;
					}else{
						byteList.add((byte) 13);
						byteList.add(b);
					}
				}
			}
			if(byteList.size()==0){
				return null;
			}
			byte[] bytes = new byte[byteList.size()];
			for(int i=0;i<bytes.length;i++){
				bytes[i] = byteList.get(i);
			}
			return new String(bytes);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 保存每次响应中的cookie
	 * @param conn
	 */
	private void saveCookie(HttpURLConnection conn){
		//获取响应中的cookie
		Map<String, List<String>> headerMap = conn.getHeaderFields();
		List<String> cookieList = headerMap.get("Set-Cookie");
		if(cookieList==null || cookieList.size()==0){
			return;
		}
		
		//获取domain,一级域名cookie共享, 如:baidu.com
		String domain = getCookieDomain(conn);
		
		//添加cookie
		Map<String,String> map = null;
		if(cookieMap.containsKey(domain)){
			map = cookieMap.get(domain);
		}else{
			map = new HashMap<String,String>();
			cookieMap.put(domain, map);
		}
		for(String cookie : cookieList){
			String keyValue = cookie.split(";")[0];
			String[] arr = keyValue.split("=",2);
			map.put(arr[0], arr[1]);
		}
		//System.out.println("保存cookie:"+cookieList);
	}
	
	/**
	 * 为请求设置cookie
	 * @param conn
	 */
	private void setCookie(HttpURLConnection conn,String lastCookie){
		if(cookieMap.size()==0){
			return;
		}
		
		//获取domain, 如baidu.com
		String domain = getCookieDomain(conn);
		if(!cookieMap.containsKey(domain)){
			return;
		}
		
		//解析用户设置的cookie
		Map<String,String> lastCookieMap = new HashMap<String,String>();
		if(lastCookie!=null){
			String[] arr = lastCookie.split(";");
			for(String str : arr){
				String[] arr2 = str.split("=");
				lastCookieMap.put(arr2[0], arr2[1]);
			}
		}
		
		//添加自动记录的cookie
		Map<String,String> map = cookieMap.get(domain);
		StringBuilder builder = new StringBuilder();
		for(String cookieName : map.keySet()){
			String cookieValue = map.get(cookieName);
			if(lastCookieMap.containsKey(cookieName)){
				cookieValue = lastCookieMap.get(cookieName);
				lastCookieMap.remove(cookieName);
			}
			builder.append(cookieName);
			builder.append("=");
			builder.append(cookieValue);
			builder.append("; ");
		}
		
		//添加用户设置的cookie
		for(String cookieName : lastCookieMap.keySet()){
			String cookieValue = lastCookieMap.get(cookieName);
			builder.append(cookieName);
			builder.append("=");
			builder.append(cookieValue);
			builder.append("; ");
		}
		String cookie = builder.toString();
		
		//重新设置请求头
		conn.setRequestProperty("Cookie", cookie);
	}
	
	
	/**
	 * 忽略SSL证书
	 * @author likaihao
	 * @date 2015年12月10日 上午10:39:41
	 * @param conn
	 */
	/*private void ignoreSSLCer(HttpURLConnection conn){
		if(conn instanceof HttpsURLConnection){
			try {
				// 创建SSLContext对象，并使用我们指定的信任管理器初始化
				TrustManager[] tm = { new MyX509TrustManager() };
				SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
				sslContext.init(null, tm, new java.security.SecureRandom());
				// 从上述SSLContext对象中得到SSLSocketFactory对象
				SSLSocketFactory ssf = sslContext.getSocketFactory();
				
				((HttpsURLConnection)conn).setSSLSocketFactory(ssf);
				
				((HttpsURLConnection) conn).setHostnameVerifier(new MyHostnameVerifier());
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}*/
	
	/**
	 * 获取cookie的domain,即生效路径
	 * @author likaihao
	 * @date 2016年7月20日 上午9:41:31
	 * @param conn
	 * @return
	 */
	private String getCookieDomain(HttpURLConnection conn){
		//获取domain,一级域名cookie共享, 如:baidu.com
		String domain = conn.getURL().getAuthority();
		String[] domainArr = domain.split("\\.");
		if(domainArr.length==3){
			domain =domainArr[1]+"."+domainArr[2];
		}
		return domain;
	}
	
}