package cn.lkl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.lkl.dao.TypeCountDao;
import cn.lkl.dao.UserDao;
import cn.lkl.service.ILiuChongService;
import cn.lkl.util.BASE64Encoder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleAccessApplicationTests {
	@Autowired
	private TypeCountDao typeCountDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	 private ILiuChongService liuChongService;
	@Value("${weather.key}")
	String weatherKey;
	@Value("${weather.url}")
	String weatherUrl;
	private static DefaultHttpClient httpclient = new DefaultHttpClient(new PoolingClientConnectionManager());

	@Test
	public void contextLoads() {
		/* List<TranactionRecord> all =  liuChongService.getTranactionRecord(1, 30, "", "2017-10-05", "");
		for(TranactionRecord t :all) {
			System.out.println(t);
		}*/
		//int  name = liuChongService.getAllTypeInfos();
		/* System.out.println(name);
		TypeCount tc = typeCountDao.selectByType("云南白药护手霜");*/
		/*List<TypeCount> all = liuChongService.getTypeCount(1, 20, "", 10000);
		for(TypeCount t :all) {
			System.out.println(t);
		}*/
		String fileUrl = "//gfs18.atguat.net.cn/b10bf6930d9a696813ecd99c52671531.doc";
		fileUrl = fileUrl.substring(fileUrl.lastIndexOf("/")+1,fileUrl.lastIndexOf("."));
		System.out.println(fileUrl);
	}
	@Test
	public void weatherTest() {
		String url = weatherUrl+"now?city=北京&key="+weatherKey;
		HttpGet get = new HttpGet(url);
		HttpResponse res;
		HttpEntity entity;
		try {
			res = httpclient.execute(get);
			entity = res.getEntity();
			String data = EntityUtils.toString(entity, "UTF-8");
			System.out.println("Response content: " ); 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Test
	public void userDaoTest() {
		
		/*List<User> allUsers = userDao.queryAllUser(10000);
		for(User u :allUsers) {
			System.out.println(u.getUsername());
		}
		User user = userDao.selectUserByUserName("liuchong");
		user.setAction("入库");
		userDao.updateByPrimaryKeySelective(user);
		User u = new User();
		u.setUsername("zhangsan");
		u.setPassword("asdfaf");
		u.setRole("user");
		u.setAction("dff");
		u.setCreattime("sdff");
		u.setCompanyid(10000);
		userDao.insertSelective(u);*/
		
	}
	@Test
	public void ronglianyunTest() throws Exception {
		for(int j = 0; j< 100; j++){
		    System.out.println((int)((Math.random()*9+1)*100000));
		    }		
	}
	
	public String sendTemplateSMS(String to, String templateId, String[] datas) throws Exception {
		String App_ID = "8a216da86077dcd001607884626d0102";
		//DefaultHttpClient httpclient = registerSSL("app.cloopen.com", "TLS", Integer.parseInt("8883"), "https");
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
		return result;
	}
	
	private HttpRequestBase getHttpRequestBase(int get, String action) throws Exception {
		String ACCOUNT_SID = "8a216da86077dcd001607884621100fc";
		String ACCOUNT_TOKEN = "8a234f4671a44dde86e3581f648b4821";
		StringBuffer baseUrl = new StringBuffer("https://app.cloopen.com:8883/2013-12-26");
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = sd.format(new Date());
		String sig = "";
		String acountName = "";
		String acountType = "Accounts";
		
		acountName = ACCOUNT_SID;
		sig = ACCOUNT_SID + ACCOUNT_TOKEN + timestamp;
		String signature = md5Digest(sig);

		String url = baseUrl.append("/" + acountType + "/").append(acountName).append("/" + action + "?sig=").append(signature).toString();
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
