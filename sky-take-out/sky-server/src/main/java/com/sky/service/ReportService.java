package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

public interface ReportService {

    // 营业额统计：营业额是指已完成的所有订单实收金额的总和
    TurnoverReportVO countTurnovers(LocalDate begin, LocalDate end);
}
