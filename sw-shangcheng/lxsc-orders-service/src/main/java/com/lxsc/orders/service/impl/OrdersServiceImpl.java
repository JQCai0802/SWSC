package com.lxsc.orders.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lxsc.Code;
import com.lxsc.JsonResult;
import com.lxsc.orders.mapper.OrderInfoMapper;
import com.lxsc.orders.mapper.OrdersMapper;
import com.lxsc.orders.model.OrderInfo;
import com.lxsc.orders.model.Orders;
import com.lxsc.orders.service.OrdersService;
import com.lxsc.orders.service.remote.RemoteGoodsService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private RemoteGoodsService remoteGoodsService;
    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    @Transactional
    @GlobalTransactional
    public Orders addOrders(Long goodsId, Integer buyNum, Long userId) {
        JsonResult<BigDecimal> result=remoteGoodsService.decrGoodsStore(goodsId,buyNum);
        if(result.getResult()==null){
            return null;
        }
        BigDecimal goodsPrice=result.getResult();
        System.out.println(userId+"----");
        //添加订单
        Orders orders=new Orders();
        orders.setOrdersMoney(goodsPrice.multiply(new BigDecimal(buyNum)));
        orders.setStatus(0);
        orders.setUserId(userId);
        orders.setCreateTime(System.currentTimeMillis());
        orders.setPorint(0);
        orders.setOrderNo(UUID.randomUUID().toString().replaceAll("-",""));
        ordersMapper.insertSelective(orders);
        System.out.println(orders.getId());
        //添加订单详情
        OrderInfo orderInfo=new OrderInfo();
        orderInfo.setOrdersId(orders.getId());
        orderInfo.setAmount(buyNum);
        orderInfo.setGoodsId(goodsId);
        orderInfo.setPrice(goodsPrice);
        orderInfoMapper.insert(orderInfo);
        //send to MQ
        //包含orders和orderInfo中的信息，在后面转变为字节
        Map messageMap=new HashMap();
        messageMap.put("orders",orders);
        messageMap.put("orderInfo",orderInfo);
        String messageJson=JSONObject.toJSONString(messageMap);
        //设置消息属性，指定超时时间，即队列A在20s后发送消息给队列B，能容忍的最大未支付时间
        MessageProperties properties=new MessageProperties();
        properties.setExpiration("20000");
        Message message=new Message(messageJson.getBytes(),properties);
        amqpTemplate.send("orderExchange","",message);
        return orders;
    }

    @Override
    public List<Map> getOrderInfoByOrderId(Long orderId) {
        return orderInfoMapper.selectConfirmOrderInfoByOrderId(orderId);
    }

    @Override
    public void orderPay(Orders orders) {
        ordersMapper.updateByPrimaryKeySelective(orders);
        amqpTemplate.convertAndSend("orderPayExchange","", JSONObject.toJSONString(orders));
    }

    @Override
    public void checkOrderPay(String payStatus, Orders orders) {
        if("TRADE_SUCCESS".equals(payStatus)){
            ordersMapper.updateByPrimaryKeySelective(orders);
            amqpTemplate.convertAndSend("orderPayExchange","", JSONObject.toJSONString(orders));
        }
    }

    @Transactional
    @GlobalTransactional
    public void cancelOrder(Orders orders, OrderInfo orderInfo) {
        Orders order=ordersMapper.selectByPrimaryKey(orders.getId());
        //判断订单是否已支付
        if(order.getStatus()==0){
            //如未支付，删掉orderinfo和order中的条目，并回滚订单。这三件事要同时实现。
            orderInfoMapper.deleteByOrderId(orderInfo.getOrdersId());
            ordersMapper.deleteByPrimaryKey(orders.getId());
            remoteGoodsService.incrGoodsStore(orderInfo.getGoodsId(),orderInfo.getAmount());
        }
    }
}


















