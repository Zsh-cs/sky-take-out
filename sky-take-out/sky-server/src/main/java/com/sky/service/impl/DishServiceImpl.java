package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.page.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    // 新增菜品和对应的口味
    //Caution: 由于涉及多表操作，所以必须开启事务
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        // 向菜品表插入一条数据
        dishMapper.save(dish);

        // 获取菜品id
        Long id=dish.getId();

        // 向菜品口味表插入多条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null && flavors.size()>0){
            // 遍历设置口味对应的菜品id
            flavors.forEach(f->f.setDishId(id));
            // 批量添加口味
            dishFlavorMapper.saveBatch(flavors);
        }
    }


    // 菜品分页查询
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        PageResult pageResult=new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }


    /**
     * 删除菜品：
     * 1.可以删除单个菜品，也可以批量删除菜品
     * 2.起售中的菜品不能删除
     * 3.被套餐关联的菜品不能删除
     * 4.删除某个菜品后，其关联的菜品口味记录也需要删除掉
     *
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        // 1.该菜品是否正在起售中
        for (Long id : ids) {
            // 使用了MyBatis-Plus提供的方法，会自动排除已被逻辑删除的菜品
            Dish dish=dishMapper.selectById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 2.该菜品是否被套餐关联
        for (Long id : ids) {
            LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
            lqw.eq(SetmealDish::getDishId,id);
            lqw.select(SetmealDish::getSetmealId);
            List<SetmealDish> setmealIds=setmealDishMapper.selectList(lqw);
        }

        // 3.删除该菜品

        // 4.若该菜品有关联的口味，则删除这些口味

    }
}
