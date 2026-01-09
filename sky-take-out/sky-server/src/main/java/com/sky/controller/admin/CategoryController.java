package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.page.CategoryPageQueryDTO;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理模块
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private JwtProperties jwtProperties;

    // 新增分类
    @PostMapping
    @ApiOperation("新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }


    // 分类分页查询
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询，参数为：{}\n", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }


    // 启用禁用分类
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result changeStatus(@PathVariable Integer status, Long id) {
        log.info("准备{}id={}的分类", status.equals(StatusConstant.ENABLE) ? "启用" : "禁用", id);
        categoryService.changeStatus(status, id);
        return Result.success();
    }


    // 根据id查询分类信息
    @GetMapping("/{id}")
    @ApiOperation("根据id查询分类信息")
    public Result<CategoryDTO> getById(@PathVariable Long id) {
        log.info("查询id={}的分类信息", id);
        CategoryDTO categoryDTO = categoryService.getById(id);
        return Result.success(categoryDTO);
    }


    // 修改分类信息
    @PutMapping
    @ApiOperation("修改分类信息")
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类信息为：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }


    // 根据id逻辑删除分类
    @DeleteMapping
    @ApiOperation("根据id逻辑删除分类")
    public Result deleteById(Long id) {
        log.info("即将逻辑删除id={}的分类", id);
        categoryService.deleteById(id);
        return Result.success();
    }


    // 根据类型查询分类信息：只查已启用的分类
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类信息：只查已启用的分类")
    public Result<List<CategoryDTO>> getValidCategoriesByType(Integer type) {
        String msg;
        switch (type){
            case 1: msg="菜品"; break;
            case 2: msg="套餐"; break;
            default: msg="未知";
        }
        log.info("查询已启用的{}分类信息", msg);
        List<CategoryDTO> validCategoryDTOs = categoryService.getValidCategoriesByType(type);
        return Result.success(validCategoryDTOs);
    }
}
