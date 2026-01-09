package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.page.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理模块
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    // 新增菜品和对应的口味
    @PostMapping
    @ApiOperation("新增菜品和对应的口味")
    public Result saveWithFlavor(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }


    // 菜品分页查询
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询，参数为：{}\n", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 批量删除菜品
     *
     * @param ids 前端使用一个字符串传入要删除的菜品id列表，加上@RequestParam注解后框架会自动转换成List<Long>
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("要删除的菜品id列表：{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }


    // 根据id查询菜品信息
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品信息")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("查询id={}的菜品信息", id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }


    // 修改菜品信息
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result updateWithFlavor(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息为：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }


    // 起售停售菜品
    @PostMapping("/status/{status}")
    @ApiOperation("起售停售菜品")
    public Result changeStatus(@PathVariable Integer status, Long id) {
        log.info("准备{}id={}的菜品", status.equals(StatusConstant.ENABLE) ? "起售" : "停售", id);
        dishService.changeStatus(status, id);
        return Result.success();
    }


    // 根据分类id查询菜品信息
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品信息")
    public Result<List<Dish>> getByCategoryId(Long categoryId) {
        List<Dish> dishes = dishService.getByCategoryId(categoryId);
        return Result.success(dishes);
    }

}
