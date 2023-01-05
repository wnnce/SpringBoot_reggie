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

    /**
     * 获取分类分页信息
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public Page<Category> getCategoryPage(Integer page, Integer pageSize) {
        //使用mybatis-plus内置的page对象
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //排序
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        pageInfo = this.page(pageInfo, lambdaQueryWrapper);
        return pageInfo;
    }

    /**
     * 删除分类
     * @param id
     */
    @Override
    public void deleteCategory(Long id) {
        //先查询是否关联了菜品 如果有关联则抛出异常
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        long dishCount = dishService.count(dishQueryWrapper);
        if(dishCount > 0){
            throw new ReggieException("关联了菜品，无法删除！");
        }
        //在查询是否关联了套餐 有一样抛出异常
        LambdaQueryWrapper<Combo> comboQueryMapper = new LambdaQueryWrapper<>();
        comboQueryMapper.eq(Combo::getCategoryId, id);
        long comboCount = comboService.count(comboQueryMapper);
        if(comboCount > 0){
            throw new ReggieException("关联了套餐，无法删除！");
        }
        //如果都没有 那么可以删除
        this.removeById(id);
    }

    /**
     * 根据分类状态获取分类列表
     * @param type
     * @return
     */
    @Override
    public List<Category> getCategoryListByType(Integer type) {
        //条件查询
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getType, type);
        return this.list(categoryLambdaQueryWrapper);
    }
}
