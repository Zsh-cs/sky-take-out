package com.sky.service;

import com.sky.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {

    // 新增订单详情
    void save(OrderDetail orderDetail);

    // 根据订单id查询订单详情
    List<OrderDetail> getByOrderId(Long orderId);

    // 根据订单id查询订单食物详细信息（订单中的食物和数量）并封装成字符串
    String getFoodsInfoByOrderId(Long orderId);
}
