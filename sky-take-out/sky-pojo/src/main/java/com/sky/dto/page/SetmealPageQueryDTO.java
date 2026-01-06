package com.sky.dto.page;

import lombok.Data;

import java.io.Serializable;

@Data
public class SetmealPageQueryDTO implements Serializable {

    private int page;
    private int pageSize;
    private String name;
    // 分类id
    private Integer categoryId;
    // 状态：0禁用，1启用
    private Integer status;

}
