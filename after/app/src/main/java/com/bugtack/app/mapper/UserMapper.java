package com.bugtack.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugtack.app.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
