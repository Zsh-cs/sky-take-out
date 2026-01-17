package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import com.sky.vo.order.OrderOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 工作台模块
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "工作台接口")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    // 查询今日运营数据
    @GetMapping("/businessData")
    @ApiOperation("查询今日运营数据")
    public Result<BusinessDataVO> getBusinessData(){
        BusinessDataVO businessDataVO=workspaceService.getBusinessDataByDate(LocalDate.now());
        log.info("查询今日运营数据：{}",businessDataVO);
        return Result.success(businessDataVO);
    }


    // 菜品总览
    @GetMapping("/overviewDishes")
    @ApiOperation("菜品总览")
    public Result<DishOverViewVO> overviewDishes(){
        DishOverViewVO dishOverViewVO=workspaceService.overviewDishes();
        log.info("菜品总览：起售{}，停售{}",dishOverViewVO.getSold(),dishOverViewVO.getDiscontinued());
        return Result.success(dishOverViewVO);
    }


    // 套餐总览
    @GetMapping("/overviewSetmeals")
    @ApiOperation("套餐总览")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        SetmealOverViewVO setmealOverViewVO=workspaceService.overviewSetmeals();
        log.info("套餐总览：起售{}，停售{}",setmealOverViewVO.getSold(),setmealOverViewVO.getDiscontinued());
        return Result.success(setmealOverViewVO);
    }


    // 查询订单管理数据
    @GetMapping("/overviewOrders")
    @ApiOperation("查询订单管理数据")
    public Result<OrderOverViewVO> overviewOrders(){
        OrderOverViewVO orderOverViewVO=workspaceService.overviewOrders();
        log.info("查询订单管理数据：{}",orderOverViewVO);
        return Result.success(orderOverViewVO);
    }


}
