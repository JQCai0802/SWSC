package com.lxsc.seckill.orders.service;

import com.lxsc.seckill.orders.model.Orders;

public interface OrdersService {
    int addOrders(Orders orders);

    Orders getOrderResult(Integer goodsId, Integer uid);
}
