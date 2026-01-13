package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.page.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    // 新增套餐和对应的菜品
    void saveWithDish(SetmealDTO setmealDTO);

    // 套餐分页查询
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    // 批量删除套餐
    void deleteBatch(List<Long> ids);

    // 根据id查询套餐及其菜品
    SetmealVO getWithDishesById(Long id);

    // 修改套餐及其菜品
    void updateWithDishes(SetmealDTO setmealDTO);

    // 起售停售套餐
    void changeStatus(Integer status, Long id);

    // 根据分类id查询已启用的套餐
    List<Setmeal> getByCategoryId(Long categoryId);

    // 根据分类id查询套餐数量
    Long getCountByCategoryId(Long categoryId);

    // 根据id查询套餐
    Setmeal getById(Long setmealId);
}