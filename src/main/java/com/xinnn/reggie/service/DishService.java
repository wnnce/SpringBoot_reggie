package com.xinnn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xinnn.reggie.dto.DishDTO;
import com.xinnn.reggie.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    Page<DishDTO> getDishPage(Integer page, Integer pageSize, String name);
    void addDish(DishDTO dishDTO);
    DishDTO getDish(Long id);
    void updateDish(DishDTO dishDTO);
    void updateDishStatus(List<String> ids, Integer type);
    void deleteDish(List<String> ids);
    List<Dish> getDishListByCategoryId(Long categoryId);
    List<DishDTO> getDishListByCategoryId(Long categoryId, Integer status);
}
