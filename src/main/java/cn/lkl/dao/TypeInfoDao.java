package cn.lkl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.lkl.vo.TypeInfo;
@Mapper
public interface TypeInfoDao {

	public int insertSelective(TypeInfo paramTypeInfo);

	public List<TypeInfo> selectAllType(@Param("companyId")Integer companyId);

	public TypeInfo queryType(@Param("companyId")Integer companyId,@Param("type")String type);


}