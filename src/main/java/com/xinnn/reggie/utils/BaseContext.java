package com.xinnn.reggie.utils;

/**
 * 在本地线程中设置和获取userId 配合拦截器使用
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static Long getCurrentUserId(){
        return threadLocal.get();
    }
    public static void setCurrentUserId(Long id){
        threadLocal.set(id);
    }
}
