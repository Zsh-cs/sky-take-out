package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

        // 获取菜品id，向菜品口味表插入多条数据
        saveFlavors(dish.getId(), dishDTO.getFlavors());
    }


    // 菜品分页查询
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        // 使用MP进行分页查询，弃用PageHelper，MP会自动过滤已被逻辑删除的记录
        Page<DishVO> page=new Page<>(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> dishVOPage = dishMapper.pageQuery(page, dishPageQueryDTO);
        return new PageResult(dishVOPage.getTotal(),dishVOPage.getRecords());
    }


    /**
     * 删除菜品：
     * 1.可以删除单个菜品，也可以批量删除菜品
     * 2.起售中的菜品不能删除
     * 3.被套餐关联的菜品不能删除
     * 4.删除某个菜品后，其关联的菜品口味记录也需要删除掉
     *
     * @param dishIds
     */
    //Caution: 由于涉及多表操作，所以必须开启事务
    @Transactional
    @Override
    public void deleteBatch(List<Long> dishIds) {
        // 1.菜品是否正在起售中
        for (Long id : dishIds) {
            // 使用了MyBatis-Plus提供的方法，会自动排除已被逻辑删除的菜品
            Dish dish=dishMapper.selectById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 2.菜品是否被套餐关联
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.in(SetmealDish::getDishId,dishIds);
        lqw.select(SetmealDish::getSetmealId);
        Long count = setmealDishMapper.selectCount(lqw);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_RELATED_TO_SETMEAL);
        }

        // 3.删除菜品
        for (Long dishId : dishIds) {
            dishMapper.deleteById(dishId);
            // 4.若菜品有关联的口味，则删除这些口味
            LambdaQueryWrapper<DishFlavor> flavorLqw=new LambdaQueryWrapper<>();
            flavorLqw.eq(DishFlavor::getDishId,dishId);
            dishFlavorMapper.delete(flavorLqw);
        }
    }


    // 根据id查询菜品信息
    @Override
    public DishVO getById(Long dishId) {
        DishVO dishVO=new DishVO();

        // 1.根据菜品id查询菜品信息
        Dish dish=dishMapper.selectById(dishId);

        // 2.根据菜品id查询口味信息
        LambdaQueryWrapper<DishFlavor> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishId);
        List<DishFlavor> dishFlavors=dishFlavorMapper.selectList(lqw);

        // 3.将以上信息封装到DishVO对象中
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }


    // 修改菜品信息
    //Caution: 由于涉及多表操作，所以必须开启事务
    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {

        // 1.修改该菜品的信息
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        // 2.删除该菜品的所有口味
        Long dishId=dishDTO.getId();
        LambdaQueryWrapper<DishFlavor> lqw=new LambdaQueryWrapper<>();
        lqw.eq(DishFlavor::getDishId,dishId);
        dishFlavorMapper.delete(lqw);

        // 3.重新插入该菜品的所有口味
        saveFlavors(dishId,dishDTO.getFlavors());
    }


    // 起售停售菜品
    @Override
    public void changeStatus(Integer status, Long id) {
        Dish dish=new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.update(dish);
    }


    /**
     * 可复用方法：向菜品口味表插入多条数据
     *
     * @param dishId  菜品id
     * @param dishFlavors  菜品的所有口味
     */
    private void saveFlavors(Long dishId, List<DishFlavor> dishFlavors){

        if(dishFlavors!=null && dishFlavors.size()>0){
            // 遍历设置口味对应的菜品id并添加口味
            for (DishFlavor flavor : dishFlavors) {
                flavor.setDishId(dishId);
                dishFlavorMapper.insert(flavor);
            }
        }
    }
}
