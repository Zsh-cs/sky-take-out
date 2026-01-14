package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.constant.MessageConstant;
import com.sky.constant.OrderStatus;
import com.sky.constant.PayStatus;
import com.sky.context.BaseContext;
import com.sky.dto.order.OrderPaymentDTO;
import com.sky.dto.order.OrderSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.*;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.order.OrderPaymentVO;
import com.sky.vo.order.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Autowired
    private UserService userService;
    @Autowired
    private WeChatPayUtil weChatPayUtil;


    // 用户下单
    @Transactional
    @Override
    public OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO) {

        // 1.处理各种业务异常
        // 1.1 地址为空
        Long addressBookId = orderSubmitDTO.getAddressBookId();
        Address address = addressBookService.getById(addressBookId);
        if (address == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 1.2 购物车为空
        List<ShoppingCartProduct> products = shoppingCartService.listByUserId();
        if (products == null) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 2.向订单表插入一条记录
        Order order = new Order();
        BeanUtils.copyProperties(orderSubmitDTO, order);
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
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(product, orderDetail);
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


    // 订单支付
    @Override
    public OrderPaymentVO pay(OrderPaymentDTO orderPaymentDTO) throws Exception {

        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);

        // 调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                orderPaymentDTO.getOrderNumber(),// 订单号
                new BigDecimal(0.01),// 支付金额为0.01元
                "苍穹外卖订单",// 订单描述
                user.getOpenid()// 微信用户的openid
        );

        String code = jsonObject.getString("code");
        if (code != null && code.equals("ORDERPAID")) {
            throw new OrderBusinessException(MessageConstant.ORDER_PAID);
        }

        OrderPaymentVO orderPaymentVO = jsonObject.toJavaObject(OrderPaymentVO.class);
        orderPaymentVO.setPackageStr(jsonObject.getString("package"));

        return orderPaymentVO;
    }


    // 支付成功，修改订单状态
    @Override
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询当前用户的订单
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Order::getNumber, outTradeNo)
            .eq(Order::getUserId, BaseContext.getCurrentId());
        Order currentOrder = orderMapper.selectOne(lqw);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Order order=Order.builder()
                .id(currentOrder.getId())
                .status(OrderStatus.TO_BE_CONFIRMED)
                .payStatus(PayStatus.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();
        orderMapper.updateById(order);
    }
}
