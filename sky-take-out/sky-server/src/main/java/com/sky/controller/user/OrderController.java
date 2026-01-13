package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.order.OrderSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.order.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端-订单接口
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户端-订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 用户下单
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrderSubmitDTO orderSubmitDTO){
        Long userId = BaseContext.getCurrentId();
        log.info("userId={}的用户下单了，订单内容：{}",userId, orderSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(orderSubmitDTO);
        return Result.success(orderSubmitVO);
    }
}
