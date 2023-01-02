package com.xinnn.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xinnn.reggie.pojo.AddressBook;
import com.xinnn.reggie.service.AddressBookService;
import com.xinnn.reggie.utils.BaseContext;
import com.xinnn.reggie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public Result<List<AddressBook>> addressBookList(HttpSession session){
        Long userId = (Long) session.getAttribute("user");
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId, userId);
        List<AddressBook> addressBookList = addressBookService.list(lambdaQueryWrapper);
        return Result.success(addressBookList);
    }
    @PostMapping
    public Result<String> addAddressBook(@RequestBody AddressBook addressBook){
        Long userId = BaseContext.getCurrentUserId();
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return Result.success("保存成功");
    }
    @PutMapping("/default")
    public Result<String> updateDefault(@RequestBody Map<String, Long> map){
        Long id = map.get("id");
        Long userId = BaseContext.getCurrentUserId();
        addressBookService.updateDefaultAddressBook(id, userId);
        return Result.success("ok");
    }
    @GetMapping("/default")
    public Result<AddressBook> getDefaultAddressBook(){
        Long userId = BaseContext.getCurrentUserId();
        AddressBook addressBook = addressBookService.getDefaultAddressBookByUserId(userId);
        return Result.success(addressBook);

    }
    @GetMapping("/{id}")
    public Result<AddressBook> getAddressBook(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }
    @PutMapping
    public Result<String> updateAddressBook(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return Result.success("ok");
    }
    @DeleteMapping
    public Result<String> deleteAddressBook(@RequestParam("ids") Long ids){
        addressBookService.removeById(ids);
        return Result.success("ok");
    }
}
