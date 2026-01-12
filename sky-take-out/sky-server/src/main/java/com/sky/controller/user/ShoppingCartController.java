package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartProductDTO;
import com.sky.entity.ShoppingCartProduct;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端-购物车接口
 * Caution: 购物车是和用户强绑定的，每个用户有自己的购物车，因此购物车接口都必须依赖于userId
 * Long userId = BaseContext.getCurrentId();
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "用户端-购物车接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    // 添加当前用户的购物车
    @PostMapping("/add")
    @ApiOperation("添加当前用户的购物车")
    public Result addByUserId(@RequestBody ShoppingCartProductDTO shoppingCartProductDTO) {
        log.info("userId={}的微信用户将一件商品添加进了ta的购物车，该商品为：{}", BaseContext.getCurrentId(), shoppingCartProductDTO);
        shoppingCartService.addByUserId(shoppingCartProductDTO);
        return Result.success();
    }


    // 展示当前用户的购物车
    @GetMapping("/list")
    @ApiOperation("展示当前用户的购物车")
    public Result<List<ShoppingCartProduct>> listByUserId(){
        log.info("准备展示userId={}的用户的购物车...",BaseContext.getCurrentId());
        List<ShoppingCartProduct> shoppingCart=shoppingCartService.listByUserId();
        return Result.success(shoppingCart);
    }


    // 清空当前用户的购物车
    @DeleteMapping("/clean")
    @ApiOperation("清空当前用户的购物车")
    public Result cleanByUserId(){
        log.info("准备清空userId={}的用户的购物车...",BaseContext.getCurrentId());
        shoppingCartService.cleanByUserId();
        return Result.success();
    }

}



