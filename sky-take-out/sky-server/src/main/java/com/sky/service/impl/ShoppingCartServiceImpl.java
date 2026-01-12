package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartProductDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCartProduct;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    // 添加当前用户的购物车
    @Transactional
    @Override
    public void addByUserId(ShoppingCartProductDTO shoppingCartProductDTO) {

        // 判断该商品是否已经有同种类型存在于购物车中
        ShoppingCartProduct product = new ShoppingCartProduct();
        BeanUtils.copyProperties(shoppingCartProductDTO, product);
        product.setUserId(BaseContext.getCurrentId());
        List<ShoppingCartProduct> list = getListIfExists(product);

        if(list !=null && list.size()>0){
            // 若已存在，则只需要将该商品的数量+1
            ShoppingCartProduct existingProduct = list.get(0);// 购物车同类商品只会存储一个，所以取出索引0的商品即可
            existingProduct.setNumber(existingProduct.getNumber()+1);
            shoppingCartMapper.updateById(existingProduct);
        } else{
            // 若不存在，则需要将该商品添加进购物车
            // 判断该商品是菜品还是套餐
            Long dishId= shoppingCartProductDTO.getDishId();
            if(dishId!=null){
                // 该商品是菜品
                Dish dish = dishMapper.selectById(dishId);
                product.setName(dish.getName());
                product.setImage(dish.getImage());
                product.setAmount(dish.getPrice());
            }else{
                // 该商品是套餐
                Setmeal setmeal = setmealMapper.selectById(shoppingCartProductDTO.getSetmealId());
                product.setName(setmeal.getName());
                product.setImage(setmeal.getImage());
                product.setAmount(setmeal.getPrice());
            }
            product.setNumber(1);// 第一次添加进购物车
            product.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(product);
        }
    }


    // 展示当前用户的购物车
    @Override
    public List<ShoppingCartProduct> listByUserId() {
        LambdaQueryWrapper<ShoppingCartProduct> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCartProduct::getUserId,BaseContext.getCurrentId());
        List<ShoppingCartProduct> shoppingCart = shoppingCartMapper.selectList(lqw);
        return shoppingCart;
    }


    // 清空当前用户的购物车
    @Override
    public void cleanByUserId() {
        LambdaQueryWrapper<ShoppingCartProduct> lqw=new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCartProduct::getUserId,BaseContext.getCurrentId());
        shoppingCartMapper.delete(lqw);
    }


    /**
     * 可复用方法：判断该商品是否已经有同种类型存在于购物车中
     * @param shoppingCartProduct
     * @return
     */
    private List<ShoppingCartProduct> getListIfExists(ShoppingCartProduct shoppingCartProduct) {
        LambdaQueryWrapper<ShoppingCartProduct> lqw = new LambdaQueryWrapper<>();
        Long userId = shoppingCartProduct.getUserId();
        Long dishId = shoppingCartProduct.getDishId();
        Long setmealId = shoppingCartProduct.getSetmealId();
        String dishFlavor = shoppingCartProduct.getDishFlavor();

        lqw.eq(userId != null, ShoppingCartProduct::getUserId, userId)
                .eq(dishId != null, ShoppingCartProduct::getDishFlavor, dishId)
                .eq(setmealId != null, ShoppingCartProduct::getSetmealId, setmealId)
                .eq(dishFlavor != null && !dishFlavor.isEmpty(), ShoppingCartProduct::getDishFlavor, dishFlavor);

        return shoppingCartMapper.selectList(lqw);
    }

}
