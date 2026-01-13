package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorServiceImpl implements DishFlavorService {

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 根据菜品id向菜品口味表插入该菜品的所有口味
     * @param dishId  菜品id
     * @param dishFlavors  该菜品的所有口味
     */
    @Override
    public void saveFlavorsByDishId(Long dishId, List<DishFlavor> dishFlavors) {
        if(dishFlavors!=null && dishFlavors.size()>0){
            // 遍历设置口味对应的菜品id并添加口味
            for (DishFlavor flavor : dishFlavors) {
                flavor.setDishId(dishId);
                dishFlavorMapper.insert(flavor);
            }
        }
    }


    /**
     * 根据菜品id删除菜品口味表中与该菜品关联的所有口味
     * @param dishId
     */
    @Override
    public void deleteFlavorsByDishId(Long dishId) {
        LambdaQueryWrapper<DishFlavor> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishId);
        dishFlavorMapper.delete(lqw);
    }


    // 根据菜品id查询口味
    @Override
    public List<DishFlavor> getByDishId(Long dishId) {
        LambdaQueryWrapper<DishFlavor> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishId);
        List<DishFlavor> dishFlavors=dishFlavorMapper.selectList(lqw);
        return dishFlavors;
    }
}
