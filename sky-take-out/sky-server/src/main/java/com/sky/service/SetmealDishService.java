package com.sky.service;

import com.sky.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService {

    // 检查是否有任何菜品被套餐关联
    void checkIfAnyDishRelatedToSetmeal(List<Long> dishIds);

    // 根据套餐id查询套餐包含的菜品
    List<SetmealDish> getBySetmealId(Long setmealId);

    // 若套餐有关联的菜品，则在套餐菜品表中清除这些关系
    void deleteDishesBySetmealId(Long setmealId);

    // 根据套餐id向套餐菜品表插入该套餐的所有关联菜品
    void saveDishesBySetmealId(Long setmealId, List<SetmealDish> setmealDishes);

}
