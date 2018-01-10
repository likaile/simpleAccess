package cn.lkl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.lkl.vo.User;
@Mapper
public interface UserDao {
	int deleteByPrimaryKey(Integer id);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

	User selectUserByUserName(@Param("mobile")String mobile);
	
	List<User> queryAllUser(@Param("companyId")Integer companyId);
	
}
