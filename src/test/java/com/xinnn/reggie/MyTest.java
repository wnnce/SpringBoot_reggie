package com.xinnn.reggie;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xinnn.reggie.pojo.Category;
import com.xinnn.reggie.pojo.Employee;
import com.xinnn.reggie.service.CategoryService;
import com.xinnn.reggie.service.EmployeeService;
import com.xinnn.reggie.utils.AliyunEmailCode;
import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

@SpringBootTest
public class MyTest {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private CategoryService categoryService;
    @Test
    public void testLogin(){
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername, "admin");
        Employee employee = employeeService.getOne(lambdaQueryWrapper);
        System.out.println(employee);
    }
    @Test
    public void testSelectName(){
        String id = "1397844263642378242";
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Category::getName).eq(Category::getId, id);
        Category category = categoryService.getOne(lambdaQueryWrapper);
        System.out.println(category.getName());
    }
    @Test
    public void testAliyunEmail() throws Exception {
        Integer code = new Random().nextInt(100000, 999999);
        AliyunEmailCode.main("ranchangxin@gmail.com", String.valueOf(code));
    }
}
