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

/**
 * 菜品
 */
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 获取菜品分页
     * @param page 页码
     * @param pageSize 每页记录数
     * @param name 搜索关键字
     * @return
     */
    @GetMapping
    public Result<Page<DishDTO>> getDishPage(Integer page, Integer pageSize, String name){
        Page<DishDTO> pageInfo = dishService.getDishPage(page, pageSize, name);
        return Result.success(pageInfo);
    }

    /**
     * 保存菜品
     * @param dishDTO 前端数据的封装类 包含菜品信息和菜品的口味信息
     * @return
     */
    @PostMapping
    public Result<String> addDish(@RequestBody DishDTO dishDTO){
        dishService.addDish(dishDTO);
        return Result.success("保存成功");
    }

    /**
     * 通过菜品id获取菜品详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDTO> getDish(@PathVariable("id") Long id){
        DishDTO dishDTO = dishService.getDish(id);
        return Result.success(dishDTO);
    }

    /**
     * 更新菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    public Result<String> updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateDish(dishDTO);
        return Result.success("更新成功");
    }

    /**
     * 批量更新菜品状态
     * @param type
     * @param dishIds
     * @return
     */
    @PostMapping("/status/{type}")
    public Result<String> updateDishStatus(@PathVariable("type")Integer type, @RequestParam("ids") String dishIds){
        List<String> ids = StringUtil.splitString(dishIds, ",");
        dishService.updateDishStatus(ids, type);
        return Result.success("更新成功");
    }

    /**
     * 批量删除菜品
     * @param dishIds
     * @return
     */
    @DeleteMapping
    public Result<String> deleteDish(@RequestParam("ids") String dishIds){
        List<String> ids = StringUtil.splitString(dishIds, ",");
        dishService.deleteDish(ids);
        return Result.success("删除成功！");
    }

    /**
     * 根据菜品分类和状态获取菜品列表
     * @param categoryId
     * @param status
     * @return
     */
    @GetMapping("/list")
    public Result<Object> getDishList(Long categoryId, Integer status){
        //如果状态等于空就返回Dish 不为空则是移动端获取 需要一起返回口味信息 需要返回DishDTO类型
        if (status == null){
            List<Dish> dishList = dishService.getDishListByCategoryId(categoryId);
            return Result.success(dishList);
        }else{
            List<DishDTO> dishList = dishService.getDishListByCategoryId(categoryId, status);
            return Result.success(dishList);
        }
    }
}
