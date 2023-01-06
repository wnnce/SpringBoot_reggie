package com.xinnn.reggie.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xinnn.reggie.pojo.Employee;
import com.xinnn.reggie.service.EmployeeService;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登陆
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee){
        String password = employee.getPassword();
        //对密码进行md5加密
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
        //将登陆的员工id保存到session
        StpUtil.login(emp.getId());
        StpUtil.getSession().set("empId", emp.getId());
        return Result.success(emp);
    }

    /**
     * 员工退出登陆
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(){
        Long empId = StpUtil.getSession().getLong("empId");
        StpUtil.logout(empId);
        StpUtil.getSession().delete("empId");
        return Result.success("用户退出");
    }

    /**
     * 获取员工分页信息
     * @param page 页码
     * @param pageSize 每页记录数
     * @param name 搜索关键字
     * @return
     */
    @GetMapping
    public Result<Page<Employee>> page(Integer page, Integer pageSize, String name){
        Page<Employee> pageInfo = employeeService.findEmployee(page, pageSize, name);
        return Result.success(pageInfo);
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping
    public Result<String> addEmployee(@RequestBody Employee employee){
        //默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return Result.success("保存成功！");
    }

    /**
     * 更新员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public Result<String> updateEmployee(@RequestBody Employee employee){
//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return Result.success("更新成功！");
    }

    /**
     * 根据id获取员工到详细信息
     * @param empId
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getEmployee(@PathVariable("id") String empId){
        Employee employee = employeeService.getById(empId);
        if (employee == null){
            return Result.error("id不正确");
        }
        return Result.success(employee);
    }
}
