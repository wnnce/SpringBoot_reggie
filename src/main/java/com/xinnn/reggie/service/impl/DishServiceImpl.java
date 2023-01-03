package com.xinnn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinnn.reggie.dto.DishDTO;
import com.xinnn.reggie.mapper.CategoryMapper;
import com.xinnn.reggie.mapper.DishMapper;
import com.xinnn.reggie.pojo.Dish;
import com.xinnn.reggie.pojo.DishFlavor;
import com.xinnn.reggie.service.DishFlavorService;
import com.xinnn.reggie.service.DishService;
import com.xinnn.reggie.utils.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public Page<DishDTO> getDishPage(Integer page, Integer pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.like(name != null, Dish::getName, name);
        dishPage = this.page(dishPage, dishQueryWrapper);
        Page<DishDTO> dishDTOPage = new Page<>();
        BeanUtils.copyProperties(dishPage, dishDTOPage, "records");
        List<Dish> dishList = dishPage.getRecords();
        List<DishDTO> dishDTOList = new ArrayList<>();
        for(Dish dish : dishList){
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(dish, dishDTO);
            dishDTO.setCategoryName(categoryMapper.getCategoryNameById(dish.getCategoryId()));
            dishDTOList.add(dishDTO);
        }
        dishDTOPage.setRecords(dishDTOList);
        return dishDTOPage;
    }

    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        this.save(dishDTO);
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        for(DishFlavor dishFlavor : dishFlavorList){
            dishFlavor.setDishId(dishDTO.getId());
            dishFlavorService.save(dishFlavor);
        }
        //删除redis中的缓存
        redisUtil.remove(RedisUtil.REGGIE_KEY + "DISH:" + dishDTO.getCategoryId() + ":1");
    }

    @Override
    public DishDTO getDish(Long id) {
        DishDTO dishDTO = new DishDTO();
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
        String categoryName = categoryMapper.getCategoryNameById(dish.getCategoryId());
        BeanUtils.copyProperties(dish, dishDTO);
        dishDTO.setFlavors(dishFlavorList);
        dishDTO.setCategoryName(categoryName);
        return dishDTO;
    }

    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        this.updateById(dishDTO);
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDTO.getId());
        dishFlavorService.remove(lambdaQueryWrapper);
        for(DishFlavor dishFlavor : dishFlavorList){
            dishFlavor.setDishId(dishDTO.getId());
            dishFlavorService.save(dishFlavor);
        }
        //删除redis中的缓存
        redisUtil.remove(RedisUtil.REGGIE_KEY + "DISH:" + dishDTO.getCategoryId() + ":1");
    }

    @Override
    @Transactional
    public void updateDishStatus(List<String> ids, Integer type) {
        for (String id : ids){
            Long dishId = Long.parseLong(id);
            //删除redis中的缓存
            Dish dish = this.getById(dishId);
            redisUtil.remove(RedisUtil.REGGIE_KEY + "DISH:" + dish.getCategoryId() + ":1");
            LambdaUpdateWrapper<Dish> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(Dish::getStatus, type).eq(Dish::getId, dishId);
            this.update(lambdaUpdateWrapper);
        }
    }

    @Override
    @Transactional
    public void deleteDish(List<String> ids) {
        for (String id : ids){
            Long dishId = Long.parseLong(id);
            //删除redis中的缓存
            Dish dish = this.getById(dishId);
            redisUtil.remove(RedisUtil.REGGIE_KEY + "DISH:" + dish.getCategoryId() + ":1");
            LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorQueryWrapper.eq(DishFlavor::getDishId, dishId);
            dishFlavorService.remove(dishFlavorQueryWrapper);
            this.removeById(dishId);
        }
    }

    @Override
    public List<Dish> getDishListByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId, categoryId);
        List<Dish> dishList = this.list(lambdaQueryWrapper);
        return dishList;
    }

    @Override
    public List<DishDTO> getDishListByCategoryId(Long categoryId, Integer status) {
        List<DishDTO> dishDTOList = null;
        //先从缓存中获取
        String key = RedisUtil.REGGIE_KEY + "DISH:" + categoryId + ":" + status;
        dishDTOList = (List<DishDTO>) redisUtil.get(key);
        if (dishDTOList != null){
            return dishDTOList;
        }

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId, categoryId).eq(Dish::getStatus, status);
        List<Dish> dishList = this.list(lambdaQueryWrapper);
        dishDTOList = new ArrayList<>();
        for(Dish dish : dishList){
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(dish, dishDTO);
            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
            List<DishFlavor> flavors = dishFlavorService.list(flavorLambdaQueryWrapper);
            dishDTO.setFlavors(flavors);
            dishDTOList.add(dishDTO);
        }
        //将列表放到缓存里面
        redisUtil.set(key, dishDTOList, 60);
        return dishDTOList;
    }
}
