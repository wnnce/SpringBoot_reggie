package com.xinnn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xinnn.reggie.dto.OrdersDTO;
import com.xinnn.reggie.dto.QueryDTO;
import com.xinnn.reggie.pojo.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
    Page<OrdersDTO> getOrdersPage(QueryDTO queryDTO);
    void updateOrdersStatus(Orders orders);
}
