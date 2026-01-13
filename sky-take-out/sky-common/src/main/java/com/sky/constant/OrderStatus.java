package com.sky.constant;

/**
 * 订单状态：1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
 */
public class OrderStatus {

    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer CONFIRMED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;
}
