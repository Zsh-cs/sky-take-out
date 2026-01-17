package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import com.sky.vo.order.OrderOverViewVO;

import java.time.LocalDate;

public interface WorkspaceService {

    // 查询指定日期区间内的运营数据
    BusinessDataVO getBusinessDataDuringPeriod(LocalDate begin, LocalDate end);

    // 查询某日的运营数据
    BusinessDataVO getBusinessDataByDate(LocalDate date);

    // 菜品总览
    DishOverViewVO overviewDishes();

    // 套餐总览
    SetmealOverViewVO overviewSetmeals();

    // 查询订单管理数据
    OrderOverViewVO overviewOrders();

}
