package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import com.sky.vo.order.OrderOverViewVO;

public interface WorkspaceService {

    // 查询今日运营数据
    BusinessDataVO getBusinessData();

    // 菜品总览
    DishOverViewVO overviewDishes();

    // 套餐总览
    SetmealOverViewVO overviewSetmeals();

    // 查询订单管理数据
    OrderOverViewVO overviewOrders();
}
