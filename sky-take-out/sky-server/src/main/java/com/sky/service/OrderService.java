package com.sky.service;

import com.sky.dto.order.OrderCancelDTO;
import com.sky.dto.order.OrderPaymentDTO;
import com.sky.dto.order.OrderRejectionDTO;
import com.sky.dto.order.OrderSubmitDTO;
import com.sky.dto.page.OrderPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.order.OrderPaymentVO;
import com.sky.vo.order.OrderStatisticsVO;
import com.sky.vo.order.OrderSubmitVO;

public interface OrderService {

    // 用户下单
    OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);

    // 订单支付
    OrderPaymentVO pay(OrderPaymentDTO orderPaymentDTO) throws Exception;

    // 支付成功，修改订单状态
    void paySuccess(String outTradeNo);

    // 接单
    void confirm(Long orderId);

    // 拒单
    void reject(OrderRejectionDTO orderRejectionDTO);

    // 取消订单
    void cancel(OrderCancelDTO orderCancelDTO);

    // 根据订单id派送订单
    void deliverById(Long orderId);

    // 根据订单id完成订单
    void completeById(Long orderId);

    // 各个状态的订单数量统计
    OrderStatisticsVO count();

    // 根据订单状态查询订单数量
    Integer countByOrderStatus(Integer orderStatus);

    // 订单分页查询
    PageResult pageQuery(OrderPageQueryDTO orderPageQueryDTO);
}
