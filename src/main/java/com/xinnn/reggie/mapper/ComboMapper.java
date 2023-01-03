package com.xinnn.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinnn.reggie.pojo.Combo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ComboMapper extends BaseMapper<Combo> {
    @Select("select category_id from setmeal where id = #{id}")
    Long getCategoryIdById(@Param("id") Long id);
}
