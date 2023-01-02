package com.xinnn.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinnn.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
