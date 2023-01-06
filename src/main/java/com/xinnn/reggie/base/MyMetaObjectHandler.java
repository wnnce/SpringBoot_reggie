package com.xinnn.reggie.base;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
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
        //从Sa-Token同获取登陆的员工Id
        Long empId = StpUtil.getSession().getLong("empId");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", empId);
        metaObject.setValue("updateUser", empId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //从Sa-Token同获取登陆的员工Id
        Long empId = StpUtil.getSession().getLong("empId");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", empId);
    }
}
