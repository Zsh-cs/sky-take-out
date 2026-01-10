package com.sky.controller.user;

import com.sky.dto.CategoryDTO;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户端-分类接口
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
@Api(tags = "用户端-分类接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // [根据type]查询已启用的分类
    @GetMapping("/list")
    @ApiOperation("[根据type]查询已启用的分类")
    public Result<List<CategoryDTO>> get(Integer type) {
        log.info("查询type={}的分类",type);
        List<CategoryDTO> validCategories = categoryService.getValidCategoriesByType(type);
        return Result.success(validCategories);
    }
}
