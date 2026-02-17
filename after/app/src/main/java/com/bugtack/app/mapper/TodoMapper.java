package com.bugtack.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugtack.app.entity.Todo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper extends BaseMapper<Todo> {
}
