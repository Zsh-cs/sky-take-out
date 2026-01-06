package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.page.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.SqlOperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    // 新增套餐
    @AutoFill(SqlOperationType.INSERT)
    void save(Setmeal setmeal);

    // 套餐分页查询
    Page<SetmealVO> pageQuery(Page<SetmealVO> page,
                              @Param("setmealPageQueryDTO") SetmealPageQueryDTO setmealPageQueryDTO);

    // 修改套餐信息
    @AutoFill(SqlOperationType.UPDATE)
    void update(Setmeal setmeal);
}
