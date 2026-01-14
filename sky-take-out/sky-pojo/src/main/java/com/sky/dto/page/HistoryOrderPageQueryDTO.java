package com.sky.dto.page;

import lombok.Data;

import java.io.Serializable;

/**
 * 当前用户的历史订单分页查询DTO
 */
@Data
public class HistoryOrderPageQueryDTO implements Serializable {

    private int page;
    private int pageSize;
    private Integer status;

}
