package com.xinnn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinnn.reggie.pojo.User;

public interface UserService extends IService<User> {
    User getUserByPhone(String phone);
}
