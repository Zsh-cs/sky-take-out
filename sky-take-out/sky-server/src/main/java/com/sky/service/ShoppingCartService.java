package com.sky.service;

import com.sky.dto.ShoppingCartProductDTO;
import com.sky.entity.ShoppingCartProduct;

import java.util.List;

public interface ShoppingCartService {

    // 添加当前用户的购物车
    void addByUserId(ShoppingCartProductDTO shoppingCartProductDTO);

    // 展示当前用户的购物车
    List<ShoppingCartProduct> listByUserId();

    // 清空当前用户的购物车
    void cleanByUserId();

    // 删除当前用户购物车中的一个商品
    void removeOneByUserId(ShoppingCartProductDTO shoppingCartProductDTO);
}
