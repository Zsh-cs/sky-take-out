package com.sky.service.impl;

import com.sky.constant.OrderStatus;
import com.sky.constant.StatusConstant;
import com.sky.service.*;
import com.sky.utils.DateUtil;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import com.sky.vo.order.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    // 查询指定日期区间内的运营数据
    @Override
    public BusinessDataVO getBusinessDataDuringPeriod(LocalDate begin, LocalDate end) {

        Double turnover = 0.0;
        Integer validOrderCount = 0;
        Integer totalOrderCount = 0;
        Integer newUsers = 0;

        List<LocalDate> dateList = DateUtil.getDateList(begin, end);
        for (LocalDate date : dateList) {

            turnover += orderService.countTurnoverByDate(date);
            validOrderCount += orderService.countValidOrderByDate(date);
            totalOrderCount += orderService.countOrderByDate(date);
            newUsers += userService.countNewUsersByDate(date);
        }

        return BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate((double) validOrderCount / totalOrderCount)
                .unitPrice(turnover / validOrderCount)
                .newUsers(newUsers)
                .build();
    }


    // 查询某日的运营数据
    @Override
    public BusinessDataVO getBusinessDataByDate(LocalDate date) {
        return getBusinessDataDuringPeriod(date,date);
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
