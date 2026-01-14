package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.page.OrderPageQueryDTO;
import com.sky.entity.Order;
import com.sky.vo.order.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    // 新增订单
    void save(Order order);

    // 订单分页查询
    Page<OrderVO> pageQuery(Page<Order> page, @Param("dto") OrderPageQueryDTO dto);
}
