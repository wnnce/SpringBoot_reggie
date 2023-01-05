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
    /**
     * 更新默认地址
     * @param id 地址id
     * @param userId 用户ID
     */
    @Override
    public void updateDefaultAddressBook(Long id, Long userId) {
        //先将该用户的所有地址IsDefault字段设置为 0
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(AddressBook::getIsDefault, 0).eq(AddressBook::getUserId, userId);
        this.update(updateWrapper);
        updateWrapper.clear();
        //在通过地址id将该地址设置为默认地址
        updateWrapper.set(AddressBook::getIsDefault, 1).eq(AddressBook::getId, id);
        this.update(updateWrapper);
    }

    /**
     * 获取默认地址
     * @param userId
     * @return
     */
    @Override
    public AddressBook getDefaultAddressBookByUserId(Long userId) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        //通过地址用户ID和IsDefault字段匹配获取
        queryWrapper.eq(AddressBook::getUserId, userId).eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = this.getOne(queryWrapper);
        if (addressBook == null){
            //抛出自定义异常
            throw new ReggieException("当前用户没有默认收货地址！");
        }
        return addressBook;
    }
}
