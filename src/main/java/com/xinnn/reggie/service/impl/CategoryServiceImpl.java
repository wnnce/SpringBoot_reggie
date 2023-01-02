package com.xinnn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinnn.reggie.base.ReggieException;
import com.xinnn.reggie.mapper.CategoryMapper;
import com.xinnn.reggie.mapper.DishMapper;
import com.xinnn.reggie.pojo.Category;
import com.xinnn.reggie.pojo.Combo;
import com.xinnn.reggie.pojo.Dish;
import com.xinnn.reggie.service.CategoryService;
import com.xinnn.reggie.service.ComboService;
import com.xinnn.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private ComboService comboService;
    @Override
    public Page<Category> getCategoryPage(Integer page, Integer pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        pageInfo = this.page(pageInfo, lambdaQueryWrapper);
        return pageInfo;
    }

    @Override
    public void deleteCategory(Long id) {
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        long dishCount = dishService.count(dishQueryWrapper);
        if(dishCount > 0){
            throw new ReggieException("关联了菜品，无法删除！");
        }
        LambdaQueryWrapper<Combo> comboQueryMapper = new LambdaQueryWrapper<>();
        comboQueryMapper.eq(Combo::getCategoryId, id);
        long comboCount = comboService.count(comboQueryMapper);
        if(comboCount > 0){
            throw new ReggieException("关联了套餐，无法删除！");
        }
        this.removeById(id);
    }

    @Override
    public List<Category> getCategoryListByType(Integer type) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getType, type);
        return this.list(categoryLambdaQueryWrapper);
    }
}
