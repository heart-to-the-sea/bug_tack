package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("meetings")
public class Meeting {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer projectId;
    private LocalDate meetingDate;
    private String participants;
    private String status;
    private String content;
    private String conclusion;
    private String meetingLink;
    private String attachment;
    private Integer relatedRequirementId;
    private Integer creatorId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
