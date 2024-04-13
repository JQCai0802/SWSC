package com.lxsc.goods.service.impl;

import com.lxsc.goods.mapper.GoodsInfoMapper;
import com.lxsc.goods.service.GoodsService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsInfoMapper goodsInfoMapper;
    @GlobalTransactional
    public BigDecimal decrGoodStore(Long goodsId, Integer buyNum) {
        int result=goodsInfoMapper.decrGoodsStore(goodsId,buyNum);
        if(result==0){
            return null;
        }
        return goodsInfoMapper.selectPriceByGoodsId(goodsId);
    }

    @Override
    public void incrGoodsStore(Long goodsId, Integer buyNum) {
        goodsInfoMapper.incrGoodsStore(goodsId,buyNum);
    }
}
