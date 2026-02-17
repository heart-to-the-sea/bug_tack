package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("project_members")
public class ProjectMember {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer projectId;
    private Integer userId;
    private String role;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime joinedAt;
}
