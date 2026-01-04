package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.result.PageResult;

public interface CategoryService {
    // 新增分类
    void save(CategoryDTO categoryDTO);

    // 分类分页查询
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    // 启用禁用分类
    void changeStatus(Integer status, Long id);

    // 根据id查询分类信息
    CategoryDTO getById(Long id);

    // 修改分类信息
    void update(CategoryDTO categoryDTO);

    // 根据id删除分类：逻辑删除
    void deleteById(Long id);
}
