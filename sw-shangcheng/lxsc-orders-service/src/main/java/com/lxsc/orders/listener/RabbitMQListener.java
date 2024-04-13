package com.lxsc.orders.listener;

import com.alibaba.fastjson.JSONObject;
import com.lxsc.orders.model.OrderInfo;
import com.lxsc.orders.model.Orders;
import com.lxsc.orders.service.OrdersService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


@Component
public class RabbitMQListener {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private OrdersService ordersService;
    //该监听器识别发送给MQ相关队列的信息，识别到以后在redis中把它删掉
    @RabbitListener(bindings={@QueueBinding(value=@Queue("ordersStatus"),exchange=@Exchange(name="ordersStatusExchange",type="fanout"))})
    public void deleteOrdersFromRedis(String message){
        stringRedisTemplate.opsForZSet().remove("orders",message);
    }

    //listen dead-letter-queue，监听死信队列（B）
    @RabbitListener(bindings = {@QueueBinding(key = {"orderDeadLetterKey"}, value = @Queue("orderDeadLetterQueue"), exchange = @Exchange(name = "orderDeadLetterExchange", type = "direct"))})
    public void orderDeadLetterListener(String message) {
        //将接收到的数据转换成集合，并取出
        Map map = JSONObject.parseObject(message, HashMap.class);
        JSONObject orderJson=(JSONObject) map.get("orders");
        JSONObject orderInfoJson=(JSONObject) map.get("orderInfo");
        //json转类
        Orders orders=orderJson.toJavaObject(Orders.class);
        OrderInfo orderInfo=orderInfoJson.toJavaObject(OrderInfo.class);
        ordersService.cancelOrder(orders,orderInfo);
    }
}
