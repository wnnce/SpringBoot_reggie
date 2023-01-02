package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xinnn.reggie.pojo.ShoppingCart;
import com.xinnn.reggie.service.ShoppingCartService;
import com.xinnn.reggie.utils.BaseContext;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @GetMapping("/list")
    public Result<List<ShoppingCart>> shoppingCartList(){
        Long userId = BaseContext.getCurrentUserId();
        List<ShoppingCart> shoppingCartList = shoppingCartService.getShoppingCartListByUserId(userId);
        return Result.success(shoppingCartList);
    }
    @PostMapping("/add")
    public Result<ShoppingCart> addShoppingCart(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentUserId();
        shoppingCart.setUserId(userId);
        shoppingCart = shoppingCartService.addAndUpdateShoppingCartNumber(shoppingCart);
        return Result.success(shoppingCart);
    }
    @PostMapping("/sub")
    public Result<ShoppingCart> subShoppingCart(@RequestBody ShoppingCart shoppingCart){
        Long userId = BaseContext.getCurrentUserId();
        shoppingCart.setUserId(userId);
        shoppingCart = shoppingCartService.subShoppingCart(shoppingCart);
        return Result.success(shoppingCart);
    }
    @DeleteMapping("/clean")
    public Result<String> cleanShoppingCart(){
        Long userId = BaseContext.getCurrentUserId();
        shoppingCartService.removeShoppingCartByUserId(userId);
        return Result.success("ok");
    }
}
