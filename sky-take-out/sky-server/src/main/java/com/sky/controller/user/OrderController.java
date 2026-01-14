package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.order.OrderPaymentDTO;
import com.sky.dto.order.OrderSubmitDTO;
import com.sky.dto.page.HistoryOrderPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.order.HistoryOrderVO;
import com.sky.vo.order.OrderPaymentVO;
import com.sky.vo.order.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    // 订单支付
    //Caution: 由于我不是商户，无法开通微信支付，所以此处就直接支付成功
    //   在微信小程序中，点击确认支付后虽然没有任何反应，但其实已经支付成功
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> pay(@RequestBody OrderPaymentDTO orderPaymentDTO) {
        log.info("订单支付：{}",orderPaymentDTO);
        orderService.paySuccess(orderPaymentDTO.getOrderNumber());
        return Result.success(null);
    }


    // 当前用户的历史订单分页查询
    @GetMapping("/historyOrders")
    @ApiOperation("当前用户的历史订单分页查询")
    public Result<PageResult> pageQueryForHistoryOrders(HistoryOrderPageQueryDTO dto){
        log.info("当前用户进行历史订单分页查询，参数：{}",dto);
        PageResult pageResult=orderService.pageQueryForHistoryOrders(dto);
        return Result.success(pageResult);
    }


    // 查询历史订单详情
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询历史订单详情")
    public Result<HistoryOrderVO> getDetailsForHistoryOrder(@PathVariable Long id){
        log.info("查询id={}的历史订单详情",id);
        HistoryOrderVO historyOrderVO=orderService.getDetailsForHistoryOrder(id);
        return Result.success(historyOrderVO);
    }


    // 用户取消订单
    @PutMapping("/cancel/{id}")
    @ApiOperation("用户取消订单")
    public Result cancel(@PathVariable Long id){
        log.info("用户取消了id={}的订单",id);
        orderService.cancel(id);
        return Result.success();
    }


    // 再来一单：将原订单中的商品重新加入到购物车中
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result oneMore(@PathVariable Long id){
        log.info("用户想要再来一单，与id={}的订单商品保持一致",id);
        orderService.oneMore(id);
        return Result.success();
    }


}












