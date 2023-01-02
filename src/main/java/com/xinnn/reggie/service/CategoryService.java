package com.xinnn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xinnn.reggie.pojo.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    Page<Category> getCategoryPage(Integer page, Integer pageSize);
    void deleteCategory(Long id);
    List<Category> getCategoryListByType(Integer type);
}
