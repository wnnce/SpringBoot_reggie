package com.xinnn.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xinnn.reggie.pojo.AddressBook;

public interface AddressBookService extends IService<AddressBook> {
    void updateDefaultAddressBook(Long id, Long userId);
    AddressBook getDefaultAddressBookByUserId(Long userId);
}
