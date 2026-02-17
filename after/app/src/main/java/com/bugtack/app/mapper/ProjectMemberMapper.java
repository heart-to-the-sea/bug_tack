package com.bugtack.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bugtack.app.entity.ProjectMember;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMemberMapper extends BaseMapper<ProjectMember> {
}
