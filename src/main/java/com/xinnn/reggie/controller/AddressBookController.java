package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xinnn.reggie.config.StpUserUtil;
import com.xinnn.reggie.pojo.AddressBook;
import com.xinnn.reggie.service.AddressBookService;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户地址管理
 */
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 根据用户ID获取所有收货地址
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> addressBookList(){
        Long userId = StpUserUtil.getSession().getLong("userId");
        //查询条件 根据userId查询
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId, userId);
        List<AddressBook> addressBookList = addressBookService.list(lambdaQueryWrapper);
        //返回全部收货地址
        return Result.success(addressBookList);
    }

    /**
     * 添加收货地址
     * @param addressBook 封装前端传过来的信息
     * @return
     */
    @PostMapping
    public Result<String> addAddressBook(@RequestBody AddressBook addressBook){
        Long userId = StpUserUtil.getSession().getLong("userId");
        //为收货地址设置userId 然后调用save方法保存
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return Result.success("保存成功");
    }
    /**
     * 设置默认地址
     * @param map 地址ID 和用户ID
     * @return
     */
    @PutMapping("/default")
    public Result<String> updateDefault(@RequestBody Map<String, Long> map){
        Long id = map.get("id");
        Long userId = StpUserUtil.getSession().getLong("userId");
        //获取到地址ID和用户ID后 调用方法
        addressBookService.updateDefaultAddressBook(id, userId);
        return Result.success("ok");
    }

    /**
     * 结账的时候 前端会获取默认的收货地址
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddressBook(){
        //从SaSession中获取用户id
        Long userId = StpUserUtil.getSession().getLong("userId");
        //调用service方法
        AddressBook addressBook = addressBookService.getDefaultAddressBookByUserId(userId);
        return Result.success(addressBook);

    }

    /**
     * 根据id获取收货地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBook(@PathVariable("id") Long id){
        //调用get方法
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 更新收货地址
     * @param addressBook
     * @return
     */
    @PutMapping
    public Result<String> updateAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return Result.success("ok");
    }

    /**
     * 删除收货地址
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteAddressBook(@RequestParam("ids") Long ids){
        addressBookService.removeById(ids);
        return Result.success("ok");
    }
}
