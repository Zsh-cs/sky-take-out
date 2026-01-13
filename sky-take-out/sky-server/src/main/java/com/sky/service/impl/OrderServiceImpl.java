package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.order.OrderSubmitDTO;
import com.sky.entity.Address;
import com.sky.entity.ShoppingCartProduct;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.AddressBookService;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.order.OrderSubmitVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressBookService addressBookService;


    // 用户下单
    @Transactional
    @Override
    public OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO) {

        // 处理各种业务异常
        // 1.地址为空
        Long addressBookId = orderSubmitDTO.getAddressBookId();
        Address address = addressBookService.getById(addressBookId);
        if(address==null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 2.购物车为空
        Long userId = BaseContext.getCurrentId();
        List<ShoppingCartProduct> products = shoppingCartService.listByUserId();
        if(products==null){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 向订单表插入一条记录

        // 向订单明细表插入多条记录

        // 用户下单成功后，清空该用户的购物车

        // 封装OrderSubmitVO作为返回结果
    }
}
