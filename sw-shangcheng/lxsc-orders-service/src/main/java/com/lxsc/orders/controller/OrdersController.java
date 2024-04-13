package com.lxsc.orders.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.lxsc.orders.config.AlipayConfig;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.lxsc.Code;
import com.lxsc.JsonResult;
import com.lxsc.orders.model.Orders;
import com.lxsc.orders.service.OrdersService;
import com.lxsc.orders.service.remote.UserService;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class OrdersController {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;
    @Resource
    private OrdersService ordersService;
    @RequestMapping("/addOrder")
    public Object addOrder(Long goodsId,Integer buyNum, String token){
        JsonResult<Long> jsonResult=userService.getUserId(token);
        /*if(jsonResult.getCode().equals(Code.ERROR.getCode())){
            return new JsonResult(Code.ERROR,null);
        }
        return new JsonResult(Code.OK,jsonResult.getResult());*/
        //System.out.println(jsonResult.getCode());
        //System.out.println(jsonResult.getResult());

        Orders orders=ordersService.addOrders(goodsId,buyNum,jsonResult.getResult());
        if(orders==null){
            return new JsonResult(Code.ERROR,null);
        }
        Map orderMap=new HashMap();
        orderMap.put("ordersMoney",orders.getOrdersMoney());
        orderMap.put("orderId",orders.getId());
        orderMap.put("orderNo",orders.getOrderNo());
        return new JsonResult(Code.OK,orderMap);
        //return new JsonResult(Code.OK,orders.getId());
    }

    @RequestMapping("/confirmOrderInfo")
    public Object confirmOrderInfo(Long orderId){
        List<Map> orderInfoList=ordersService.getOrderInfoByOrderId(orderId);
        return new JsonResult(Code.OK,orderInfoList);
    }

    @RequestMapping("/pay")
    public Object pay(Long orderId, String orderNo,Integer porint, BigDecimal actualMoney,Long addressId,String token,String payType) throws AlipayApiException {
        JsonResult<Long> jsonResult=userService.getUserId(token);
        /*if(jsonResult.getCode().equals(Code.ERROR.getCode())){
            return new JsonResult(Code.ERROR,null);
        }*/
        //取这个值的目的是判断用户是否登录
        Long userId=jsonResult.getResult();
        //建议一个map存储从html中传回的参数
        Map map=new HashMap();
        map.put("addressId",addressId);
        map.put("porint",porint);
        map.put("id",orderId);
        map.put("orderNo",orderNo);
        //将map转化为json格式
        String orderJson=JSONObject.toJSONString(map);
        //添加事务，保证两个方法同时进行，返回一个object
        stringRedisTemplate.execute(new SessionCallback<Object>(){
            public <K,V> Object execute(RedisOperations<K,V> redisOperations){
                redisOperations.multi();
                //把json数据存入redis，如果用户超期未支付，该数据会从redis中移除，操作后返回一个布尔类型值
                redisOperations.opsForValue().setIfAbsent((K)("orderPay"+orderNo),(V)orderJson,Duration.ofSeconds(60*45));
                //掉单信息存储（备份信息），不指定超时时间，要使用定时任务扫描数据
                redisOperations.opsForZSet().add((K)"orders",(V)orderJson,System.currentTimeMillis());
                return redisOperations.exec();
            }
        });
        //将参数原封不动传入zfb函数
        if(payType.equals("zfb")){
            return zfbPay(orderId,orderNo,actualMoney,addressId,token,payType);
        }
        return "准备支付";
    }

    //此处语句执行出现问题时会发生掉单现象
    @RequestMapping("/paySuccess")
    public String paySuccess(String out_trade_no,BigDecimal total_amount){
        System.out.println(out_trade_no);
        //必须保证此处能获取到数据
        String orderJson=stringRedisTemplate.opsForValue().get("orderPay"+out_trade_no);
        Orders orders=JSONObject.parseObject(orderJson,Orders.class);
        orders.setActualMoney(total_amount);
        //0是待支付、1是待发货
        orders.setStatus(1);
        ordersService.orderPay(orders);
        return "支付成功";
    }

    @RequestMapping("/checkOrderPay")
    public Object checkOrderPay(String orderJson) throws AlipayApiException {
        Orders orders=JSONObject.parseObject(orderJson,Orders.class);
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL,AlipayConfig.APPID,AlipayConfig.RSA_PRIVATE_KEY,AlipayConfig.FORMAT,AlipayConfig.CHARSET,AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent=new JSONObject();
        bizContent.put("out_trade-no",orders.getOrderNo().toString());
        request.setBizContent(bizContent.toString());
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            JSONObject jsonObject=JSONObject.parseObject(response.getBody());
            JSONObject jsonObject1=(JSONObject) jsonObject.get("alipay_trade_query_response");
            String payStatus=jsonObject1.getString("trade_status");
            //执行添加MQ的操作
            ordersService.checkOrderPay(payStatus,orders);
        } else {
            System.out.println("调用失败");
            System.out.println(response.getBody());
        }
        return new JsonResult(Code.OK,null);
    }



    private String zfbPay(Long orderId, String orderNo, BigDecimal actualMoney,Long addressId,String token,String payType) throws AlipayApiException {
        //客户端对象
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.URL,AlipayConfig.APPID,AlipayConfig.RSA_PRIVATE_KEY,AlipayConfig.FORMAT,AlipayConfig.CHARSET,AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
        //请求对象
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //响应地址
        request.setNotifyUrl(AlipayConfig.notify_url);
        request.setReturnUrl(AlipayConfig.return_url);
        //业务参数
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderNo);
        bizContent.put("total_amount", actualMoney);
        bizContent.put("subject", "lx商城订单支付");
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        //请求参数
        request.setBizContent(bizContent.toString());
        //发送请求，获取用户支付页面。先跳转返回页面然后再跳转同步地址
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            String str=response.getBody();
            return str;
        } else {
            System.out.println("调用失败");
        }
        return "";
    }


















}
