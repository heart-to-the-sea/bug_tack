package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("environments")
public class Environment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String key;
    private String description;
    private Integer order;
    private Boolean isActive;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
