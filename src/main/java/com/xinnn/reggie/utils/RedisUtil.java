package com.xinnn.reggie.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis工具类 封装了set get remove方法
 */
@Component
public class RedisUtil {
    //项目redis key的统一前缀
    public static final String REGGIE_KEY = "REGGIE:";
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public void set(Object key, Object value, Integer time){
        //时间单位默认为分钟
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }
    public Object get(Object key){
        return redisTemplate.opsForValue().get(key);
    }
    public void remove(Object key){
        redisTemplate.delete(key);
    }
}
