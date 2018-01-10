package cn.lkl.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * 容联云 接口 6分钱一条 谨慎使用
 * Date: 2017年12月22日 下午1:46:31 
 * @author likaile 
 * @desc
 */
@Component
public class MessageUtils {
	@Value("${message.appId}")
	private String App_ID;
	@Value("${message.accountId}")
	private String ACCOUNT_SID;
	@Value("${message.accountToken}")
	private String ACCOUNT_TOKEN;
	@Value("${message.baseUrl}")
	private String baseUrl;
	private static final SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Date:2017年12月22日下午1:53:28 
	 * @author likaile 
	 * @desc to 发送的手机号  多手机号用逗号分开  templateId 模板ID datas 自定义替换的值
	 */
	public String sendTemplateSMS(String to, String templateId, String[] datas) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient(new PoolingClientConnectionManager());
		String result = "";
		try { 
			HttpPost httppost = (HttpPost) getHttpRequestBase(1, "SMS/TemplateSMS");
			String requsetbody = "";
				JsonObject json = new JsonObject();
				json.addProperty("appId", App_ID);
				json.addProperty("to", to);
				json.addProperty("templateId", templateId);
				if (datas != null) {
					StringBuilder sb = new StringBuilder("[");
					for (String s : datas) {
						sb.append("\"" + s + "\"" + ",");
					}
					sb.replace(sb.length() - 1, sb.length(), "]");
					JsonParser parser = new JsonParser();
					JsonArray Jarray = parser.parse(sb.toString()).getAsJsonArray();
					json.add("datas", Jarray);
				}
				requsetbody = json.toString();
			BasicHttpEntity requestBody = new BasicHttpEntity();
			requestBody.setContent(new ByteArrayInputStream(requsetbody.getBytes("UTF-8")));
			requestBody.setContentLength(requsetbody.getBytes("UTF-8").length);
			httppost.setEntity(requestBody);
			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();
			if (entity != null)
				result = EntityUtils.toString(entity, "UTF-8");

			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}  finally {
			if (httpclient != null)
				httpclient.getConnectionManager().shutdown();
		}
		logger.info("---sendMessage :"+to+" return:"+result);
		return result;
	}
	
	private HttpRequestBase getHttpRequestBase(int get, String action) throws Exception {
		String timestamp = sd.format(new Date());
		String sig = "";
		String acountName = "";
		String acountType = "Accounts";
		acountName = ACCOUNT_SID;
		sig = ACCOUNT_SID + ACCOUNT_TOKEN + timestamp;
		String signature = md5Digest(sig);
		String url = new StringBuffer(baseUrl).append("/" + acountType + "/").append(acountName).append("/" + action + "?sig=").append(signature).toString();
		HttpRequestBase mHttpRequestBase = null;
		mHttpRequestBase = new HttpPost(url);
		mHttpRequestBase.setHeader("Accept", "application/json");
		mHttpRequestBase.setHeader("Content-Type", "application/json;charset=utf-8");
		String src = acountName + ":" + timestamp;
		String auth = base64Encoder(src);
		mHttpRequestBase.setHeader("Authorization", auth);
		return mHttpRequestBase;
	}
	public String base64Encoder(String src) throws Exception
	  {
	    BASE64Encoder encoder = new BASE64Encoder();
	    return encoder.encode(src.getBytes("utf-8"));
	  }
	 public String md5Digest(String src) throws Exception
	  {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    byte[] b = md.digest(src.getBytes("utf-8"));
	    return byte2HexStr(b);
	  }
	private String byte2HexStr(byte[] b)
	  {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < b.length; ++i) {
	      String s = Integer.toHexString(b[i] & 0xFF);
	      if (s.length() == 1)
	        sb.append("0");

	      sb.append(s.toUpperCase());
	    }
	    return sb.toString();
	  }
}
