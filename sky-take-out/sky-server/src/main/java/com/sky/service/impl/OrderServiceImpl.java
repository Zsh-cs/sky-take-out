package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.OrderStatus;
import com.sky.constant.PayStatus;
import com.sky.context.BaseContext;
import com.sky.dto.order.OrderSubmitDTO;
import com.sky.entity.Address;
import com.sky.entity.Order;
import com.sky.entity.OrderDetail;
import com.sky.entity.ShoppingCartProduct;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.AddressBookService;
import com.sky.service.OrderDetailService;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.vo.order.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;


    // 用户下单
    @Transactional
    @Override
    public OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO) {

        // 1.处理各种业务异常
        // 1.1 地址为空
        Long addressBookId = orderSubmitDTO.getAddressBookId();
        Address address = addressBookService.getById(addressBookId);
        if(address==null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 1.2 购物车为空
        List<ShoppingCartProduct> products = shoppingCartService.listByUserId();
        if(products==null){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 2.向订单表插入一条记录
        Order order=new Order();
        BeanUtils.copyProperties(orderSubmitDTO,order);
        order.setOrderTime(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING_PAYMENT);// 设置订单状态为待付款
        order.setPayStatus(PayStatus.UN_PAID);// 设置支付状态为未支付
        order.setNumber(String.valueOf(System.currentTimeMillis()));// 设置订单号为当前时间戳
        order.setPhone(address.getPhone());
        order.setConsignee(address.getConsignee());
        order.setUserId(BaseContext.getCurrentId());
        orderMapper.save(order);

        // 3.向订单明细表插入多条记录
        for (ShoppingCartProduct product : products) {
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(product,orderDetail);
            orderDetail.setOrderId(order.getId());// 设置订单详情关联的订单id
            orderDetailService.save(orderDetail);
        }

        // 4.用户下单成功后，清空该用户的购物车
        shoppingCartService.cleanByUserId();

        // 5.封装OrderSubmitVO作为返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();
        return orderSubmitVO;
    }
}
