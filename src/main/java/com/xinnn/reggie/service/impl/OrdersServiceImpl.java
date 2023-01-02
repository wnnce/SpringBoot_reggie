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
    @Override
    @Transactional
    public void submit(Orders orders) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, orders.getUserId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        if (shoppingCartList == null || shoppingCartList.size() == 0){
            throw new ReggieException("购物车为空，无法下单！");
        }
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null){
            throw new ReggieException("地址信息有误，无法下单！");
        }
        Long ordersId = IdWorker.getId();
        AtomicInteger atomicInteger = new AtomicInteger();
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for(ShoppingCart shoppingCart : shoppingCartList){
            atomicInteger.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(ordersId);
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
        orders.setId(ordersId);
        orders.setAmount(new BigDecimal(atomicInteger.get()));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setNumber(String.valueOf(orderDetailList.size()));
        orders.setPhone(user.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setStatus(2);
        orders.setNumber(String.valueOf(ordersId));
        orders.setUserName(user.getName());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);
        orderDetailService.saveBatch(orderDetailList);
        shoppingCartService.removeShoppingCartByUserId(orders.getUserId());
    }

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

    @Override
    public void updateOrdersStatus(Orders orders) {
        LambdaUpdateWrapper<Orders> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Orders::getStatus, orders.getStatus()).eq(Orders::getId, orders.getId());
        this.update(updateWrapper);
    }
}
