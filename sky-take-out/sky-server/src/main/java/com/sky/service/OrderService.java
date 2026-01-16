package com.sky.service;

import com.sky.dto.order.OrderCancelDTO;
import com.sky.dto.order.OrderPaymentDTO;
import com.sky.dto.order.OrderRejectionDTO;
import com.sky.dto.order.OrderSubmitDTO;
import com.sky.dto.page.HistoryOrderPageQueryDTO;
import com.sky.dto.page.OrderPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.order.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    // 商家取消订单
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

    // 根据订单id查询订单详情
    OrderVO getDetails(Long id);

    // 当前用户的历史订单分页查询
    PageResult pageQueryForHistoryOrders(HistoryOrderPageQueryDTO dto);

    // 查询历史订单详情
    HistoryOrderVO getDetailsForHistoryOrder(Long id);

    // 用户取消订单
    void cancel(Long id);

    // 再来一单：将原订单中的商品重新加入到购物车中
    void oneMore(Long id);

    // 自动取消超时未付款的订单
    void cancelTimeoutOrders();

    // 自动完成一直在派送中的订单
    void completeDeliveryOrders();

    // 用户催单
    void chase(Long id);

    // 根据日期统计当天营业额
    BigDecimal countTurnoverByDate(LocalDate date);
}
