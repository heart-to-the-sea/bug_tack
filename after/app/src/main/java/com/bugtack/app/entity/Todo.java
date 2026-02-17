package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("todos")
public class Todo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer issueId;
    private String priority;
    private LocalDateTime deadline;
    private String status;
    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
}
