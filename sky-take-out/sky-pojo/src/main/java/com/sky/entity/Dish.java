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
 * 菜品
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 菜品名称
    private String name;
    // 菜品分类id
    private Long categoryId;
    // 菜品价格
    private BigDecimal price;
    // 图片
    private String image;
    // 描述信息
    private String description;
    // 0停售，1起售
    private Integer status;

    // 公共字段
    private LocalDateTime createTime;
    private Long createUser;
    private LocalDateTime updateTime;
    private Long updateUser;

    // 逻辑删除字段
    @TableLogic
    private Integer deleted;

}
