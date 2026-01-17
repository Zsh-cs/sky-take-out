package com.sky.service;

import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import com.sky.vo.order.OrderReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    // 营业额统计：营业额是指已完成的所有订单实收金额的总和
    TurnoverReportVO countTurnovers(LocalDate begin, LocalDate end);

    // 用户统计：新增用户数和总用户数
    UserReportVO countUsers(LocalDate begin, LocalDate end);

    // 订单统计：有效订单是指已完成订单
    OrderReportVO countOrders(LocalDate begin, LocalDate end);

    // 获取销量TOP10的商品
    SalesTop10ReportVO getTop10Sales(LocalDate begin, LocalDate end);

    // 运营数据导出为Excel报表
    void export(HttpServletResponse response);

}
