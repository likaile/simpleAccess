package cn.lkl.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.lkl.dao.TranactionRecordDao;
import cn.lkl.dao.TypeCountDao;
import cn.lkl.dao.TypeInfoDao;
import cn.lkl.dao.UserDao;
import cn.lkl.service.ILiuChongService;
import cn.lkl.util.StringUtils;
import cn.lkl.vo.TranactionRecord;
import cn.lkl.vo.TypeCount;
import cn.lkl.vo.TypeInfo;
import cn.lkl.vo.User;

@Service("liuchongService")
public class LiuChongServiceImpl
  implements ILiuChongService
{
  @Autowired
  private TranactionRecordDao tranactionRecordDao;
  @Autowired
  private TypeInfoDao typeInfoDao;
  @Autowired
  private TypeCountDao typeCountDao;
  @Autowired
  private UserDao userDao;
 
  private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
  @Transactional
  public void saveType(String type, String desc,Integer companyId)
  {
    TypeInfo typeInfo = new TypeInfo();
    typeInfo.setType(type);
    typeInfo.setDesc(desc);
    typeInfo.setCompanyId(companyId);
    typeInfoDao.insertSelective(typeInfo);
    
    TypeCount tc = new TypeCount();
    tc.setType(type);
    tc.setCount(0);
    tc.setDesc(desc);
    tc.setUpdated(sd.format(new Date()));
    tc.setCompanyId(companyId);
    typeCountDao.insertSelective(tc);
  }
  
  public String getTypes(Integer companyId)
  {
    List<TypeInfo> all = typeInfoDao.selectAllType(companyId);
    if (all.size() > 0)
    {
      StringBuffer b = new StringBuffer();
      for (TypeInfo t : all) {
        b.append(t.getType() + ",");
      }
      return b.substring(0, b.length() - 1);
    }
    return "";
  }
  @Transactional
  public Integer saveItem(String type, int num, String desc, String time,String addFromUser,Integer companyId) throws Exception
  {
    TypeCount tc = typeCountDao.selectByType(companyId,type);
    if (StringUtils.isEmpty(time)) {
        time = sd.format(new Date());
    }else {
    	if(new Date().getTime()<sd.parse(time).getTime()) {
    		throw new Exception("超出当前时间范围!");
    	}
	    time = sd.format(sd.parse(time));
	}
    Integer count =0 ;
    TranactionRecord tr = new TranactionRecord();
    //如果数量库内有记录
    if (tc != null)
    {
      count = Integer.valueOf(tc.getCount().intValue() + num);
      tr.setCount(tc.getCount());
      tc.setCount(count);
      tc.setUpdated(time);
      tc.setCompanyId(companyId);
      typeCountDao.updateTypeCount(tc);
    }
    tr.setNum(Integer.valueOf(num));
    tr.setUser(addFromUser);
    tr.setReason("入库-" + desc);
    tr.setType(type);
    tr.setCreated(time);
    tr.setStatus(0);
    tr.setToUser("无");
    tr.setCompanyId(companyId);
    tranactionRecordDao.insertSelective(tr);
    return count;
  }
  @Transactional
  public Integer editItem(String type, int num, String desc, String time,String fromUser,String toUser,Integer companyId)
    throws Exception
  {
    TypeCount tc = typeCountDao.selectByType(companyId,type);
    Integer count = 0;
    TranactionRecord tr = new TranactionRecord();
    if (StringUtils.isEmpty(time)) {
        time = sd.format(new Date());
    }else {
    	if(new Date().getTime()<sd.parse(time).getTime()) {
    		throw new Exception("超出当前时间范围!");
    	}
	    time = sd.format(sd.parse(time));
	}
    if (tc != null)
    {
      if (num > tc.getCount().intValue()) {
        throw new Exception(type + "的仓库存储不足！");
      }
      count = Integer.valueOf(tc.getCount().intValue() - num);
      tc.setCount(count);
      tc.setUpdated(time);
      tc.setCompanyId(companyId);
      typeCountDao.updateTypeCount(tc);
      tr.setCount(tc.getCount());
    }
    else
    {
      throw new Exception(type + "的仓库存储不足！");
    }
    tr.setNum(Integer.valueOf(num));
    tr.setUser(fromUser);
    tr.setToUser(toUser);
    tr.setReason("出库-" + desc);
    tr.setType(type);
    tr.setCreated(time);
    tr.setStatus(0);
    tr.setCompanyId(companyId);
    tranactionRecordDao.insertSelective(tr);
    return count;
  }
  
  public List<TranactionRecord> getTranactionRecord(Integer page, Integer rows, String orderType, String time, String type,Integer companyId)
  {
    TranactionRecord tr = new TranactionRecord();
    if (!StringUtils.isEmpty(orderType)) {
      tr.setReason(orderType);
    }
    if (!StringUtils.isEmpty(time)) {
      tr.setCreated(time);
    }
    if (!StringUtils.isEmpty(type)) {
      tr.setType(type);
    }
    tr.setCompanyId(companyId);
    PageHelper.startPage(page.intValue(), rows.intValue());
    return tranactionRecordDao.selectByUserAndTime(tr);
  }
  
  public List<TypeCount> getTypeCount(Integer page, Integer rows,String type,Integer companyId)
  {
    PageHelper.startPage(page.intValue(), rows.intValue());
    return typeCountDao.selectAllWithTypeName(companyId,type);
  }
  
  public boolean queryType(Integer companyId,String types)
  {
    return typeInfoDao.queryType(companyId,types) != null;
  }

	@Override
	public Integer getCountByType(String type,Integer companyId) {
		if(StringUtils.isEmpty(type)) {
			return 0;
		}
		TypeCount tc = typeCountDao.selectByType(companyId,type);
		if(tc == null){
			return 0;
		}
		return tc.getCount();
	}

	@Override
	public TranactionRecord getTranactionRecordByID(String id) {
		TranactionRecord tr = new TranactionRecord();
		tr.setId(Integer.valueOf(id));
		return tranactionRecordDao.getTranactionRecordByID(tr);
	}
	@Override
	public String callBackDate(TranactionRecord tr) {
		TypeCount tc = typeCountDao.selectByType(tr.getCompanyId(),tr.getType());
		if(null == tc){return "回滚失败,仓库内无该物品种类！";}
		if(tr.getReason().contains("出库")){
			tc.setCount(tc.getCount()+tr.getNum());
		}
		if(tr.getReason().contains("入库")){
			if(tc.getCount()<tr.getNum()){
				return "回滚失败,该物品库存不足以支持入库的回滚操作";
			}
			tc.setCount(tc.getCount()-tr.getNum());
		}
		try {
			typeCountDao.updateTypeCount(tc);
			tranactionRecordDao.deleteTranactionRecord(tr);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "回滚成功！";
	}

	@Override
	public int getAllTypeInfos(Integer companyId) {
		return typeCountDao.selectAllTypeInfo(companyId);
	}

	@Override
	public int getAllTypeTrans(Integer companyId) {
		return tranactionRecordDao.selectAllTypeTrans(companyId);
	}

	@Override
	public User selectUserByUserName(String username) {
		return userDao.selectUserByUserName(username);
	}

	@Override
	public void saveUser(String mobile, String username, String password, String isAdd, String isEdit,String isSave, String isCallBack,Integer companyId) {
		StringBuffer action = new StringBuffer();
		if(!StringUtils.isEmpty(isAdd)&&isAdd.equals("0")) {
			action.append("入库,");
		}
		if(!StringUtils.isEmpty(isEdit)&&isEdit.equals("0")) {
			action.append("出库,");
		}
		if(!StringUtils.isEmpty(isSave)&&isSave.equals("0")) {
			action.append("新增商品,");
		}
		if(!StringUtils.isEmpty(isCallBack)&&isCallBack.equals("0")) {
			action.append("回滚,");
		}
		User user = new User();
		user.setMobile(mobile);
		user.setUsername(username);
		user.setPassword(password);
		user.setAction(action.toString());
		user.setCreattime(sd.format(new Date()));
		user.setCompanyid(companyId);
		userDao.insertSelective(user);
	}

	@Override
	public void updateUserPwd(String mobile, String newPassword) {
		User user = new User();
		user.setMobile(mobile);
		user.setPassword(newPassword);
		userDao.updateByPrimaryKeySelective(user);
	}

}
