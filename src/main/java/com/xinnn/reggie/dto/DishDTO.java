package com.xinnn.reggie.dto;

import com.xinnn.reggie.pojo.Dish;
import com.xinnn.reggie.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDTO extends Dish {
    //菜品到口味信息
    private List<DishFlavor> flavors = new ArrayList<>();
    //分类名称
    private String categoryName;

    private Integer copies;
}
