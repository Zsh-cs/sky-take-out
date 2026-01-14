package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.entity.OrderDetail;
import com.sky.mapper.OrderDetailMapper;
import com.sky.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    // 新增订单详情
    @Override
    public void save(OrderDetail orderDetail) {
        orderDetailMapper.insert(orderDetail);
    }


    // 根据订单id查询订单详情
    @Override
    public List<OrderDetail> getByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> lqw=new LambdaQueryWrapper<>();
        lqw.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.selectList(lqw);
        return orderDetails;
    }


    // 根据订单id查询订单食物详细信息（订单中的食物和数量）并封装成字符串
    @Override
    public String getFoodsInfoByOrderId(Long orderId) {

        List<OrderDetail> orderDetails = getByOrderId(orderId);

        List<String> strs=new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            String str=orderDetail.getName()+'*'+orderDetail.getNumber()+", ";
            strs.add(str);
        }

        return String.join("",strs);
    }


}
