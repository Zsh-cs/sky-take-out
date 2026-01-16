package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.page.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    // 新增菜品和对应的口味
    void saveWithFlavor(DishDTO dishDTO);

    // 菜品分页查询
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    // 批量删除菜品
    void deleteBatch(List<Long> ids);

    // 根据id查询菜品
    Dish getById(Long id);

    // 根据id查询菜品及其口味
    DishVO getWithFlavorById(Long id);

    // 修改菜品信息
    void updateWithFlavor(DishDTO dishDTO);

    // 起售停售菜品
    void changeStatus(Integer status, Long id);

    // 根据分类id查询菜品信息
    List<Dish> getByCategoryId(Long categoryId);

    // 根据分类id查询已启用的菜品及其关联的口味
    List<DishVO> getWithFlavorByCategoryId(Long categoryId);

    // 根据套餐id查询套餐包含的菜品
    List<DishItemVO> getBySetmealId(Long setmealId);

    // 根据分类id查询菜品数量
    Long getCountByCategoryId(Long categoryId);

    // 统计起售和停售的菜品数量
    Integer countByStatus(Integer status);
}
