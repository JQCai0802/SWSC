package com.lxsc.orders.mapper;

import com.lxsc.orders.model.OrderInfo;

import java.util.List;
import java.util.Map;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

    List<Map> selectConfirmOrderInfoByOrderId(Long orderId);

    void deleteByOrderId(Long ordersId);
}