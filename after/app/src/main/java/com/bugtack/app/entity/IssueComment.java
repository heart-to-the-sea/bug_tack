package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("issue_comments")
public class IssueComment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer issueId;
    private Integer userId;
    private String content;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
