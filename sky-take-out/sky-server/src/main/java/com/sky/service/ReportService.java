package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import com.sky.vo.order.OrderReportVO;

import java.time.LocalDate;

public interface ReportService {

    // 营业额统计：营业额是指已完成的所有订单实收金额的总和
    TurnoverReportVO countTurnovers(LocalDate begin, LocalDate end);

    // 用户统计：新增用户数和总用户数
    UserReportVO countUsers(LocalDate begin, LocalDate end);

    // 订单统计：有效订单是指已完成订单
    OrderReportVO countOrders(LocalDate begin, LocalDate end);
}
