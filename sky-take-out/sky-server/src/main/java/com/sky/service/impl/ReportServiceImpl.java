package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.service.OrderDetailService;
import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.service.UserService;
import com.sky.vo.SalesTop10ReportVO;
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
    @Autowired
    private OrderDetailService orderDetailService;

    // 营业额统计：营业额是指已完成的所有订单实收金额的总和
    @Override
    public TurnoverReportVO countTurnovers(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);
        String dateListStr = StringUtils.join(dateList, ",");

        // 创建一个Double集合，用于存放从begin到end范围内每天的营业额
        List<Double> turnoverList=new ArrayList<>();
        for (LocalDate date : dateList) {
            Double turnover = orderService.countTurnoverByDate(date);
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

        List<Integer> newUserList=new ArrayList<>();
        List<Integer> totalUserList=new ArrayList<>();
        for (LocalDate date : dateList) {
            newUserList.add(userService.countNewUsersByDate(date));
            totalUserList.add(userService.countTotalUsersByDate(date));
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }


    // 订单统计：有效订单是指已完成订单
    @Override
    public OrderReportVO countOrders(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

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
                .dateList(StringUtils.join(dateList, ","))
                .validOrderCountList(StringUtils.join(validOrderList, ","))
                .orderCountList(StringUtils.join(orderList,","))
                .validOrderCount(validOrderCount)
                .totalOrderCount(totalOrderCount)
                .orderCompletionRate((double)validOrderCount/totalOrderCount)
                .build();
    }


    /**
     * 获取销量TOP10的商品
     * 必须是已完成的订单！
     */
    @Override
    public SalesTop10ReportVO getTop10Sales(LocalDate begin, LocalDate end) {

        List<GoodsSalesDTO> top10Sales = orderDetailService.getTop10Sales(begin, end);
        List<String> nameList=new ArrayList<>();
        List<Integer> numberList=new ArrayList<>();

        for (GoodsSalesDTO sale : top10Sales) {
            nameList.add(sale.getName());
            numberList.add(sale.getNumber());
        }

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
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
