package com.lxsc.goods.service;

import java.math.BigDecimal;

public interface GoodsService {
    BigDecimal decrGoodStore(Long goodsId, Integer buyNum);

    void incrGoodsStore(Long goodsId, Integer buyNum);
}
