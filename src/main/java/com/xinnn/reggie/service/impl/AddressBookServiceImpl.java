package com.xinnn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinnn.reggie.base.ReggieException;
import com.xinnn.reggie.mapper.AddressBookMapper;
import com.xinnn.reggie.pojo.AddressBook;
import com.xinnn.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    public void updateDefaultAddressBook(Long id, Long userId) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(AddressBook::getIsDefault, 0).eq(AddressBook::getUserId, userId);
        this.update(updateWrapper);
        updateWrapper.clear();
        updateWrapper.set(AddressBook::getIsDefault, 1).eq(AddressBook::getId, id);
        this.update(updateWrapper);
    }

    @Override
    public AddressBook getDefaultAddressBookByUserId(Long userId) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = this.getOne(queryWrapper);
        if (addressBook == null){
            throw new ReggieException("当前用户没有默认收货地址！");
        }
        return addressBook;
    }
}
