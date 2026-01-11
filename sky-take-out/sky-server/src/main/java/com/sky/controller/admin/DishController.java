package com.sky.controller.admin;

import com.sky.constant.RedisKeyPrefix;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 菜品管理模块
 */
@RestController("adminDishController")
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    // 新增菜品和对应的口味
    @PostMapping
    @ApiOperation("新增菜品和对应的口味")
    public Result saveWithFlavor(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        // 清理该菜品对应分类的Redis缓存
        cleanRedisCache(dishDTO.getCategoryId());

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

        // 清理菜品对应分类的Redis缓存
        Set<Long> categoryIds=new HashSet<>();// 不可重复
        ids.forEach(id->categoryIds.add(dishService.getById(id).getCategoryId()));
        categoryIds.forEach(id->cleanRedisCache(id));

        return Result.success();
    }


    // 根据id查询菜品及其口味
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品及其口味")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("查询id={}的菜品及其口味", id);
        DishVO dishVO = dishService.getWithFlavorById(id);
        return Result.success(dishVO);
    }


    // 修改菜品信息
    @PutMapping
    @ApiOperation("修改菜品信息")
    public Result updateWithFlavor(@RequestBody DishDTO dishDTO) {

        // 清理该菜品当前分类和即将修改成的新分类（如有）的Redis缓存
        Long currentCategoryId = dishService.getById(dishDTO.getId()).getCategoryId();
        cleanRedisCache(currentCategoryId);

        Long newCategoryId=dishDTO.getCategoryId();
        if(!newCategoryId.equals(currentCategoryId)){
            cleanRedisCache(newCategoryId);
        }

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

        // 清理该菜品对应分类的Redis缓存
        Long categoryId = dishService.getById(id).getCategoryId();
        cleanRedisCache(categoryId);

        return Result.success();
    }


    // 根据分类id查询菜品信息
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> getByCategoryId(Long categoryId) {
        log.info("查询分类id={}的菜品",categoryId);
        List<Dish> dishes = dishService.getByCategoryId(categoryId);
        return Result.success(dishes);
    }


    /**
     * 可复用方法：清理某一分类id的Redis缓存
     * @param categoryId 分类id
     */
    private void cleanRedisCache(Long categoryId){
        String key=RedisKeyPrefix.DISH_PREFIX+categoryId;
        redisTemplate.delete(key);
    }

}
