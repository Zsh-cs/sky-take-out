package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.page.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    // 新增分类
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.save(category);
    }


    // 分类分页查询
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        Page<Category> page=categoryMapper.pageQuery(categoryPageQueryDTO);
        PageResult pageResult=new PageResult(page.getTotal(),page.getResult());
        return pageResult;
    }


    // 启用禁用分类
    @Override
    public void changeStatus(Integer status, Long id) {
        Category category=new Category();
        category.setStatus(status);
        category.setId(id);
        categoryMapper.update(category);
    }


    // 根据id查询分类信息
    @Override
    public CategoryDTO getById(Long id) {
        CategoryDTO categoryDTO=new CategoryDTO();
        Category category=categoryMapper.getById(id);
        BeanUtils.copyProperties(category,categoryDTO);
        return categoryDTO;
    }


    // 修改分类信息
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        categoryMapper.update(category);
    }


    // 根据id删除分类：逻辑删除
    @Override
    public void deleteById(Long id) {
        // 查询当前分类下是否有菜品或套餐，若有就抛出业务异常
        Integer count= dishMapper.countByCategoryId(id);
        if(count>0){
            // 当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_RELATED_TO_DISH);
        }

        count= setmealMapper.countByCategoryId(id);
        if(count>0){
            // 当前分类下有套餐，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_RELATED_TO_SETMEAL);
        }

        // 若没有异常，则逻辑删除分类
        categoryMapper.deleteById(id);
    }


    // 根据类型查询分类信息：只查已启用的分类
    @Override
    public List<CategoryDTO> getValidCategoriesByType(Integer type) {
        List<CategoryDTO> validCategoryDTOs=new ArrayList<>();
        List<Category> validCategories = categoryMapper.getValidCategoriesByType(type);

        // 使用对象属性拷贝，将实体类数据拷贝到DTO中
        for (Category validCategory : validCategories) {
            CategoryDTO validCategoryDTO=new CategoryDTO();
            BeanUtils.copyProperties(validCategory,validCategoryDTO);
            validCategoryDTOs.add(validCategoryDTO);
        }

        return validCategoryDTOs;
    }

}
