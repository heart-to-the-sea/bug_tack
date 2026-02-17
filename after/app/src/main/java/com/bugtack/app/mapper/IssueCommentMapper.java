package com.bugtack.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugtack.app.entity.IssueComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IssueCommentMapper extends BaseMapper<IssueComment> {
}
