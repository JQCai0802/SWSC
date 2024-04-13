package com.lxsc.seckill.remote;

import com.lxsc.JsonResult;
import com.lxsc.seckill.model.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("SECKILL-GOODS-SERVICE")
public interface RemoteGoodsService {
    @RequestMapping("/getGoodsList")
    JsonResult<List<Goods>> getGoodsList();
}
