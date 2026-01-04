package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    // 新增分类
    @Insert("insert into category (type, name, sort, create_time, update_time, create_user, update_user)" +
            "values "+
            "(#{type}, #{name}, #{sort}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void save(Category category);

    // 分类分页查询
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    // 为了修改的通用性，我们使用动态SQL编写更加通用的update方法：根据主键动态修改分类
    void update(Category category);
}
