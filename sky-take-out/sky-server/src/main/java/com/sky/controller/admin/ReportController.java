package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import com.sky.vo.order.OrderReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 数据统计模块
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "数据统计接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // 营业额统计：营业额是指已完成的所有订单实收金额的总和
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> countTurnovers(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("统计{}到{}之间的营业额",begin,end);
        TurnoverReportVO turnoverReportVO = reportService.countTurnovers(begin, end);
        log.info("营业额列表：{}",turnoverReportVO.getTurnoverList());
        return Result.success(turnoverReportVO);
    }


    // 用户统计：新增用户数和总用户数
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> countUsers(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("统计{}到{}之间的新增用户数和总用户数",begin,end);
        UserReportVO userReportVO=reportService.countUsers(begin,end);
        return Result.success(userReportVO);
    }


    // 订单统计：有效订单是指已完成订单
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> countOrders(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("统计{}到{}之间的订单",begin,end);
        OrderReportVO orderReportVO=reportService.countOrders(begin,end);
        return Result.success(orderReportVO);
    }
}
