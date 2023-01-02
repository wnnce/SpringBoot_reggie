package com.xinnn.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinnn.reggie.mapper.DishFlavorMapper;
import com.xinnn.reggie.pojo.DishFlavor;
import com.xinnn.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
