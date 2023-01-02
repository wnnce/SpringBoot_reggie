package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xinnn.reggie.pojo.Employee;
import com.xinnn.reggie.service.EmployeeService;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee, HttpSession session){
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        Employee emp = employeeService.getEmployeeByUserName(employee.getUsername());
        if (emp == null){
            return Result.error("用户不存在！");
        }
        if (!emp.getPassword().equals(password)){
            return Result.error("密码错误！");
        }
        if (emp.getStatus() == 0){
            return Result.error("账号已禁用");
        }
        session.setAttribute("employee", emp.getId());
        return Result.success(emp);
    }
    @PostMapping("/logout")
    public Result<String> logout(HttpSession session){
        session.removeAttribute("employee");
        return Result.success("用户退出");
    }
    @GetMapping
    public Result<Page<Employee>> page(Integer page, Integer pageSize, String name){
        Page<Employee> pageInfo = employeeService.findEmployee(page, pageSize, name);
        return Result.success(pageInfo);
    }
    @PostMapping
    public Result<String> addEmployee(@RequestBody Employee employee, HttpSession session){
        Long empId =(Long) session.getAttribute("employee");
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return Result.success("保存成功！");
    }
    @PutMapping
    public Result<String> updateEmployee(@RequestBody Employee employee, HttpSession session){
        Long empId = (Long) session.getAttribute("employee");
//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return Result.success("更新成功！");
    }
    @GetMapping("/{id}")
    public Result<Employee> getEmployee(@PathVariable("id") String empId){
        Employee employee = employeeService.getById(empId);
        if (employee == null){
            return Result.error("id不正确");
        }
        return Result.success(employee);
    }
}
