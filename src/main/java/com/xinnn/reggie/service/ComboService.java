package com.xinnn.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xinnn.reggie.dto.ComboDTO;
import com.xinnn.reggie.pojo.Combo;
import com.xinnn.reggie.pojo.ComboDish;

import java.util.List;

public interface ComboService extends IService<Combo> {
    Page<ComboDTO> getComboPage(Integer page, Integer pageSize, String name);
    void addCombo(ComboDTO comboDTO);
    ComboDTO getCombo(Long id);
    void updateCombo(ComboDTO comboDTO);
    Long updateComboStatus(String id, Integer status);
    Long deleteCombo(String id);
    List<Combo> getComboListByCategoryId(Long categoryId, Integer status);
}
