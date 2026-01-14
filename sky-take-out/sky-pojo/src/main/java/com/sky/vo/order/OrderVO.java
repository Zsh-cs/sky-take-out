package com.sky.vo.order;

import com.sky.entity.OrderDetail;
import com.sky.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Order implements Serializable {

    // 订单食物信息（包括菜品和套餐）
    //Caution: 根据前端要求，此变量必须叫做orderDishes，否则前端展示不出来！
    private String orderDishes;
    // 订单详情
    private List<OrderDetail> orderDetailList;

}
