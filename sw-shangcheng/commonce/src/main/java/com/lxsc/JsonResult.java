package com.lxsc;

public class JsonResult<T> {
    private Code code;
    private String codeStr;
    private String msg;
    private T result;

    public JsonResult() {
    }

    public JsonResult(Code code, T result) {
        this(code,code.getMsg(),result);
    }

    public JsonResult(Code code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public String getCode() {
        return codeStr;
    }

    public void setCode(String code) {
        this.codeStr = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
