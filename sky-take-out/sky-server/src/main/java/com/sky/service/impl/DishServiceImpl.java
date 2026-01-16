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
import com.sky.service.DishFlavorService;
import com.sky.service.DishService;
import com.sky.service.SetmealDishService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private SetmealDishService setmealDishService;

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
        dishFlavorService.saveFlavorsByDishId(dish.getId(), dishDTO.getFlavors());
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
    @Transactional
    @Override
    public void deleteBatch(List<Long> dishIds) {
        // 1.菜品是否正在起售中
        for (Long id : dishIds) {
            // 使用了MyBatis-Plus提供的方法，会自动排除已被逻辑删除的菜品
            Dish dish=dishMapper.selectById(id);
            if(dish.getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 2.菜品是否被套餐关联
        setmealDishService.checkIfAnyDishRelatedToSetmeal(dishIds);

        // 3.删除菜品
        for (Long dishId : dishIds) {
            dishMapper.deleteById(dishId);
            // 4.若菜品有关联的口味，则在菜品口味表中删除这些口味
            dishFlavorService.deleteFlavorsByDishId(dishId);
        }
    }


    // 根据id查询菜品
    @Override
    public Dish getById(Long id) {
        return dishMapper.selectById(id);
    }


    // 根据id查询菜品及其口味
    @Override
    public DishVO getWithFlavorById(Long dishId) {
        DishVO dishVO=new DishVO();

        // 1.根据菜品id查询菜品
        Dish dish=dishMapper.selectById(dishId);
        // 2.根据菜品id查询口味
        List<DishFlavor> dishFlavors = dishFlavorService.getByDishId(dishId);
        // 3.将以上信息封装到DishVO对象中
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }


    // 修改菜品信息
    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        // 1.修改该菜品的信息
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        // 2.删除该菜品的所有口味
        dishFlavorService.deleteFlavorsByDishId(dish.getId());
        // 3.重新插入该菜品的所有口味
        dishFlavorService.saveFlavorsByDishId(dish.getId(),dishDTO.getFlavors());
    }


    // 起售停售菜品
    @Override
    public void changeStatus(Integer status, Long id) {
        Dish dish=new Dish();
        dish.setStatus(status);
        dish.setId(id);
        dishMapper.update(dish);
    }


    // 根据分类id查询菜品信息
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Dish::getCategoryId,categoryId);
        lqw.orderByAsc(Dish::getId);
        List<Dish> dishes = dishMapper.selectList(lqw);

        return dishes;
    }


    // 根据分类id查询已启用的菜品及其关联的口味
    @Transactional
    @Override
    public List<DishVO> getWithFlavorByCategoryId(Long categoryId) {
        List<DishVO> dishVOs=new ArrayList<>();

        // 1.根据分类id查询该分类下的所有已启用的菜品
        LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Dish::getCategoryId,categoryId)
                .eq(Dish::getStatus,StatusConstant.ENABLE);
        List<Dish> dishes = dishMapper.selectList(lqw);

        // 2.遍历菜品，根据菜品id查出该菜品关联的口味
        for (Dish dish : dishes) {
            // 3.将每一个菜品及其口味封装到一个DishVO中，添加进DishVOs
            DishVO dishVO = getWithFlavorById(dish.getId());// 此处复用了getWithFlavorById()方法
            dishVOs.add(dishVO);
        }

        return dishVOs;
    }


    // 根据套餐id查询套餐包含的菜品
    @Transactional
    @Override
    public List<DishItemVO> getBySetmealId(Long setmealId) {
        List<DishItemVO> dishItemVOs=new ArrayList<>();

        List<SetmealDish> setmealDishes = setmealDishService.getBySetmealId(setmealId);

        for (SetmealDish setmealDish : setmealDishes) {
            LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
            lqw.eq(Dish::getId,setmealDish.getDishId());
            Dish dish = dishMapper.selectOne(lqw);

            // 将setmealDish和dish的部分信息封装到dishItemVO中
            DishItemVO dishItemVO =DishItemVO.builder()
                    .name(setmealDish.getName())
                    .copies(setmealDish.getCopies())
                    .image(dish.getImage())
                    .description(dish.getDescription())
                    .build();
            dishItemVOs.add(dishItemVO);
        }

        return dishItemVOs;
    }


    // 根据分类id查询菜品数量
    @Override
    public Long getCountByCategoryId(Long categoryId) {
        LambdaQueryWrapper<Dish> dishLqw=new LambdaQueryWrapper<>();
        dishLqw.eq(Dish::getCategoryId,categoryId);
        Long count = dishMapper.selectCount(dishLqw);
        return count;
    }


    // 统计起售和停售的菜品数量
    @Override
    public Integer countByStatus(Integer status) {
        LambdaQueryWrapper<Dish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(Dish::getStatus,status);
        return Math.toIntExact(dishMapper.selectCount(lqw));
    }

}
