package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xinnn.reggie.pojo.Category;
import com.xinnn.reggie.service.CategoryService;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类 套餐
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据page 和pageSize获取分类列表
     * @param page 页码
     * @param pageSize 每一页的记录数
     * @return
     */
    @GetMapping
    public Result<Page<Category>> categoryPage(Integer page, Integer pageSize){
        Page<Category> pageInfo = categoryService.getCategoryPage(page, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody Category category){
        categoryService.save(category);
        return Result.success("保存成功！");
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Result<String> deleteCategory(@RequestParam("ids") Long id){
        categoryService.deleteCategory(id);
        return Result.success("删除成功");
    }

    /**
     * 更新分类
     * @param category
     * @return
     */
    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("更新成功！");
    }

    /**
     * 通过分类的具体状态来获取分类列表
     * @param type
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(Integer type){
        List<Category> categoryList = null;
        //如果type不等于空就使用条件查询 为空则返回所有
        if (type != null){
            categoryList = categoryService.getCategoryListByType(type);
        }else {
            categoryList = categoryService.list();
        }
        return Result.success(categoryList);
    }
}
