package com.bugtack.app.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugtack.app.entity.Issue;
import com.bugtack.app.entity.IssueHistory;
import com.bugtack.app.entity.Project;
import com.bugtack.app.mapper.IssueHistoryMapper;
import com.bugtack.app.mapper.IssueMapper;
import com.bugtack.app.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IssueService extends ServiceImpl<IssueMapper, Issue> {

    private final IssueHistoryMapper issueHistoryMapper;
    private final ProjectMapper projectMapper;

    public IPage<Issue> getIssuePage(Integer projectId, Integer requirementId, String status, 
                                       Integer assigneeId, String severity, String keyword,
                                       Integer page, Integer pageSize, String sortField, String sortOrder) {
        Page<Issue> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Issue> wrapper = new LambdaQueryWrapper<>();
        
        if (projectId != null) {
            wrapper.eq(Issue::getProjectId, projectId);
        }
        if (requirementId != null) {
            wrapper.eq(Issue::getRequirementId, requirementId);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Issue::getStatus, status);
        }
        if (assigneeId != null) {
            wrapper.eq(Issue::getAssigneeId, assigneeId);
        }
        if (StringUtils.hasText(severity)) {
            wrapper.eq(Issue::getSeverity, severity);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Issue::getTitle, keyword);
        }
        
        if ("asc".equalsIgnoreCase(sortOrder)) {
            wrapper.orderByAsc(Issue::getCreatedAt);
        } else {
            wrapper.orderByDesc(Issue::getCreatedAt);
        }
        
        return this.page(pageParam, wrapper);
    }

    public Issue getIssueDetail(Integer id) {
        return this.getById(id);
    }

    @Transactional
    public Issue createIssue(Issue issue, Integer creatorId) {
        issue.setCreatorId(creatorId);
        issue.setStatus("pending");
        this.save(issue);
        
        IssueHistory history = new IssueHistory();
        history.setIssueId(issue.getId());
        history.setAction("create");
        history.setToStatus("pending");
        history.setUserId(creatorId);
        history.setCreatedAt(LocalDateTime.now());
        issueHistoryMapper.insert(history);
        
        return issue;
    }

    @Transactional
    public Issue updateIssue(Issue issue, Integer userId) {
        Issue old = this.getById(issue.getId());
        
        if (!old.getStatus().equals(issue.getStatus())) {
            IssueHistory history = new IssueHistory();
            history.setIssueId(issue.getId());
            history.setAction("status_change");
            history.setFromStatus(old.getStatus());
            history.setToStatus(issue.getStatus());
            history.setUserId(userId);
            history.setCreatedAt(LocalDateTime.now());
            issueHistoryMapper.insert(history);
        }
        
        this.updateById(issue);
        return issue;
    }

    public List<IssueHistory> getIssueHistory(Integer issueId) {
        LambdaQueryWrapper<IssueHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IssueHistory::getIssueId, issueId);
        wrapper.orderByAsc(IssueHistory::getCreatedAt);
        return issueHistoryMapper.selectList(wrapper);
    }

    @Transactional
    public void assignIssue(Integer issueId, Integer assigneeId, Integer userId) {
        Issue issue = this.getById(issueId);
        String oldAssignee = issue.getAssigneeId() != null ? issue.getAssigneeId().toString() : "无";
        
        issue.setAssigneeId(assigneeId);
        this.updateById(issue);
        
        IssueHistory history = new IssueHistory();
        history.setIssueId(issueId);
        history.setAction("assign");
        history.setDescription("分配给用户: " + assigneeId);
        history.setUserId(userId);
        history.setCreatedAt(LocalDateTime.now());
        issueHistoryMapper.insert(history);
    }
}
