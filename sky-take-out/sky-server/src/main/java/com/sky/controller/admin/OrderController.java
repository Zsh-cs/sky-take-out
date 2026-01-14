package com.sky.controller.admin;

import com.sky.dto.order.OrderCancelDTO;
import com.sky.dto.order.OrderConfirmDTO;
import com.sky.dto.order.OrderRejectionDTO;
import com.sky.dto.page.OrderPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.order.OrderStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理模块
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 接单
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrderConfirmDTO orderConfirmDTO){
        Long orderId = orderConfirmDTO.getId();
        log.info("接单，订单id为：{}",orderId);
        orderService.confirm(orderId);
        return Result.success();
    }


    // 拒单
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result reject(@RequestBody OrderRejectionDTO orderRejectionDTO){
        log.info("拒单：{}",orderRejectionDTO);
        orderService.reject(orderRejectionDTO);
        return Result.success();
    }


    // 取消订单
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrderCancelDTO orderCancelDTO){
        log.info("取消订单：{}",orderCancelDTO);
        orderService.cancel(orderCancelDTO);
        return Result.success();
    }


    // 根据订单id派送订单
    @PutMapping("/delivery/{id}")
    @ApiOperation("根据订单id派送订单")
    public Result deliver(@PathVariable Long id){
        log.info("即将派送id={}的订单",id);
        orderService.deliverById(id);
        return Result.success();
    }


    // 根据订单id完成订单
    @PutMapping("/complete/{id}")
    @ApiOperation("根据订单id完成订单")
    public Result complete(@PathVariable Long id){
        log.info("即将完成id={}的订单",id);
        orderService.completeById(id);
        return Result.success();
    }


    // 各个状态的订单数量统计
    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> count(){
        log.info("统计各个状态的订单数量");
        OrderStatisticsVO orderStatisticsVO = orderService.count();
        return Result.success(orderStatisticsVO);
    }


    // 订单分页查询
    @GetMapping("/conditionSearch")
    @ApiOperation("订单分页查询")
    public Result<PageResult> pageQuery(OrderPageQueryDTO orderPageQueryDTO){
        log.info("订单分页查询，参数为：{}\n",orderPageQueryDTO);
        PageResult pageResult = orderService.pageQuery(orderPageQueryDTO);
        return Result.success(pageResult);
    }

}
