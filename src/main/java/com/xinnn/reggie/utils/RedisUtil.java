package com.xinnn.reggie.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    public static final String REGGIE_KEY = "REGGIE:";
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    public void set(Object key, Object value, Integer time){
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
    }
    public Object get(Object key){
        return redisTemplate.opsForValue().get(key);
    }
    public void remove(Object key){
        redisTemplate.delete(key);
    }
}
