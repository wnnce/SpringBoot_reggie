package com.xinnn.reggie.utils;

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static Long getCurrentUserId(){
        return threadLocal.get();
    }
    public static void setCurrentUserId(Long id){
        threadLocal.set(id);
    }
}
