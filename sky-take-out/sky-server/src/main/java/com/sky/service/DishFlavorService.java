package com.sky.service;

import com.sky.entity.DishFlavor;

import java.util.List;

public interface DishFlavorService {

    // 根据菜品id向菜品口味表插入该菜品的所有口味
    void saveFlavorsByDishId(Long dishId, List<DishFlavor> dishFlavors);

    // 根据菜品id删除菜品口味表中与该菜品关联的所有口味
    void deleteFlavorsByDishId(Long dishId);

    // 根据菜品id查询口味
    List<DishFlavor> getByDishId(Long dishId);
}
