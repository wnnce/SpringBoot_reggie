package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xinnn.reggie.dto.OrdersDTO;
import com.xinnn.reggie.dto.QueryDTO;
import com.xinnn.reggie.pojo.Orders;
import com.xinnn.reggie.service.OrdersService;
import com.xinnn.reggie.utils.BaseContext;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 订单
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 结账方法
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submitOrders(@RequestBody Orders orders){
        //为订单封装userId
        Long userId = BaseContext.getCurrentUserId();
        orders.setUserId(userId);
        ordersService.submit(orders);
        return Result.success("ok");
    }

    /**
     * 用户查看历史订单
     * @param queryDTO
     * @return
     */
    @GetMapping("/userPage")
    public Result<Page<OrdersDTO>> getOrderPage(QueryDTO queryDTO){
        //通过userId获取订单列表
        Long userId = BaseContext.getCurrentUserId();
        queryDTO.setUserId(userId);
        Page<OrdersDTO> pageInfo = ordersService.getOrdersPage(queryDTO);
        return Result.success(pageInfo);
    }

    /**
     * 管理员管理订单
     * @param queryDTO
     * @param session
     * @return
     */
    @GetMapping("/page")
    public Result<Page<OrdersDTO>> getOrderPageByEmployee(QueryDTO queryDTO, HttpSession session){
        if (session.getAttribute("employee") != null){
            //只有管理员登陆了才会返回订单列表信息
            Page<OrdersDTO> pageInfo = ordersService.getOrdersPage(queryDTO);
            return Result.success(pageInfo);
        }
        return Result.error("NOTLOGIN");
    }

    /**
     * 更新订单状态
     * @param orders
     * @return
     */
    @PutMapping
    public Result<String> updateOrderStatus(@RequestBody Orders orders){
        ordersService.updateOrdersStatus(orders);
        return Result.success("更新成功");
    }
}
