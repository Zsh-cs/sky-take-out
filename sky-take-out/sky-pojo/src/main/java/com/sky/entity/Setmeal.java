package com.sky.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 分类id
    private Long categoryId;
    // 套餐名称
    private String name;
    // 套餐价格
    private BigDecimal price;
    // 状态 0:停用 1:启用
    private Integer status;
    // 描述信息
    private String description;
    // 图片
    private String image;

    // 公共字段
    private LocalDateTime createTime;
    private Long createUser;
    private LocalDateTime updateTime;
    private Long updateUser;

    // 逻辑删除字段
    @TableLogic
    private Integer deleted;
}
