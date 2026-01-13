package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.constant.MessageConstant;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealDishServiceImpl implements SetmealDishService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    // 检查是否有任何菜品被套餐关联
    @Override
    public void checkIfAnyDishRelatedToSetmeal(List<Long> dishIds) {
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.in(SetmealDish::getDishId,dishIds);
        lqw.select(SetmealDish::getSetmealId);
        Long count = setmealDishMapper.selectCount(lqw);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_RELATED_TO_SETMEAL);
        }
    }


    // 根据套餐id查询套餐包含的菜品
    @Override
    public List<SetmealDish> getBySetmealId(Long setmealId) {
        LambdaQueryWrapper<SetmealDish> setmealDishLqw=new LambdaQueryWrapper<>();
        setmealDishLqw.eq(SetmealDish::getSetmealId,setmealId)
                .orderByDesc(SetmealDish::getPrice);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(setmealDishLqw);
        return setmealDishes;
    }


    // 若套餐有关联的菜品，则在套餐菜品表中清除这些关系
    @Override
    public void deleteDishesBySetmealId(Long setmealId) {
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,setmealId);
        setmealDishMapper.delete(lqw);
    }


    // 根据套餐id向套餐菜品表插入该套餐的所有关联菜品
    @Transactional
    @Override
    public void saveDishesBySetmealId(Long setmealId, List<SetmealDish> setmealDishes) {
        if(setmealDishes!=null && setmealDishes.size()>0){
            // 遍历设置菜品对应的套餐id
            for (SetmealDish dish : setmealDishes) {
                dish.setSetmealId(setmealId);
                // 向套餐菜品表中插入套餐关联的所有菜品
                setmealDishMapper.insert(dish);
            }
        }
    }
}
