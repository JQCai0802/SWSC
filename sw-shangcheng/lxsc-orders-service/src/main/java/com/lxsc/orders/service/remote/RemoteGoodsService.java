package com.lxsc.orders.service.remote;


import com.lxsc.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient("GoodsService")
public interface RemoteGoodsService {
    @GetMapping("/decrGoodsStore")
    JsonResult<BigDecimal> decrGoodsStore(@RequestParam Long goodsId, @RequestParam Integer buyNum);
    @GetMapping("/incrGoodsStore")
    void incrGoodsStore(@RequestParam Long goodsId, @RequestParam Integer buyNum);
}
