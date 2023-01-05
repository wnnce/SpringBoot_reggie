package com.xinnn.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinnn.reggie.dto.ComboDTO;
import com.xinnn.reggie.mapper.CategoryMapper;
import com.xinnn.reggie.mapper.ComboMapper;
import com.xinnn.reggie.pojo.Combo;
import com.xinnn.reggie.pojo.ComboDish;
import com.xinnn.reggie.service.ComboDishService;
import com.xinnn.reggie.service.ComboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "REGGIE:COMBO:")
public class ComboServiceImpl extends ServiceImpl<ComboMapper, Combo> implements ComboService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ComboDishService comboDishService;
    @Autowired
    private ComboMapper comboMapper;

    /**
     * 获取套餐分页信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<ComboDTO> getComboPage(Integer page, Integer pageSize, String name) {
        //需要返回ComboDTO 所有创建两个page对象
        Page<Combo> comboPage = new Page<>(page, pageSize);
        Page<ComboDTO> combaDTOPage = new Page<>();
        //先查询出分页数据
        LambdaQueryWrapper<Combo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //条件查询 name ！= null 才会在sql语句中添加该条件
        lambdaQueryWrapper.eq(name != null, Combo::getName, name);
        comboPage = this.page(comboPage, lambdaQueryWrapper);
        //复制对象 忽略records属性
        BeanUtils.copyProperties(comboPage, combaDTOPage, "records");
        //取得套餐列表
        List<Combo> comboList = comboPage.getRecords();
        List<ComboDTO> comboDTOList = new ArrayList<>();
        for (Combo combo : comboList){
            ComboDTO comboDTO = new ComboDTO();
            //复制对象 ComboDTO是Combo的子类
            BeanUtils.copyProperties(combo, comboDTO);
            //调用categoryMapper，通过CategoryId获取CategoryName 再封装给comboDTO对象
            comboDTO.setCategoryName(categoryMapper.getCategoryNameById(combo.getCategoryId()));
            comboDTOList.add(comboDTO);
        }
        //combaDTOPage设置套餐列表
        combaDTOPage.setRecords(comboDTOList);
        return combaDTOPage;
    }

    /**
     * 添加分类
     * @param comboDTO
     */
    @Override
    @Transactional
    //删除缓存
    @CacheEvict(key = "#comboDTO.getCategoryId() + ':1'")
    public void addCombo(ComboDTO comboDTO) {
        //先保存套餐信息
        this.save(comboDTO);
        List<ComboDish> comboDishList = comboDTO.getSetmealDishes();
        //再保存套餐内的菜品信息
        for(ComboDish comboDish : comboDishList){
            comboDish.setSetmealId(comboDTO.getId());
            comboDishService.save(comboDish);
        }
    }

    /**
     * 获取套餐详细信息 先根据id获取套餐信息 在通过套餐ID获取套餐内的菜品列表 再根据查询套餐得到的分类ID获取分类名称 最后把参数全部封装到ComboDTO中
     * @param id
     * @return
     */
    @Override
    public ComboDTO getCombo(Long id) {
        Combo combo = this.getById(id);
        ComboDTO comboDTO = new ComboDTO();
        if(combo != null){
            BeanUtils.copyProperties(combo, comboDTO);
            LambdaQueryWrapper<ComboDish> comboDishWrapper = new LambdaQueryWrapper<>();
            comboDishWrapper.eq(ComboDish::getSetmealId, combo.getId());
            List<ComboDish> comboDishList = comboDishService.list(comboDishWrapper);
            comboDTO.setSetmealDishes(comboDishList);
            comboDTO.setCategoryName(categoryMapper.getCategoryNameById(combo.getCategoryId()));
            return comboDTO;
        }
        return null;
    }

    /**
     * 更新套餐
     * @param comboDTO
     */
    @Override
    @Transactional
    //删除缓存
    @CacheEvict(key = "#comboDTO.getCategoryId() + ':1'")
    public void updateCombo(ComboDTO comboDTO) {
        this.updateById(comboDTO);
        //步骤和上面添加套餐一样
        LambdaQueryWrapper<ComboDish> comboDishWrapper = new LambdaQueryWrapper<>();
        comboDishWrapper.eq(ComboDish::getSetmealId, comboDTO.getId());
        comboDishService.remove(comboDishWrapper);
        List<ComboDish> comboDishList = comboDTO.getSetmealDishes();
        for(ComboDish comboDish : comboDishList){
            comboDish.setSetmealId(comboDTO.getId());
            comboDishService.save(comboDish);
        }
    }

    /**
     * 更新套餐状态
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    @CacheEvict(key = "#result + ':1'")
    public Long updateComboStatus(String id, Integer status) {
        Long comboId = Long.parseLong(id);
        LambdaUpdateWrapper<Combo> comboUpdateWrapper = new LambdaUpdateWrapper<>();
        comboUpdateWrapper.set(Combo::getStatus, status).eq(Combo::getId, comboId);
        this.update(comboUpdateWrapper);
        return comboMapper.getCategoryIdById(comboId);
    }

    /**
     * 删除套餐
     * @param id
     * @return
     */
    @Override
    @Transactional
    @CacheEvict(key = "#result + ':1'")
    public Long deleteCombo(String id) {
        Long comboId = Long.parseLong(id);
        LambdaQueryWrapper<ComboDish> comboDishWrapper = new LambdaQueryWrapper<>();
        comboDishWrapper.eq(ComboDish::getSetmealId, comboId);
        Long categoryId = comboMapper.getCategoryIdById(comboId);
        //先移除符合条件的套餐菜品
        comboDishService.remove(comboDishWrapper);
        //再移除套餐
        this.removeById(id);
        //返回分类id是为了删除缓存
        return categoryId;
    }

    /**
     * 根据分类id和状态获取套餐列表并且缓存到redis
     * @param categoryId 分类id
     * @param status 套餐状态
     * @return
     */
    @Override
    @Cacheable(key = "#categoryId + ':' + #status", unless = "#result == null")
    public List<Combo> getComboListByCategoryId(Long categoryId, Integer status) {
        LambdaQueryWrapper<Combo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Combo::getCategoryId, categoryId)
                .eq(Combo::getStatus, status);
        List<Combo> comboList = this.list(lambdaQueryWrapper);
        return comboList;
    }
}
