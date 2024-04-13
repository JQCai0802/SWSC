package com.lxsc.goods.controller;


import com.lxsc.Code;
import com.lxsc.JsonResult;
import com.lxsc.goods.service.GoodsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@CrossOrigin
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @GetMapping("/decrGoodsStore")
    public Object decrGoodsStore(Long goodsId,Integer buyNum){
        BigDecimal price=goodsService.decrGoodStore(goodsId,buyNum);
        if(price==null){
            return new JsonResult<BigDecimal>(Code.ERROR,price);
        }
        return new JsonResult<BigDecimal>(Code.OK,price);
    }

    @GetMapping("/incrGoodsStore")
    public void incrGoodsStore(Long goodsId, Integer buyNum){
        goodsService.incrGoodsStore(goodsId,buyNum);
    }
}
