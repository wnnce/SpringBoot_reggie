package com.xinnn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinnn.reggie.pojo.OrderDetail;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    List<OrderDetail> getOrderDetailListByOrderId(Long orderId);
}
