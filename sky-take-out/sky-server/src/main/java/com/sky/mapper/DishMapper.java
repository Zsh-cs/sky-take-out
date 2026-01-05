package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.SqlOperationType;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    // 根据分类id查询菜品数量
    @Select("select count(*) from dish where category_id=#{id}")
    Integer countByCategoryId(Long id);

    // 新增菜品
    @AutoFill(SqlOperationType.INSERT)
    void save(Dish dish);

    // 菜品分页查询
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
