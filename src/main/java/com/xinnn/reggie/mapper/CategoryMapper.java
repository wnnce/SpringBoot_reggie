package com.xinnn.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinnn.reggie.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    @Select("select name from category where id = #{id}")
    String getCategoryNameById(@Param("id")Long id);
}
