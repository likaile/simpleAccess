package cn.lkl.dao;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.lkl.vo.TranactionRecord;
@Mapper
public interface TranactionRecordDao {
    int insert(TranactionRecord tr);

    int insertSelective(TranactionRecord tr);
    
    List<TranactionRecord> selectByUserAndTime(TranactionRecord tr);

	TranactionRecord getTranactionRecordByID(TranactionRecord tr);

	void deleteTranactionRecord(TranactionRecord tr);
	
	int selectAllTypeTrans(@Param("companyId")Integer companyId);
}