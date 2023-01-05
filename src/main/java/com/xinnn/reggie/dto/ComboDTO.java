package com.xinnn.reggie.dto;

import com.xinnn.reggie.pojo.Combo;
import com.xinnn.reggie.pojo.ComboDish;
import lombok.Data;
import java.util.List;

@Data
public class ComboDTO extends Combo {
    //套餐中到菜品信息
    private List<ComboDish> setmealDishes;
    //分类名字
    private String categoryName;
}
