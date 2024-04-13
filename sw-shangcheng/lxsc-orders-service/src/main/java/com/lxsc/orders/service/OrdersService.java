package com.lxsc.orders.service;

import com.lxsc.orders.model.OrderInfo;
import com.lxsc.orders.model.Orders;

import java.util.List;
import java.util.Map;

public interface OrdersService {

    Orders addOrders(Long goodsId, Integer buyNum, Long userId);

    List<Map> getOrderInfoByOrderId(Long orderId);

    void orderPay(Orders orders);

    void checkOrderPay(String payStatus, Orders orders);

    void cancelOrder(Orders orders, OrderInfo orderInfo);
}
