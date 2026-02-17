package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("issue_statuses")
public class IssueStatus {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String key;
    private String type;
    private String description;
    private Integer order;
    private Boolean isActive;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
