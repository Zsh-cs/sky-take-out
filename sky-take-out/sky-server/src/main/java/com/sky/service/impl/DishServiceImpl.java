package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
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

    // 新增菜品和对应的口味
    @Transactional//Caution: 由于涉及多表操作，所以必须开启事务
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish=new Dish();

        // 使用对象属性拷贝，将DTO数据拷贝到实体类中
        // 实体类的公共字段已经统一进行自动填充，此处不必额外赋值
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
}
