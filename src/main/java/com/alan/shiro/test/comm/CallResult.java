package com.alan.shiro.test.comm;

public class CallResult<T> {
    private String msg;
    private Integer code;
    private T content;

    public CallResult(){
        this.code =0;
    }
    public CallResult(Integer code){
        this.code=-1;
    }
    public CallResult(Integer code,String msg){
        this.code=code;
        this.msg = msg;
    }
    public CallResult(Integer code,String msg,T content){
        this.code=code;
        this.msg = msg;
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public CallResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public CallResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public T getContent() {
        return content;
    }

    public CallResult setContent(T content) {
        this.content = content;
        return this;
    }

}
