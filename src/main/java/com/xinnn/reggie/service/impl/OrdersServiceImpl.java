package com.xinnn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinnn.reggie.base.ReggieException;
import com.xinnn.reggie.dto.OrdersDTO;
import com.xinnn.reggie.dto.QueryDTO;
import com.xinnn.reggie.mapper.OrdersMapper;
import com.xinnn.reggie.pojo.*;
import com.xinnn.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;

    /**
     * 结账方法
     * @param orders
     */
    @Override
    //开启事务
    @Transactional
    public void submit(Orders orders) {
        //先根据用户id获取购物车列表
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, orders.getUserId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        //判断购物车是否为空
        if (shoppingCartList == null || shoppingCartList.size() == 0){
            throw new ReggieException("购物车为空，无法下单！");
        }
        //判断前端提交到地址是否存在
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null){
            throw new ReggieException("地址信息有误，无法下单！");
        }
        //使用雪花算法生成id
        Long ordersId = IdWorker.getId();
        AtomicInteger atomicInteger = new AtomicInteger();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(ShoppingCart shoppingCart : shoppingCartList){
            //添加每一样菜品到金额到总计
            atomicInteger.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());
            //订单详情项
            OrderDetail orderDetail = new OrderDetail();
            //为订单详情设置订单id
            orderDetail.setOrderId(ordersId);
            //其他字段和购物车项相同
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetailList.add(orderDetail);
        }
        User user = userService.getById(orders.getUserId());
        //设置订单属性 设置ID
        orders.setId(ordersId);
        //设置订单总计
        orders.setAmount(new BigDecimal(atomicInteger.get()));
        //设置订单时间和结账时间
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        //设置订单中到商品数量 值为订单详情列表到大小
        orders.setNumber(String.valueOf(orderDetailList.size()));
        orders.setPhone(user.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        //刚生成的订单状态统一为2
        orders.setStatus(2);
        orders.setNumber(String.valueOf(ordersId));
        orders.setUserName(user.getName());
        //设置订单收货地址
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);
        //批量保存订单详情列表
        orderDetailService.saveBatch(orderDetailList);
        shoppingCartService.removeShoppingCartByUserId(orders.getUserId());
    }

    /**
     * 获取订单分页信息 先获取订单信息 在获取订单详情信息
     * @param queryDTO
     * @return
     */
    @Override
    public Page<OrdersDTO> getOrdersPage(QueryDTO queryDTO) {
        Page<Orders> ordersPage = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        Page<OrdersDTO> ordersDTOPage = new Page<>();
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(queryDTO.getUserId() != null, Orders::getUserId, queryDTO.getUserId());
        queryWrapper.like(queryDTO.getNumber() != null, Orders::getNumber, queryDTO.getNumber());
        if (queryDTO.getBeginTime() != null && queryDTO.getEndTime() != null){
            queryWrapper.ge(Orders::getCheckoutTime, queryDTO.getBeginTime());
            queryWrapper.le(Orders::getCheckoutTime, queryDTO.getEndTime());
        }
        queryWrapper.orderByAsc(queryDTO.getUserId() == null, Orders::getCheckoutTime);
        ordersPage = this.page(ordersPage, queryWrapper);
        BeanUtils.copyProperties(ordersPage, ordersDTOPage, "records");
        List<Orders> ordersList = ordersPage.getRecords();
        List<OrdersDTO> ordersDTOList = new ArrayList<>();
        for(Orders orders : ordersList){
            User user = userService.getById(orders.getUserId());
            OrdersDTO ordersDTO = new OrdersDTO();
            BeanUtils.copyProperties(orders, ordersDTO);
            List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailListByOrderId(orders.getId());
            ordersDTO.setOrderDetails(orderDetailList);
            ordersDTO.setUserName(user.getName());
            ordersDTOList.add(ordersDTO);
        }
        ordersDTOPage.setRecords(ordersDTOList);
        return ordersDTOPage;
    }

    /**
     * 更新订单状态
     * @param orders
     */
    @Override
    public void updateOrdersStatus(Orders orders) {
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Orders::getStatus, orders.getStatus()).eq(Orders::getId, orders.getId());
        this.update(updateWrapper);
    }
}
