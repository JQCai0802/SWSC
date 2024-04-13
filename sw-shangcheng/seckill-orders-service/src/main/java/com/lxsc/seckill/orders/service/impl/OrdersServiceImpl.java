package com.lxsc.seckill.orders.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lxsc.seckill.commons.Constants;
import com.lxsc.seckill.orders.mapper.OrdersMapper;
import com.lxsc.seckill.orders.model.Orders;
import com.lxsc.seckill.orders.service.OrdersService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

public class OrdersServiceImpl implements OrdersService {
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public int addOrders(Orders orders) {
        try {
            //补全
            orders.setBuyNum(1);
            orders.setCreateTime(new Date());
            orders.setStatus(1);
            orders.setOrderMoney(orders.getBuyPrice().multiply(new BigDecimal(orders.getBuyNum())));
            //此处对mapper配置文件进行修改，自动获取order记录主键
            ordersMapper.insert(orders);
            //将插入到mysql中的完整订单消息存入redis
            String key= Constants.SEC_KILL_ORDERS_RESULT+orders.getGoodsId()+":"+orders.getUid();
            String value= JSONObject.toJSONString(orders);
            stringRedisTemplate.opsForValue().set(key,value, Duration.ofSeconds(60*5));

        } catch (DuplicateKeyException e) {
            System.out.println(e.getClass());
            return 0;
            //try catch拦截重复消息异常后，不会抛异常到MQ，防止数据出不了列
        }
        return 0;
    }

    @Override
    public Orders getOrderResult(Integer goodsId, Integer uid) {
        String key= Constants.SEC_KILL_ORDERS_RESULT+goodsId+":"+uid;
        String orderStr= stringRedisTemplate.opsForValue().get(key);
        if(orderStr==null){
            return null;
        }
        return JSONObject.parseObject(orderStr,Orders.class);

    }
}
