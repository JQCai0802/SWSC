package com.lxsc.seckill.timer;

import com.lxsc.seckill.commons.Constants;
import com.lxsc.seckill.model.Goods;
import com.lxsc.seckill.remote.RemoteGoodsService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Component
public class SecKillTimer {
    @Resource
    private RemoteGoodsService remoteGoodsService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AmqpTemplate amqpTemplate;

    @Scheduled(cron = "* * * * * *")
    public void initSecKillDataToRedis(){
        //找到goods数据库中
        List<Goods> goodsList=remoteGoodsService.getGoodsList().getResult();
        goodsList.forEach(goods -> {
            stringRedisTemplate.opsForHash().putIfAbsent(Constants.SEC_KILL_GOODS_STORE+goods.getId(),"store",goods.getStore()+"");
            stringRedisTemplate.opsForHash().putIfAbsent(Constants.SEC_KILL_GOODS_STORE+goods.getId(),"startTime",goods.getStartTime().getTime()+"");
            stringRedisTemplate.opsForHash().putIfAbsent(Constants.SEC_KILL_GOODS_STORE+goods.getId(),"endTime",goods.getEndTime().getTime()+"");
            stringRedisTemplate.opsForHash().putIfAbsent(Constants.SEC_KILL_GOODS_STORE+goods.getId(),"price",goods.getPrice()+"");
        });
    }

    @Scheduled(cron = "0/5 * * * * *")
    public void diaoDan(){
        long maxScore=System.currentTimeMillis()-1000*60*5;
        Set<String> orderSet=stringRedisTemplate.opsForZSet().rangeByScore(Constants.SEC_KILL_ORDERS,0,maxScore);
        orderSet.forEach(order->amqpTemplate.convertAndSend("secKillExchange","",order));
    }

}
