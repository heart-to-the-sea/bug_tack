package com.bugtack.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugtack.app.entity.Meeting;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeetingMapper extends BaseMapper<Meeting> {
}
