package com.sky.service;

import com.sky.dto.order.OrderPaymentDTO;
import com.sky.dto.order.OrderSubmitDTO;
import com.sky.vo.order.OrderPaymentVO;
import com.sky.vo.order.OrderSubmitVO;

public interface OrderService {

    // 用户下单
    OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);

    // 订单支付
    OrderPaymentVO pay(OrderPaymentDTO orderPaymentDTO) throws Exception;

    // 支付成功，修改订单状态
    void paySuccess(String outTradeNo);
}
