package com.lxsc.goods.mapper;

import com.lxsc.goods.model.GoodsInfo;

import java.math.BigDecimal;

public interface GoodsInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GoodsInfo record);

    int insertSelective(GoodsInfo record);

    GoodsInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GoodsInfo record);

    int updateByPrimaryKey(GoodsInfo record);

    int decrGoodsStore(Long goodsId, Integer buyNum);

    BigDecimal selectPriceByGoodsId(Long goodsId);

    void incrGoodsStore(Long goodsId, Integer buyNum);
}