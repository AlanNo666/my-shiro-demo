package com.alan.shiro.test.web.base;

import com.alan.shiro.test.comm.CallResult;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String doDealWithException(final Exception ex){
        ex.printStackTrace();
        if(ex instanceof AuthorizationException){
            return "权限不足";
        }
        return "";
    }
}
