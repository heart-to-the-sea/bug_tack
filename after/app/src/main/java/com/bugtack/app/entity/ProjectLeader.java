package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("project_leaders")
public class ProjectLeader {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer projectId;
    private Integer userId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime assignedAt;
}
