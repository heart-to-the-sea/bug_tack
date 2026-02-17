package com.bugtack.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugtack.app.entity.TestingTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestingTaskMapper extends BaseMapper<TestingTask> {
}
