package com.lxsc.goods.mapper;

import com.lxsc.goods.model.EvaluteImg;

public interface EvaluteImgMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EvaluteImg record);

    int insertSelective(EvaluteImg record);

    EvaluteImg selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EvaluteImg record);

    int updateByPrimaryKey(EvaluteImg record);
}