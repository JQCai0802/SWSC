package com.lxsc.seckill.orders.controller;

import com.lxsc.Code;
import com.lxsc.JsonResult;
import com.lxsc.seckill.orders.model.Orders;
import com.lxsc.seckill.orders.service.OrdersService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@CrossOrigin
public class OrdersController {
    @Resource
    private OrdersService ordersService;
    @RequestMapping("/getOrderResult")
    public Object getOrderResult(Integer goodsId){
        Integer uid=1;
        Orders orders=ordersService.getOrderResult(goodsId,uid);
        if(orders==null){
            return new JsonResult<Orders>(Code.ERROR,null);
        }
        return new JsonResult<Orders>(Code.OK,orders);
    }
}
