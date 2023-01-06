package com.xinnn.reggie.controller;

import com.xinnn.reggie.config.StpUserUtil;
import com.xinnn.reggie.pojo.ShoppingCart;
import com.xinnn.reggie.service.ShoppingCartService;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车
 */
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 通过userId返回用户购物车
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> shoppingCartList(){
        Long userId = StpUserUtil.getSession().getLong("userId");
        List<ShoppingCart> shoppingCartList = shoppingCartService.getShoppingCartListByUserId(userId);
        return Result.success(shoppingCartList);
    }

    /**
     * 添加到购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        Long userId = StpUserUtil.getSession().getLong("userId");
        shoppingCart.setUserId(userId);
        //购物车总存在该物品则数量加一 不存在则添加
        shoppingCart = shoppingCartService.addAndUpdateShoppingCartNumber(shoppingCart);
        return Result.success(shoppingCart);
    }

    /**
     * 购物车中物品到数量减少
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result<ShoppingCart> subShoppingCart(@RequestBody ShoppingCart shoppingCart){
        Long userId = StpUserUtil.getSession().getLong("userId");
        shoppingCart.setUserId(userId);
        shoppingCart = shoppingCartService.subShoppingCart(shoppingCart);
        return Result.success(shoppingCart);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result<String> cleanShoppingCart(){
        Long userId = StpUserUtil.getSession().getLong("userId");
        shoppingCartService.removeShoppingCartByUserId(userId);
        return Result.success("ok");
    }
}
