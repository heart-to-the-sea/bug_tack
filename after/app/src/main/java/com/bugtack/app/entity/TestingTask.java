package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("testing_tasks")
public class TestingTask {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer issueId;
    private String environment;
    private String status;
    private Integer queueOrder;
    private Integer submitterId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
