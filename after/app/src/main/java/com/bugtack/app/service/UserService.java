package com.bugtack.app.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugtack.app.entity.Issue;
import com.bugtack.app.entity.Project;
import com.bugtack.app.entity.ProjectMember;
import com.bugtack.app.entity.User;
import com.bugtack.app.mapper.IssueMapper;
import com.bugtack.app.mapper.ProjectMapper;
import com.bugtack.app.mapper.ProjectMemberMapper;
import com.bugtack.app.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {

    private final ProjectMemberMapper projectMemberMapper;
    private final ProjectMapper projectMapper;
    private final IssueMapper issueMapper;

    public IPage<User> getUserPage(Integer page, Integer pageSize, String keyword, String role, Integer projectId) {
        Page<User> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(User::getUsername, keyword).or().like(User::getName, keyword).or().like(User::getEmail, keyword));
        }
        if (StringUtils.hasText(role)) {
            wrapper.eq(User::getRole, role);
        }
        wrapper.orderByDesc(User::getCreatedAt);
        return this.page(pageParam, wrapper);
    }

    public User getUserDetail(Integer id) {
        return this.getById(id);
    }

    public User createUser(User user) {
        user.setStatus("active");
        user.setRole(user.getRole() != null ? user.getRole() : "member");
        this.save(user);
        return user;
    }

    public User updateUser(Integer id, User user) {
        user.setId(id);
        this.updateById(user);
        return this.getById(id);
    }

    public List<Project> getUserProjects(Integer userId) {
        LambdaQueryWrapper<ProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectMember::getUserId, userId);
        List<ProjectMember> members = projectMemberMapper.selectList(wrapper);
        
        List<Project> projects = new ArrayList<>();
        for (ProjectMember member : members) {
            Project project = projectMapper.selectById(member.getProjectId());
            if (project != null) {
                projects.add(project);
            }
        }
        return projects;
    }

    public IPage<Issue> getUserIssues(Integer userId, String status, Integer page, Integer pageSize) {
        Page<Issue> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Issue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Issue::getAssigneeId, userId);
        if (StringUtils.hasText(status)) {
            wrapper.eq(Issue::getStatus, status);
        }
        wrapper.orderByDesc(Issue::getCreatedAt);
        return issueMapper.selectPage(pageParam, wrapper);
    }

    public int getUserProjectCount(Integer userId) {
        LambdaQueryWrapper<ProjectMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectMember::getUserId, userId);
        return projectMemberMapper.selectCount(wrapper).intValue();
    }

    public int getUserIssueCount(Integer userId) {
        LambdaQueryWrapper<Issue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Issue::getAssigneeId, userId);
        return issueMapper.selectCount(wrapper).intValue();
    }

    public User login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        wrapper.eq(User::getStatus, "active");
        User user = this.getOne(wrapper);
        
        if (user != null && user.getPassword().equals(password)) {
            user.setLastLoginAt(LocalDateTime.now());
            this.updateById(user);
            return user;
        }
        return null;
    }

    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return this.getOne(wrapper);
    }
}
