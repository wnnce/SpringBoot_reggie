package com.xinnn.reggie.dto;


import com.xinnn.reggie.pojo.OrderDetail;
import com.xinnn.reggie.pojo.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDTO extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;
    //订单中的订单详细信息
    private List<OrderDetail> orderDetails;
	
}
