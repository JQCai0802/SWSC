package com.lxsc.seckill.goods.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lxsc.seckill.commons.Constants;
import com.lxsc.seckill.goods.mapper.GoodsMapper;
import com.lxsc.seckill.goods.model.Goods;
import com.lxsc.seckill.goods.service.GoodsService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AmqpTemplate amqpTemplate;

    @Override
    public List<Goods> getGoodsList() {
        return goodsMapper.selectAll();
    }

    @Override
    public int secKill(Integer goodsId, Integer uid) {
        //从redis中拿出秒杀商品信息
        Map map=stringRedisTemplate.opsForHash().entries(Constants.SEC_KILL_GOODS_STORE+goodsId);
        Long sysTime=System.currentTimeMillis();
        Long startTime=Long.parseLong(map.get("startTime").toString());
        Long endTime=Long.parseLong(map.get("endTime").toString());
        //判断是否在可秒杀时间内
        if(sysTime<startTime){
            return 1;
        }
        if(sysTime<endTime){
            return 2;
        }
        //将uid、goodsid、price三个参数组合在一起为订单信息，转换成json
        Map orderMap=new HashMap();
        orderMap.put("uid",uid);
        orderMap.put("goodsId",goodsId);
        orderMap.put("buyPrice",map.get("price"));
        String orderJson= JSONObject.toJSONString(orderMap);

        do {
            Object result=stringRedisTemplate.execute(new SessionCallback<Object>() {
                @Override
                public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                    List keys=new ArrayList();
                    keys.add(Constants.SEC_KILL_GOODS_STORE+goodsId);
                    keys.add(Constants.SEC_KILL_GOODS_STORE+goodsId+":"+uid);
                    //监控redis中这两个key所对应的数据
                    redisOperations.watch(keys);
                    //redis中取商品库存，判断是否够
                    String storeStr=(String) redisOperations.opsForHash().get(((K)(Constants.SEC_KILL_GOODS_STORE+goodsId)),"store");
                    if(Integer.valueOf(storeStr)<=0){
                        redisOperations.unwatch();
                        return 3;
                    }
                    //redis中取该用户购买量，判断之前是否买过
                    String purchaseLimits=(String) redisOperations.opsForValue().get(Constants.SEC_KILL_PURCHASE_LIMITS+goodsId+":"+uid);
                    if(purchaseLimits!=null){
                        redisOperations.unwatch();
                        return 4;
                    }
                    //开启事务
                    redisOperations.multi();
                    //库存-1
                    redisOperations.opsForHash().put(((K)(Constants.SEC_KILL_GOODS_STORE+goodsId)),"store",(Integer.parseInt(storeStr)-1)+"");
                    //用户购买量+1
                    redisOperations.opsForValue().set(((K)(Constants.SEC_KILL_PURCHASE_LIMITS+goodsId+":"+uid)),((V)"1"));
                    //将订单存入redis，防掉单数据，有时间戳
                    redisOperations.opsForZSet().add((K)Constants.SEC_KILL_ORDERS,(V)orderJson,System.currentTimeMillis());
                    return redisOperations.exec();
                }
            });
            //函数的第一个出口
            if(result instanceof Integer){
                return (int) result;
            }
            List list=(List) result;
            if(list!=null&&!list.isEmpty()){
                break;
            }
        } while (true);
        //将订单信息发给MQ，走支付流程
        //这段代码可能执行不到，但库存已减，因此要防止掉单
        amqpTemplate.convertAndSend("secKillExchange","",orderJson);
        //函数的第二个出口
        return 0;
    }
}
