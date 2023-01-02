package com.xinnn.reggie.dto;

import com.xinnn.reggie.pojo.Combo;
import com.xinnn.reggie.pojo.ComboDish;
import lombok.Data;
import java.util.List;

@Data
public class ComboDTO extends Combo {

    private List<ComboDish> setmealDishes;

    private String categoryName;
}
