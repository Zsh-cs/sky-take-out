package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.SetmealDTO;
import com.sky.dto.page.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
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

    // 新增套餐和对应的菜品
    @Transactional
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.save(setmeal);
        saveDishes(setmeal.getId(),setmealDTO.getSetmealDishes());
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
    public void deleteBatch(List<Long> ids) {

    }


    // 根据id查询套餐
    @Override
    public SetmealVO getById(Long id) {
        return null;
    }


    // 修改套餐信息
    @Transactional
    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {

    }


    // 起售停售套餐
    @Override
    public void changeStatus(Integer status, Long id) {

    }


    /**
     * 可复用方法：向套餐菜品表插入多条记录
     *
     * @param setmealId  套餐id
     * @param setmealDishes  套餐的所有菜品
     */
    private void saveDishes(Long setmealId, List<SetmealDish> setmealDishes){
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
