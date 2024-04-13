package com.lxsc.seckill.goods.controller;

import com.lxsc.Code;
import com.lxsc.JsonResult;
import com.lxsc.seckill.goods.model.Goods;
import com.lxsc.seckill.goods.service.GoodsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @RequestMapping("/getGoodsList")
    public Object getGoodsList(){
        List<Goods> list=goodsService.getGoodsList();
        return new JsonResult<List<Goods>>(Code.OK,list);
    }
    @RequestMapping("/secKill")
    public Object secKill(Integer goodsId){
        //用户id，此处不做展开，简写
        Integer uid=1;
        int result=goodsService.secKill(goodsId,uid);
        switch(result){
            case 1:
                return new JsonResult(Code.ERROR,"1",null);
            case 2:
                return new JsonResult(Code.ERROR,"2",null);
            case 3:
                return new JsonResult(Code.ERROR,"3",null);
            case 4:
                return new JsonResult(Code.ERROR,"4",null);
        }
        return new JsonResult(Code.OK,null);
    }












}
