package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.enumeration.SqlOperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CategoryMapper {

    // 新增分类，自动填充公共字段
    @AutoFill(SqlOperationType.INSERT)
    @Insert("insert into category (type, name, sort, create_time, create_user, update_time, update_user)" +
            "values "+
            "(#{type}, #{name}, #{sort}, #{createTime}, #{createUser}, #{updateTime}, #{updateUser})")
    void save(Category category);

    // 分类分页查询
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    // 为了修改的通用性，我们使用动态SQL编写更加通用的update方法：根据主键动态修改分类
    // 自动填充公共字段
    @AutoFill(SqlOperationType.UPDATE)
    void update(Category category);

    // 根据id查询分类信息
    @Select("select id,name,type,sort from category where id=#{id}")
    Category getById(Long id);

    // 根据id删除分类：逻辑删除
    @Update("update category set deleted=1 where id=#{id}")
    void deleteById(Long id);

}
