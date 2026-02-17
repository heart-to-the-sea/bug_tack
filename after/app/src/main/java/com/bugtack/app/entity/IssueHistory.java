package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("issue_history")
public class IssueHistory {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer issueId;
    private String action;
    private String fromStatus;
    private String toStatus;
    private Integer userId;
    private String description;
    private String environment;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
