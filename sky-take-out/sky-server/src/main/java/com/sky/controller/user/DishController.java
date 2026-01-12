package com.sky.controller.user;

import com.sky.constant.RedisKeyPrefix;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户端-菜品接口
 */
@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "用户端-菜品接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    // 根据分类id查询已启用的菜品及其关联的口味
    @GetMapping("/list")
    @ApiOperation("根据分类id查询已启用的菜品及其关联的口味")
    public Result<List<DishVO>> getWithFlavorByCategoryId(Long categoryId){
        log.info("查询分类id={}的已启用的菜品及其关联的口味",categoryId);

        // 构造Redis中的key，key命名规则：dish::{categoryId}
        String key= RedisKeyPrefix.DISH_PREFIX +categoryId;

        // 查询Redis中是否缓存了该分类id下的菜品及其关联的口味
        ValueOperations ops = redisTemplate.opsForValue();
        List<DishVO> dishVOs = (List<DishVO>) ops.get(key);
        log.info("从Redis中查出dishVOs={}",dishVOs);
        if(dishVOs!=null && dishVOs.size()>0){
            // 若缓存命中，则直接返回，无需查询数据库
            log.info("Redis缓存命中，直接返回");
            return Result.success(dishVOs);
        }

        // 若缓存不命中，则继续查询数据库，并将查询到的记录缓存到Redis中
        log.info("Redis缓存不命中，继续查询数据库");
        dishVOs=dishService.getWithFlavorByCategoryId(categoryId);
        log.info("准备将dishVOs={}缓存到Redis中",dishVOs);
        ops.set(key,dishVOs);

        return Result.success(dishVOs);
    }
}
