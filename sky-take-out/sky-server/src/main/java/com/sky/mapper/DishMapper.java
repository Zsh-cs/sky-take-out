package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.page.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.SqlOperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

// 使用了MyBatis-Plus
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    // 新增菜品
    @AutoFill(SqlOperationType.INSERT)
    void save(Dish dish);

    // 菜品分页查询
    Page<DishVO> pageQuery(Page<DishVO> page, @Param("dishPageQueryDTO") DishPageQueryDTO dishPageQueryDTO);

}
