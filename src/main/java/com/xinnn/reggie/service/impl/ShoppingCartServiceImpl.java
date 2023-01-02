package com.xinnn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinnn.reggie.mapper.ShoppingCartMapper;
import com.xinnn.reggie.pojo.ShoppingCart;
import com.xinnn.reggie.service.ShoppingCartService;
import com.xinnn.reggie.utils.BaseUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public ShoppingCart addShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        this.save(shoppingCart);
        return shoppingCart;
    }

    /**
     * 更新购物车中的数量
     * @param shoppingCart
     * @param operate 需要进行的操作 true表示增加 false表示删除
     * @return
     */
    @Override
    public ShoppingCart updateShoppingCartNumber(ShoppingCart shoppingCart, boolean operate) {
        String sql = null;
        if (operate){
            sql = "number = number + 1";
        }else{
            sql = "number = number - 1";
        }
        LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        if (shoppingCart.getDishId() == null){
            updateWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }else {
            updateWrapper.eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        }
        updateWrapper.setSql(sql);
        this.update(updateWrapper);
        shoppingCart = this.getById(shoppingCart.getId());
        return shoppingCart;
    }

    @Override
    public ShoppingCart addAndUpdateShoppingCartNumber(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = BaseUtil.checkQueryWrapper(shoppingCart);
        ShoppingCart queryShoppingCart = this.getOne(queryWrapper);
        if(queryShoppingCart == null){
            queryShoppingCart = this.addShoppingCart(shoppingCart);
        }else{
            queryShoppingCart = this.updateShoppingCartNumber(queryShoppingCart, true);
        }
        return queryShoppingCart;
    }

    @Override
    public ShoppingCart subShoppingCart(ShoppingCart shoppingCart) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = BaseUtil.checkQueryWrapper(shoppingCart);
        ShoppingCart queryShoppingCart = this.getOne(queryWrapper);
        if(queryShoppingCart.getNumber() > 1){
            queryShoppingCart = this.updateShoppingCartNumber(queryShoppingCart, false);
        }else{
            this.removeById(queryShoppingCart);
            queryShoppingCart.setNumber(0);
        }
        return queryShoppingCart;
    }

    @Override
    public List<ShoppingCart> getShoppingCartListByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId).orderByAsc(ShoppingCart::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public void removeShoppingCartByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        this.remove(queryWrapper);
    }
}
