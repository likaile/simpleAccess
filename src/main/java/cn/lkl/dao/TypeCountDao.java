package cn.lkl.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.lkl.vo.TypeCount;

@Mapper
public interface TypeCountDao {
    //@Insert("insert into typeCount(type,count,desc1,updated) values(#{type},#{count},#{desc},#{updated})")
    //@Options(useGeneratedKeys = true, keyProperty = "id")  
 //   int insert(TypeCount record);
  //  @Insert("insert into typeCount(type,count,desc1,updated) values(#{type},#{count},#{desc},#{updated})")
    int insertSelective(TypeCount record);
 //   @Update("update typeCount set count=#{count},updated=#{updated},desc1=#{desc} WHERE type=#{type}")
    int updateTypeCount(TypeCount record);
  //  @Select("select * from typeCount where type = #{type}")
    TypeCount selectByType(@Param("companyId")Integer companyId,@Param("type")String type);
   // @Select("select * from typeCount where type = #{type} order by updated desc")
    List<TypeCount> selectAllWithTypeName(@Param("companyId")Integer companyId,@Param("type")String type);
    //@Select("select password from user_sys where name = #{name}")
	
	int selectAllTypeInfo(@Param("companyId")Integer companyId);
	
	int deleteTypeCount(@Param("companyId")Integer companyId,@Param("type")String type);

}