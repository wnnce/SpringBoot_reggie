package com.xinnn.reggie.base;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xinnn.reggie.utils.BaseContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatis-plus公共字段处理类
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        //从本地线程获取员工ID
        Long empId = BaseContext.getCurrentUserId();
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", empId);
        metaObject.setValue("updateUser", empId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //从本地线程获取用户ID
        Long empId = BaseContext.getCurrentUserId();
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", empId);
    }
}
