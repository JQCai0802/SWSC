package com.lxsc.seckill.orders.listener;

import com.alibaba.fastjson.JSONObject;
import com.lxsc.seckill.commons.Constants;
import com.lxsc.seckill.orders.model.Orders;
import com.lxsc.seckill.orders.service.OrdersService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SecKillListener {
    @Resource
    private OrdersService ordersService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @RabbitListener(bindings={@QueueBinding(value=@Queue("secKillQueue"),exchange=@Exchange(name="secKillExchange",type = "fanout"))})
    public void secKillMessageListener(String message){
        Orders orders= JSONObject.parseObject(message,Orders.class);
        //添加订单，返回添加结果
        int result=ordersService.addOrders(orders);
        //只要是已插入数据库，无论如何都将redis中的放掉单消息删除
        if(result==0){
            stringRedisTemplate.opsForZSet().remove(Constants.SEC_KILL_ORDERS,message);
        }
    }
}
