package com.sky.service;

import com.sky.dto.order.OrderSubmitDTO;
import com.sky.vo.order.OrderSubmitVO;

public interface OrderService {

    // 用户下单
    OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);
}
