package com.xinnn.reggie.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result <T>{
    private Integer code;
    private String msg;
    private T data;
    private Map<String, Object> map = new HashMap<>();
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
