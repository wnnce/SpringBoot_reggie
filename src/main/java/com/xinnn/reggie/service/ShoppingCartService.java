package com.xinnn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinnn.reggie.pojo.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    ShoppingCart addShoppingCart(ShoppingCart shoppingCart);
    ShoppingCart updateShoppingCartNumber(ShoppingCart shoppingCart, boolean operate);
    ShoppingCart addAndUpdateShoppingCartNumber(ShoppingCart shoppingCart);
    ShoppingCart subShoppingCart(ShoppingCart shoppingCart);
    List<ShoppingCart> getShoppingCartListByUserId(Long userId);
    void removeShoppingCartByUserId(Long userId);
}
