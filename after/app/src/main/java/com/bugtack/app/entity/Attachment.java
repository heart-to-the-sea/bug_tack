package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("attachments")
public class Attachment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String filename;
    private String originalName;
    private Long size;
    private String type;
    private String mimeType;
    private String path;
    private String url;
    private Integer uploaderId;
    private Integer issueId;
    private Integer meetingId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
