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
}
