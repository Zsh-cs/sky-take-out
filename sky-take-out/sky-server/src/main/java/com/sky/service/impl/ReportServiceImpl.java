package com.sky.service.impl;

import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.service.UserService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import com.sky.vo.order.OrderReportVO;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    // 营业额统计：营业额是指已完成的所有订单实收金额的总和
    @Override
    public TurnoverReportVO countTurnovers(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);
        String dateListStr = StringUtils.join(dateList, ",");

        // 创建一个BigDecimal集合，用于存放从begin到end范围内每天的营业额
        List<BigDecimal> turnoverList=new ArrayList<>();
        for (LocalDate date : dateList) {
            BigDecimal turnover = orderService.countTurnoverByDate(date);
            turnoverList.add(turnover);
        }
        String turnoverListStr = StringUtils.join(turnoverList, ",");

        return TurnoverReportVO.builder()
                .dateList(dateListStr)
                .turnoverList(turnoverListStr)
                .build();

    }


    // 用户统计：新增用户数和总用户数
    @Override
    public UserReportVO countUsers(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);
        String dateListStr = StringUtils.join(dateList, ",");

        List<Integer> newUserList=new ArrayList<>();
        List<Integer> totalUserList=new ArrayList<>();
        for (LocalDate date : dateList) {
            newUserList.add(userService.countNewUsersByDate(date));
            totalUserList.add(userService.countTotalUsersByDate(date));
        }
        String newUserListStr = StringUtils.join(newUserList, ",");
        String totalUserListStr = StringUtils.join(totalUserList, ",");

        return UserReportVO.builder()
                .dateList(dateListStr)
                .newUserList(newUserListStr)
                .totalUserList(totalUserListStr)
                .build();
    }


    // 订单统计：有效订单是指已完成订单
    @Override
    public OrderReportVO countOrders(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);
        String dateListStr = StringUtils.join(dateList, ",");

        List<Integer> validOrderList=new ArrayList<>();
        List<Integer> orderList=new ArrayList<>();
        Integer validOrderCount=0;
        Integer totalOrderCount=0;
        for (LocalDate date : dateList) {
            Integer daliyValidOrder = orderService.countValidOrderByDate(date);
            Integer daliyOrder = orderService.countOrderByDate(date);
            validOrderList.add(daliyValidOrder);
            orderList.add(daliyOrder);
            validOrderCount+=daliyValidOrder;
            totalOrderCount+=daliyOrder;
        }

        return OrderReportVO.builder()
                .dateList(dateListStr)
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .orderCountList(StringUtils.join(orderList,","))
                .validOrderCount(validOrderCount)
                .totalOrderCount(totalOrderCount)
                .orderCompletionRate((double)validOrderCount/totalOrderCount)
                .build();
    }


    /**
     * 可复用方法：根据begin和end获取一个存放了从begin到end范围内每天的日期的LocalDate集合
     * @param begin 开始日期
     * @param end 结束日期
     * @return
     */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end){
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }
}
