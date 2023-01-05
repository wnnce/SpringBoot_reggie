package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xinnn.reggie.dto.ComboDTO;
import com.xinnn.reggie.pojo.Combo;
import com.xinnn.reggie.service.ComboService;
import com.xinnn.reggie.utils.Result;
import com.xinnn.reggie.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐
 */
@RestController
@RequestMapping("/setmeal")
public class ComboController {
    @Autowired
    private ComboService comboService;

    /**
     * 获取套餐分页
     * @param page 页码
     * @param pageSize 每页记录数
     * @param name 搜索关键字
     * @return
     */
    @GetMapping
    public Result<Page<ComboDTO>> comboPage(Integer page, Integer pageSize, String name){
        Page<ComboDTO> pageInfo = comboService.getComboPage(page, pageSize, name);
        return Result.success(pageInfo);
    }

    /**
     * 添加套餐
     * @param comboDTO 封装前端传过来的参数 包含套餐信息和套餐菜品信息
     * @return
     */
    @PostMapping
    public Result<String> addCombo(@RequestBody ComboDTO comboDTO){
        comboService.addCombo(comboDTO);
        return Result.success("保存成功");
    }

    /**
     * 根据id获取套餐的详细信息
     * @param id 套餐id
     * @return 返回ComboDTO 包含套餐信息和套餐内的菜品信息
     */
    @GetMapping("/{id}")
    public Result<ComboDTO> getCombo(@PathVariable("id") Long id){
        ComboDTO comboDTO = comboService.getCombo(id);
        return Result.success(comboDTO);
    }

    /**
     * 更新套餐
     * @param comboDTO
     * @return
     */
    @PutMapping
    public Result<String> updateCombo(@RequestBody ComboDTO comboDTO){
        comboService.updateCombo(comboDTO);
        return Result.success("更新成功！");
    }

    /**
     * 单个或者批量更新套餐状态
     * @param status 状态信息
     * @param comboIds 需要更改状态的套餐ID 多个ID使用 , 分隔
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable("status") Integer status, @RequestParam("ids")String comboIds){
        //调用工具方法 将String根据分隔关键字转为数组
        List<String> ids = StringUtil.splitString(comboIds, ",");
        //遍历数组
        for(String id : ids){
            //更新套餐状态
            comboService.updateComboStatus(id, status);
        }
        return Result.success("更新成功");
    }

    /**
     * 批量删除或删除单个套餐
     * @param comboIds
     * @return
     */
    @DeleteMapping
    public Result<String> deleteCombo(@RequestParam("ids")String comboIds){
        //根据分隔关键字转为数组 不包含关键字则直接将字符串添加到数组
        List<String> ids = StringUtil.splitString(comboIds, ",");
        for (String id : ids){
            //删除套餐
            comboService.deleteCombo(id);
        }
        return Result.success("删除成功");
    }

    /**
     * 根据分类id和套餐状态获取套餐列表
     * @param categoryId 套餐的分类id
     * @param status 套餐的状态
     * @return
     */
    @GetMapping("/list")
    public Result<List<Combo>> comboList(Long categoryId, Integer status){
        List<Combo> comboList = comboService.getComboListByCategoryId(categoryId, status);
        return Result.success(comboList);
    }
}
