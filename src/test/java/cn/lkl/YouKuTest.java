package cn.lkl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
@RunWith(SpringRunner.class)
@SpringBootTest
public class YouKuTest {
	public static  String[] addrs = new String[]{"浙江_杭州","香港_香港","浙江_金华","广东_广州","上海_上海","澳门_澳门","福建_厦门"};
	DefaultHttpClient httpclient = new DefaultHttpClient(new PoolingClientConnectionManager());
	   static int i ;
	    public static void main(String args[]) {
	        System.out.println(i);
	}
	@Test
	public void youkuTest() throws InterruptedException {
		  int num = 50 ; 
          num = num ++ * 2 ; 
          System.out.println(num) ; 
		/*String url = "http://hn-stage.lomark.cn/toyota/activity/submitCrown";
		String url2 = "http://hn-stage.lomark.cn/toyota/activity/CrownAward";
		Long started = 150101049888L;
		
		int count = 0;
		while (count <1000) {
			String result = post(url,started,true);
			if(result.equals("1")) {
				Thread.sleep(5000);
				result = post(url2,started,false);
				System.out.println(result);
				if(!result.contains("\"code\":0")) {
				}
			}else {
				System.out.println("第一步出错"+result);
			}
			started++;
			count++;
		}*/
	}
	public  String post(String url,Long phone,boolean flag){    
        HttpPost post = new HttpPost(url);    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
        if(flag) {
        	int random=(int)(Math.random()*7);
        	formparams.add(new BasicNameValuePair("times", "近三个月"));
        	formparams.add(new BasicNameValuePair("addr", addrs[random]));
        }
        formparams.add(new BasicNameValuePair("phone", phone+""));
        formparams.add(new BasicNameValuePair("name", "李乐"));
        
        try {   
            UrlEncodedFormEntity uefEntity;      
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");      
            post.setEntity(uefEntity);  
             
            HttpResponse res = httpclient.execute(post);    
            HttpEntity entity = res.getEntity();    
            if (entity != null) {      
                return EntityUtils.toString(entity, "UTF-8");  
          }    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }
        return "0";
     }  
}
