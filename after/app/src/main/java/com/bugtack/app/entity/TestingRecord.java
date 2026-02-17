package com.bugtack.app.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("testing_records")
public class TestingRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer taskId;
    private Integer issueId;
    private String environment;
    private String result;
    private String description;
    private Integer testerId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime testedAt;
}
