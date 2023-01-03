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

@RestController
@RequestMapping("/setmeal")
public class ComboController {
    @Autowired
    private ComboService comboService;
    @GetMapping
    public Result<Page<ComboDTO>> comboPage(Integer page, Integer pageSize, String name){
        Page<ComboDTO> pageInfo = comboService.getComboPage(page, pageSize, name);
        return Result.success(pageInfo);
    }
    @PostMapping
    public Result<String> addCombo(@RequestBody ComboDTO comboDTO){
        comboService.addCombo(comboDTO);
        return Result.success("保存成功");
    }
    @GetMapping("/{id}")
    public Result<ComboDTO> getCombo(@PathVariable("id") Long id){
        ComboDTO comboDTO = comboService.getCombo(id);
        return Result.success(comboDTO);
    }
    @PutMapping
    public Result<String> updateCombo(@RequestBody ComboDTO comboDTO){
        comboService.updateCombo(comboDTO);
        return Result.success("更新成功！");
    }
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable("status") Integer status, @RequestParam("ids")String comboIds){
        List<String> ids = StringUtil.splitString(comboIds, ",");
        for(String id : ids){
            comboService.updateComboStatus(id, status);
        }
        return Result.success("更新成功");
    }
    @DeleteMapping
    public Result<String> deleteCombo(@RequestParam("ids")String comboIds){
        List<String> ids = StringUtil.splitString(comboIds, ",");
        for (String id : ids){
            comboService.deleteCombo(id);
        }
        return Result.success("删除成功");
    }
    @GetMapping("/list")
    public Result<List<Combo>> comboList(Long categoryId, Integer status){
        List<Combo> comboList = comboService.getComboListByCategoryId(categoryId, status);
        return Result.success(comboList);
    }
}
