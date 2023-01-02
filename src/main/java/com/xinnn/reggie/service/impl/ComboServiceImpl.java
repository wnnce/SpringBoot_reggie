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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComboServiceImpl extends ServiceImpl<ComboMapper, Combo> implements ComboService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ComboDishService comboDishService;
    @Override
    public Page<ComboDTO> getComboPage(Integer page, Integer pageSize, String name) {
        Page<Combo> comboPage = new Page<>(page, pageSize);
        Page<ComboDTO> combaDTOPage = new Page<>();
        LambdaQueryWrapper<Combo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(name != null, Combo::getName, name);
        comboPage = this.page(comboPage, lambdaQueryWrapper);
        BeanUtils.copyProperties(comboPage, combaDTOPage, "records");
        List<Combo> comboList = comboPage.getRecords();
        List<ComboDTO> comboDTOList = new ArrayList<>();
        for (Combo combo : comboList){
            ComboDTO comboDTO = new ComboDTO();
            BeanUtils.copyProperties(combo, comboDTO);
            comboDTO.setCategoryName(categoryMapper.getCategoryNameById(combo.getCategoryId()));
            comboDTOList.add(comboDTO);
        }
        combaDTOPage.setRecords(comboDTOList);
        return combaDTOPage;
    }

    @Override
    @Transactional
    public void addCombo(ComboDTO comboDTO) {
        this.save(comboDTO);
        List<ComboDish> comboDishList = comboDTO.getSetmealDishes();
        for(ComboDish comboDish : comboDishList){
            comboDish.setSetmealId(comboDTO.getId());
            comboDishService.save(comboDish);
        }
    }

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

    @Override
    @Transactional
    public void updateCombo(ComboDTO comboDTO) {
        this.updateById(comboDTO);
        LambdaQueryWrapper<ComboDish> comboDishWrapper = new LambdaQueryWrapper<>();
        comboDishWrapper.eq(ComboDish::getSetmealId, comboDTO.getId());
        comboDishService.remove(comboDishWrapper);
        List<ComboDish> comboDishList = comboDTO.getSetmealDishes();
        for(ComboDish comboDish : comboDishList){
            comboDish.setSetmealId(comboDTO.getId());
            comboDishService.save(comboDish);
        }
    }

    @Override
    @Transactional
    public void updateComboStatus(List<String> ids, Integer status) {
        for(String id : ids){
            Long comboId = Long.parseLong(id);
            LambdaUpdateWrapper<Combo> comboUpdateWrapper = new LambdaUpdateWrapper<>();
            comboUpdateWrapper.set(Combo::getStatus, status).eq(Combo::getId, comboId);
            this.update(comboUpdateWrapper);
        }
    }

    @Override
    @Transactional
    public void deleteCombo(List<String> ids) {
        for(String id : ids){
            Long comboId = Long.parseLong(id);
            LambdaQueryWrapper<ComboDish> comboDishWrapper = new LambdaQueryWrapper<>();
            comboDishWrapper.eq(ComboDish::getSetmealId, comboId);
            comboDishService.remove(comboDishWrapper);
            this.removeById(id);
        }
    }

    @Override
    public List<Combo> getComboListByCategoryId(Long categoryId, Integer status) {
        LambdaQueryWrapper<Combo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Combo::getCategoryId, categoryId)
                .eq(Combo::getStatus, status);
        List<Combo> comboList = this.list(lambdaQueryWrapper);
        return comboList;
    }
}
