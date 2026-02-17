package com.bugtack.app.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugtack.app.entity.Project;
import com.bugtack.app.entity.ProjectLeader;
import com.bugtack.app.entity.ProjectMember;
import com.bugtack.app.mapper.ProjectLeaderMapper;
import com.bugtack.app.mapper.ProjectMemberMapper;
import com.bugtack.app.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService extends ServiceImpl<ProjectMapper, Project> {

    private final ProjectMemberMapper projectMemberMapper;
    private final ProjectLeaderMapper projectLeaderMapper;

    public IPage<Project> getProjectPage(Integer page, Integer pageSize, String keyword) {
        Page<Project> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Project::getName, keyword);
        }
        wrapper.orderByDesc(Project::getCreatedAt);
        return this.page(pageParam, wrapper);
    }

    @Transactional
    public Project createProject(Project project, List<Integer> leaderIds, List<Integer> memberIds) {
        this.save(project);
        
        if (leaderIds != null) {
            for (Integer leaderId : leaderIds) {
                ProjectLeader leader = new ProjectLeader();
                leader.setProjectId(project.getId());
                leader.setUserId(leaderId);
                leader.setAssignedAt(LocalDateTime.now());
                projectLeaderMapper.insert(leader);
            }
        }
        
        if (memberIds != null) {
            for (Integer memberId : memberIds) {
                ProjectMember member = new ProjectMember();
                member.setProjectId(project.getId());
                member.setUserId(memberId);
                member.setRole("member");
                member.setJoinedAt(LocalDateTime.now());
                projectMemberMapper.insert(member);
            }
        }
        
        return project;
    }

    @Transactional
    public Project updateProject(Project project, List<Integer> leaderIds, List<Integer> memberIds) {
        this.updateById(project);
        
        if (leaderIds != null) {
            LambdaQueryWrapper<ProjectLeader> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProjectLeader::getProjectId, project.getId());
            projectLeaderMapper.delete(wrapper);
            
            for (Integer leaderId : leaderIds) {
                ProjectLeader leader = new ProjectLeader();
                leader.setProjectId(project.getId());
                leader.setUserId(leaderId);
                leader.setAssignedAt(LocalDateTime.now());
                projectLeaderMapper.insert(leader);
            }
        }
        
        if (memberIds != null) {
            LambdaQueryWrapper<ProjectMember> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProjectMember::getProjectId, project.getId());
            projectMemberMapper.delete(wrapper);
            
            for (Integer memberId : memberIds) {
                ProjectMember member = new ProjectMember();
                member.setProjectId(project.getId());
                member.setUserId(memberId);
                member.setRole("member");
                member.setJoinedAt(LocalDateTime.now());
                projectMemberMapper.insert(member);
            }
        }
        
        return project;
    }

    public List<Integer> getLeaderIds(Integer projectId) {
        LambdaQueryWrapper<ProjectLeader> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectLeader::getProjectId, projectId);
        List<ProjectLeader> leaders = projectLeaderMapper.selectList(wrapper);
        List<Integer> ids = new ArrayList<>();
        for (ProjectLeader leader : leaders) {
            ids.add(leader.getUserId());
        }
        return ids;
    }

    public List<Integer> getMemberIds(Integer projectId) {
        LambdaQueryWrapper<ProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectMember::getProjectId, projectId);
        List<ProjectMember> members = projectMemberMapper.selectList(wrapper);
        List<Integer> ids = new ArrayList<>();
        for (ProjectMember member : members) {
            ids.add(member.getUserId());
        }
        return ids;
    }
}
