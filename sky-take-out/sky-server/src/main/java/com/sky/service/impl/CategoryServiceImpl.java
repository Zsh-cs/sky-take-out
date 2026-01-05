package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
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
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    // 新增分类
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category=new Category();

        // 使用对象属性拷贝，将DTO数据拷贝到实体类中
        // 实体类的公共字段已经统一进行自动填充，此处不必额外赋值
        BeanUtils.copyProperties(categoryDTO,category);

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


    // 根据id查询分类信息
    @Override
    public CategoryDTO getById(Long id) {
        CategoryDTO categoryDTO=new CategoryDTO();
        Category category=categoryMapper.getById(id);

        // 使用对象属性拷贝，将实体类数据拷贝到DTO中
        BeanUtils.copyProperties(category,categoryDTO);
        return categoryDTO;
    }


    // 修改分类信息
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category=new Category();

        // 使用对象属性拷贝，将DTO数据拷贝到实体类中
        // 实体类的公共字段已经统一进行自动填充，此处不必额外赋值
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
    public CategoryDTO getValidCategoryByType(Integer type) {
        CategoryDTO validCategoryDTO=new CategoryDTO();
        Category validCategory=categoryMapper.getValidCategoryByType(type);

        // 使用对象属性拷贝，将实体类数据拷贝到DTO中
        BeanUtils.copyProperties(validCategory,validCategoryDTO);
        return validCategoryDTO;
    }
}
