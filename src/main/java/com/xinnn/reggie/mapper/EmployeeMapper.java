package com.xinnn.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinnn.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
