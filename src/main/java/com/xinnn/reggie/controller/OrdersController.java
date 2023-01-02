package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xinnn.reggie.dto.OrdersDTO;
import com.xinnn.reggie.dto.QueryDTO;
import com.xinnn.reggie.pojo.Orders;
import com.xinnn.reggie.service.OrdersService;
import com.xinnn.reggie.utils.BaseContext;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/submit")
    public Result<String> submitOrders(@RequestBody Orders orders){
        Long userId = BaseContext.getCurrentUserId();
        orders.setUserId(userId);
        ordersService.submit(orders);
        return Result.success("ok");
    }
    @GetMapping("/userPage")
    public Result<Page<OrdersDTO>> getOrderPage(QueryDTO queryDTO){
        Long userId = BaseContext.getCurrentUserId();
        queryDTO.setUserId(userId);
        Page<OrdersDTO> pageInfo = ordersService.getOrdersPage(queryDTO);
        return Result.success(pageInfo);
    }
    @GetMapping("/page")
    public Result<Page<OrdersDTO>> getOrderPageByEmployee(QueryDTO queryDTO, HttpSession session){
        if (session.getAttribute("employee") != null){
            Page<OrdersDTO> pageInfo = ordersService.getOrdersPage(queryDTO);
            return Result.success(pageInfo);
        }
        return Result.error("NOTLOGIN");
    }
    @PutMapping
    public Result<String> updateOrderStatus(@RequestBody Orders orders){
        ordersService.updateOrdersStatus(orders);
        return Result.success("更新成功");
    }
}
