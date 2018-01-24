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
/** 
 * @ClassName: IdCardTest 
 * @Description: 调用移动接口查询 姓名 与身份证号是否匹配
 * @author likaile
 * @date 2018年1月17日 下午2:14:55  
 */ 
 
public class IdCardTest {
	
	public static DefaultHttpClient httpclient = new DefaultHttpClient(new PoolingClientConnectionManager());
	
	public static void main(String args[]) {
		String url = "http://wap.sx.10086.cn/m/chooseNumber/chkIdCard.action";
		String result = post(url,"李凯乐","142731199110030355");
		System.out.println(result);
	}   
	
	public static String post(String url,String custName,String IdCard){    
        HttpPost post = new HttpPost(url);    
        List<NameValuePair> formparams = new ArrayList<NameValuePair>(); 
       
        formparams.add(new BasicNameValuePair("custName",custName));
        formparams.add(new BasicNameValuePair("idCard",IdCard));
        
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
