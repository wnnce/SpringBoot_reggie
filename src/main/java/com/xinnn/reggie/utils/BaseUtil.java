package com.xinnn.reggie.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xinnn.reggie.pojo.ShoppingCart;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseUtil {
    /**
     * 拦截器给客户端返回消息 提示用户未登录
     * @param response
     * @throws IOException
     */
    public static void sendMessage(HttpServletResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("code", 0);
        objectNode.put("msg", "NOTLOGIN");
        response.getWriter().print(objectNode);
    }

    /**
     * 对于购物车查询条件的封装
     * @param shoppingCart
     * @return
     */
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
