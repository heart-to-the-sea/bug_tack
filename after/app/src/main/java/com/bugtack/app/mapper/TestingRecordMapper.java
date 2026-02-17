package com.bugtack.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugtack.app.entity.TestingRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestingRecordMapper extends BaseMapper<TestingRecord> {
}
