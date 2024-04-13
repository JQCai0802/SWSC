package com.lxsc;

public enum Code {
    OK("10000","请求成功"),
    ERROR("10001","请求失败"),
    NO_CONFIRM_ORDERS("11000","没有确认订单");
    private String code;
    private String msg;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    Code(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
