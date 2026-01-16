package com.sky.service.impl;

import com.sky.constant.OrderStatus;
import com.sky.constant.StatusConstant;
import com.sky.service.*;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import com.sky.vo.order.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    // 查询今日运营数据
    @Override
    public BusinessDataVO getBusinessData() {

        Double turnover = orderService.countTurnoverByDate(LocalDate.now());
        Integer validOrderCount = orderService.countValidOrderByDate(LocalDate.now());
        Integer totalOrderCount = orderService.countOrderByDate(LocalDate.now());
        Integer newUsers = userService.countNewUsersByDate(LocalDate.now());

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate((double) validOrderCount / totalOrderCount)
                .unitPrice(turnover / validOrderCount)
                .newUsers(newUsers)
                .build();
    }


    // 菜品总览
    @Override
    public DishOverViewVO overviewDishes() {
        Integer sold = dishService.countByStatus(StatusConstant.ENABLE);
        Integer discontinued = dishService.countByStatus(StatusConstant.DISABLE);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }


    // 套餐总览
    @Override
    public SetmealOverViewVO overviewSetmeals() {
        Integer sold = setmealService.countByStatus(StatusConstant.ENABLE);
        Integer discontinued = setmealService.countByStatus(StatusConstant.DISABLE);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }


    // 查询订单管理数据
    @Override
    public OrderOverViewVO overviewOrders() {

        return OrderOverViewVO.builder()
                .waitingOrders(orderService.countByOrderStatus(OrderStatus.TO_BE_CONFIRMED))
                .deliveredOrders(orderService.countByOrderStatus(OrderStatus.CONFIRMED))
                .completedOrders(orderService.countByOrderStatus(OrderStatus.COMPLETED))
                .cancelledOrders(orderService.countByOrderStatus(OrderStatus.CANCELLED))
                .allOrders(orderService.countAllOrders())
                .build();
    }
}
