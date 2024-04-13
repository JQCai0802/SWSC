package com.lxsc.goods.mapper;

import com.lxsc.goods.model.Evalute;

import java.util.List;
import java.util.Map;

public interface EvaluteMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Evalute record);

    int insertSelective(Evalute record);

    Evalute selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Evalute record);

    int updateByPrimaryKey(Evalute record);

    long countEvaluteByGoodsId(Long goodsId, String evaluteLevel);

    List<Evalute> selectEvalutePage(Long goodsId, Long skipNum, Long pageSize,String evaluteLevel);

    long countEvaluteHaveImgByGoodsId(Long goodsId);

    List<Evalute> selectEvaluteHaveImgPage(Long goodsId, Long skipNum, Long pageSize);

    Map countEvaluteNum(Long goodsId);
}