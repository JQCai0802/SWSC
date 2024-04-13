package com.lxsc.timer;

import com.lxsc.JsonResult;
import com.lxsc.service.remote.RemoteOrderService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class LxscTimer {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RemoteOrderService remoteOrderService;
    //为了秒杀项目测试，暂时注释
    //@Scheduled(cron="*****")
    public void checkOrderPay(){
        long maxscore=System.currentTimeMillis()-1000*60*5;
        Set<String> ordersSet=stringRedisTemplate.opsForZSet().rangeByScore("orders",0,maxscore);
        ordersSet.forEach(order->{
            JsonResult jsonResult=remoteOrderService.checkOrderPay(order);
            System.out.println(jsonResult.getCode());
            //将订单信息map变成的字符串一个一个的取出发送给order服务，收取其回复并判断，回复支付成功时删除该记录。
            if(jsonResult.getCode().equals("10000")){
                stringRedisTemplate.opsForZSet().remove("orders",order);
            }
        });
    }
}
