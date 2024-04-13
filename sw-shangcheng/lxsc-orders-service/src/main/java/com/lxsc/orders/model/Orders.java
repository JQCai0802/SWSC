package com.lxsc.orders.model;

import java.math.BigDecimal;

public class Orders {
    private Long id;

    private BigDecimal ordersMoney;

    private BigDecimal actualMoney;

    private Integer porint;

    private Integer status;

    private Long createTime;

    private Long userId;

    private Long addressId;

    private String orderNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getOrdersMoney() {
        return ordersMoney;
    }

    public void setOrdersMoney(BigDecimal ordersMoney) {
        this.ordersMoney = ordersMoney;
    }

    public BigDecimal getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(BigDecimal actualMoney) {
        this.actualMoney = actualMoney;
    }

    public Integer getPorint() {
        return porint;
    }

    public void setPorint(Integer porint) {
        this.porint = porint;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}