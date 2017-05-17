package com.baza.crawler.dao;

import com.baza.crawler.domain.LinkedinModel;
import com.baza.crawler.domain.LinkedinModelExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface LinkedinModelMapper {
    int countByExample(LinkedinModelExample example);

    int deleteByExample(LinkedinModelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LinkedinModel record);

    int insertSelective(LinkedinModel record);

    List<LinkedinModel> selectByExampleWithBLOBs(LinkedinModelExample example);

    List<LinkedinModel> selectByExample(LinkedinModelExample example);

    LinkedinModel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LinkedinModel record, @Param("example") LinkedinModelExample example);

    int updateByExampleWithBLOBs(@Param("record") LinkedinModel record, @Param("example") LinkedinModelExample example);

    int updateByExample(@Param("record") LinkedinModel record, @Param("example") LinkedinModelExample example);

    int updateByPrimaryKeySelective(LinkedinModel record);

    int updateByPrimaryKeyWithBLOBs(LinkedinModel record);

    int updateByPrimaryKey(LinkedinModel record);
}