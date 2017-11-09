package cn.lkl.dao;


import org.apache.ibatis.annotations.Mapper;

import cn.lkl.vo.JianDanImage;
@Mapper
public interface ImageDao {
	//@Select("SELECT * FROM t_jiandanImage WHERE id = #{id}")
	public JianDanImage getImageById(int id);
}
