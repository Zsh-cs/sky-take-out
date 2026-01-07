package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.page.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    // 新增套餐和对应的菜品
    @Transactional
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.save(setmeal);
        saveDishesBySetmealId(setmeal.getId(),setmealDTO.getSetmealDishes());
    }


    // 套餐分页查询
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        Page<SetmealVO> page=new Page<>(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> setmealVOPage=setmealMapper.pageQuery(page,setmealPageQueryDTO);
        return new PageResult(setmealVOPage.getTotal(),setmealVOPage.getRecords());
    }


    // 批量删除套餐
    @Transactional
    @Override
    public void deleteBatch(List<Long> setmealIds) {
        // 1.套餐是否正在起售中
        for (Long id : setmealIds) {
            Setmeal setmeal=setmealMapper.selectById(id);
            if(setmeal.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        // 2.删除套餐
        for (Long setmealId : setmealIds) {
            setmealMapper.deleteById(setmealId);
            // 3.若套餐有关联的菜品，则在套餐菜品表中清除这些关系
            LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
            lqw.eq(SetmealDish::getSetmealId,setmealId);
            setmealDishMapper.delete(lqw);
        }
    }


    // 根据id查询套餐
    @Override
    public SetmealVO getById(Long setmealId) {
        SetmealVO setmealVO=new SetmealVO();

        // 1.根据套餐id查询套餐信息
        Setmeal setmeal=setmealMapper.selectById(setmealId);

        // 2.根据套餐id查询菜品信息
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,setmealId);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(lqw);

        // 3.将以上信息封装到SetmealVO对象中
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }


    // 修改套餐信息
    @Transactional
    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        // 1.修改该套餐的信息
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        // 2.删除套餐菜品表中该套餐关联的所有菜品
        deleteDishesBySetmealId(setmeal.getId());
        // 3.重新在套餐菜品表中插入该套餐关联的所有菜品
        saveDishesBySetmealId(setmeal.getId(), setmealDTO.getSetmealDishes());
    }


    // 起售停售套餐
    @Override
    public void changeStatus(Integer status, Long setmealId) {
        Setmeal setmeal=new Setmeal();
        setmeal.setId(setmealId);

        // 获取该套餐关联的所有菜品id
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,setmealId);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(lqw);

        // 若套餐中包含未起售的菜品，则该套餐不能起售
        if(status==StatusConstant.ENABLE){
            for (SetmealDish setmealDish : setmealDishes) {
                Long dishId=setmealDish.getDishId();
                Dish dish=dishMapper.selectById(dishId);
                if(dish.getStatus()==StatusConstant.DISABLE){
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }

        // 否则正常起售停售
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);
    }


    /**
     * 可复用方法：根据套餐id向套餐菜品表插入该套餐的所有关联菜品
     * @param setmealId  套餐id
     * @param setmealDishes  该套餐的所有关联菜品
     */
    private void saveDishesBySetmealId(Long setmealId, List<SetmealDish> setmealDishes){
        if(setmealDishes!=null && setmealDishes.size()>0){
            // 遍历设置菜品对应的套餐id
            for (SetmealDish dish : setmealDishes) {
                dish.setSetmealId(setmealId);
                // 向套餐菜品表中插入套餐关联的所有菜品
                setmealDishMapper.insert(dish);
            }
        }
    }


    /**
     * 可复用方法，根据套餐id删除套餐菜品表中与该套餐关联的所有菜品
     * @param setmealId
     */
    private void deleteDishesBySetmealId(Long setmealId){
        LambdaQueryWrapper<SetmealDish> lqw=new LambdaQueryWrapper<>();
        lqw.eq(SetmealDish::getSetmealId,setmealId);
        setmealDishMapper.delete(lqw);
    }

}
