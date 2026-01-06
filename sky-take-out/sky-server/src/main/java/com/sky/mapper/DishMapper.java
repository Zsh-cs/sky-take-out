package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.page.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.SqlOperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

// 使用了MyBatis-Plus
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    // 根据分类id查询菜品数量
    @Select("select count(*) from dish where category_id=#{id}")
    Integer countByCategoryId(Long id);

    // 新增菜品
    @AutoFill(SqlOperationType.INSERT)
    void save(Dish dish);

    // 菜品分页查询
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

}
