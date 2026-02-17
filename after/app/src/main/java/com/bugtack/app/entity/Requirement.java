package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("requirements")
public class Requirement {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer projectId;
    private String title;
    private String description;
    private String priority;
    private String status;
    private Integer assigneeId;
    private Integer creatorId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
