package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("issues")
public class Issue {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer projectId;
    private Integer requirementId;
    private String title;
    private String description;
    private String type;
    private String severity;
    private String status;
    private String environment;
    private String version;
    private Integer assigneeId;
    private Integer creatorId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
}
