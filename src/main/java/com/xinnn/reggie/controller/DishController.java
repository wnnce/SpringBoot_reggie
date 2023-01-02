package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xinnn.reggie.dto.DishDTO;
import com.xinnn.reggie.pojo.Dish;
import com.xinnn.reggie.service.DishService;
import com.xinnn.reggie.utils.Result;
import com.xinnn.reggie.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @GetMapping
    public Result<Page<DishDTO>> getDishPage(Integer page, Integer pageSize, String name){
        Page<DishDTO> pageInfo = dishService.getDishPage(page, pageSize, name);
        return Result.success(pageInfo);
    }
    @PostMapping
    public Result<String> addDish(@RequestBody DishDTO dishDTO){
        dishService.addDish(dishDTO);
        return Result.success("保存成功");
    }
    @GetMapping("/{id}")
    public Result<DishDTO> getDish(@PathVariable("id") Long id){
        DishDTO dishDTO = dishService.getDish(id);
        return Result.success(dishDTO);
    }
    @PutMapping
    public Result<String> updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        return Result.success("更新成功");
    }
    @PostMapping("/status/{type}")
    public Result<String> updateDishStatus(@PathVariable("type")Integer type, @RequestParam("ids") String dishIds){
        List<String> ids = StringUtil.splitString(dishIds, ",");
        dishService.updateDishStatus(ids, type);
        return Result.success("更新成功");
    }
    @DeleteMapping
    public Result<String> deleteDish(@RequestParam("ids") String dishIds){
        List<String> ids = StringUtil.splitString(dishIds, ",");
        dishService.deleteDish(ids);
        return Result.success("删除成功！");
    }
    @GetMapping("/list")
    public Result<Object> getDishList(Long categoryId, Integer status){
        if (status == null){
            List<Dish> dishList = dishService.getDishListByCategoryId(categoryId);
            return Result.success(dishList);
        }else{
            List<DishDTO> dishList = dishService.getDishListByCategoryId(categoryId, status);
            return Result.success(dishList);
        }
    }
}
