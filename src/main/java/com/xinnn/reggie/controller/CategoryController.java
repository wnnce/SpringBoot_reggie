package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xinnn.reggie.pojo.Category;
import com.xinnn.reggie.service.CategoryService;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public Result<Page<Category>> categoryPage(Integer page, Integer pageSize){
        Page<Category> pageInfo = categoryService.getCategoryPage(page, pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping
    public Result<String> addCategory(@RequestBody Category category){
        categoryService.save(category);
        return Result.success("保存成功！");
    }
    @DeleteMapping
    public Result<String> deleteCategory(@RequestParam("ids") Long id){
        categoryService.deleteCategory(id);
        return Result.success("删除成功");
    }
    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("更新成功！");
    }
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(Integer type){
        List<Category> categoryList = null;
        if (type != null){
            categoryList = categoryService.getCategoryListByType(type);
        }else {
            categoryList = categoryService.list();
        }
        return Result.success(categoryList);
    }
}
