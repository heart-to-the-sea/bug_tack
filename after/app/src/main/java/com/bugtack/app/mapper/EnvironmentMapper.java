package com.bugtack.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugtack.app.entity.Environment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnvironmentMapper extends BaseMapper<Environment> {
}
