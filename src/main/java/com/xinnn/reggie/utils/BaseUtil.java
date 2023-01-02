package com.xinnn.reggie.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xinnn.reggie.pojo.ShoppingCart;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseUtil {
    public static void sendMessage(HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code", 0);
        objectNode.put("msg", "NOTLOGIN");
        response.getWriter().print(objectNode);
    }
    public static LambdaQueryWrapper<ShoppingCart> checkQueryWrapper(ShoppingCart shoppingCart){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        if(shoppingCart.getDishId() == null){
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }else{
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }
        return queryWrapper;
    }
}
