package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.page.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.SqlOperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

// 使用MyBatis-Plus简化代码
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    // 新增分类，自动填充公共字段
    @AutoFill(SqlOperationType.INSERT)
    @Insert("insert into category (type, name, sort, create_time, create_user, update_time, update_user)" +
            "values "+
            "(#{type}, #{name}, #{sort}, #{createTime}, #{createUser}, #{updateTime}, #{updateUser})")
    void save(Category category);

    // 分类分页查询
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    // 根据主键动态修改分类
    @AutoFill(SqlOperationType.UPDATE)
    void update(Category category);

}
