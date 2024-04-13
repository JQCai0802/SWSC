package com.lxsc.seckill.goods.service;

import com.lxsc.seckill.goods.model.Goods;

import java.util.List;

public interface GoodsService {
    List<Goods> getGoodsList();

    int secKill(Integer goodsId, Integer uid);
}
