package com.sky.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 类型: 1菜品分类，2套餐分类
    private Integer type;
    // 分类名称
    private String name;
    // 顺序
    private Integer sort;
    // 分类状态：0禁用，1启用
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
