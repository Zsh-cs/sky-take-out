package com.sky.service.impl;

import com.sky.service.OrderService;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
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

    // 营业额统计：营业额是指已完成的所有订单实收金额的总和
    @Override
    public TurnoverReportVO countTurnovers(LocalDate begin, LocalDate end) {

        // 创建一个LocalDate集合，用于存放从begin到end范围内每天的日期
        List<LocalDate> dateList=new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
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
}
