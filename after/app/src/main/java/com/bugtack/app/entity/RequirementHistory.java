package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("requirement_history")
public class RequirementHistory {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer requirementId;
    private String action;
    private String fieldName;
    private String oldValue;
    private String newValue;
    private Integer userId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
