package com.bugtack.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugtack.app.entity.IssueHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IssueHistoryMapper extends BaseMapper<IssueHistory> {
}
