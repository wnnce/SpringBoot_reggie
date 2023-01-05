package com.xinnn.reggie.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据类
 * @param <T>
 */
@Data
public class Result <T>{
    //状态码
    private Integer code;
    //返回消息
    private String msg;
    //返回数据
    private T data;
    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setCode(1);
        result.setData(data);
        return result;
    }
    public static <T> Result<T> error(String msg){
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setMsg(msg);
        return result;
    }
}
