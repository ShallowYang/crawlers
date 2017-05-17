package com.baza.crawler.dao;

import com.baza.crawler.domain.LinkedinBaiDuSearch;
import com.baza.crawler.domain.LinkedinBaiDuSearchExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface LinkedinBaiDuSearchMapper {
    int countByExample(LinkedinBaiDuSearchExample example);

    int deleteByExample(LinkedinBaiDuSearchExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(LinkedinBaiDuSearch record);

    int insertSelective(LinkedinBaiDuSearch record);

    List<LinkedinBaiDuSearch> selectByExample(LinkedinBaiDuSearchExample example);

    LinkedinBaiDuSearch selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") LinkedinBaiDuSearch record, @Param("example") LinkedinBaiDuSearchExample example);

    int updateByExample(@Param("record") LinkedinBaiDuSearch record, @Param("example") LinkedinBaiDuSearchExample example);

    int updateByPrimaryKeySelective(LinkedinBaiDuSearch record);

    int updateByPrimaryKey(LinkedinBaiDuSearch record);

    List<LinkedinBaiDuSearch> selectByNoCrawler();
}