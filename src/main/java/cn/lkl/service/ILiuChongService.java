package cn.lkl.service;

import java.util.List;

import cn.lkl.vo.TranactionRecord;
import cn.lkl.vo.TypeCount;
import cn.lkl.vo.User;

public interface ILiuChongService {
	public void saveType(String paramString1, String paramString2, Integer companyId);

	public String getTypes(Integer companyId);

	public Integer editItem(String paramString1, int paramInt, String paramString2, String paramString3,String fromUser,String toUser, Integer companyId)
			throws Exception;

	public List<TranactionRecord> getTranactionRecord(Integer page, Integer rows, String orderType,
			String time, String type, Integer companyId);

	public Integer getCountByType(String Type, Integer companyId);

	public TranactionRecord getTranactionRecordByID(String name);

	public String callBackDate(TranactionRecord tr);
	
	public int getAllTypeInfos(Integer companyId);
	
	public int getAllTypeTrans(Integer companyId);

	public User selectUserByUserName(String username);

	public Integer saveItem(String type, int num1, String desc, String time, String addFromUser, Integer companyId) throws Exception;

	public List<TypeCount> getTypeCount(Integer page, Integer rows, String type, Integer companyId);

	public boolean queryType(Integer companyId, String type);

	public void saveUser(String mobile, String username, String password, String isAdd, String isEdit, String isSave, String isCallBack, String isDelete,Integer companyId);

	public void updateUserPwd(String mobile, String newPassword);

	public void deleteAllTypeInfo(String type,Integer companyId) throws Exception;
}
