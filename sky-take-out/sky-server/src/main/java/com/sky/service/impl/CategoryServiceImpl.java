package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    // 新增分类
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category=new Category();

        // 使用对象属性拷贝，将DTO数据拷贝到实体类中
        BeanUtils.copyProperties(categoryDTO,category);

        // 设置实体类的其他属性
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        // 设置该记录的创建人id和修改人id为当前管理端的登录用户id
        Long id= BaseContext.getCurrentId();
        category.setCreateUser(id);
        category.setUpdateUser(id);

        // 调用持久层方法新增分类
        categoryMapper.save(category);
    }


    // 分类分页查询
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        // 开始分页查询，PageHelper会对我们的SQL语句动态拼接分页查询条件，它的底层是基于ThreadLocal实现的
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());

        // 返回值遵循PageHelper规范
        Page<Category> page=categoryMapper.pageQuery(categoryPageQueryDTO);

        // 提取出page的total和records属性，封装到PageResult中
        PageResult pageResult=new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }


    // 启用禁用分类
    @Override
    public void changeStatus(Integer status, Long id) {
        // 为了修改的通用性，我们不在持久层中单独编写changeStatus方法，而是使用动态SQL编写更加通用的update方法
        Category category=new Category();
        category.setStatus(status);
        category.setId(id);
        categoryMapper.update(category);
    }


}
