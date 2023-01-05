package com.xinnn.reggie.base;

/**
 * 自定义异常处理类
 */
public class ReggieException extends RuntimeException{
    public ReggieException(String message){
        super(message);
    }
}
