package cn.lkl;

import java.util.List;

import org.apache.http.HttpEntity;
 
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.lkl.dao.TypeCountDao;
import cn.lkl.dao.UserDao;
import cn.lkl.service.ILiuChongService;
import cn.lkl.vo.TranactionRecord;
import cn.lkl.vo.TypeCount;
import cn.lkl.vo.User;

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
		List<TypeCount> all = liuChongService.getTypeCount(1, 20, "", 10000);
		for(TypeCount t :all) {
			System.out.println(t);
		}
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
}
