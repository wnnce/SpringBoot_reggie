package com.xinnn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xinnn.reggie.pojo.Employee;

public interface EmployeeService extends IService<Employee> {
    Page<Employee> findEmployee(Integer page, Integer pageSize, String name);
    Employee getEmployeeByUserName(String username);
}
