package com.bugtack.app.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bugtack.app.entity.Requirement;
import com.bugtack.app.entity.RequirementHistory;
import com.bugtack.app.entity.Issue;
import com.bugtack.app.mapper.RequirementHistoryMapper;
import com.bugtack.app.mapper.RequirementMapper;
import com.bugtack.app.mapper.IssueMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequirementService extends ServiceImpl<RequirementMapper, Requirement> {

    private final RequirementHistoryMapper requirementHistoryMapper;
    private final IssueMapper issueMapper;

    public IPage<Requirement> getRequirementPage(Integer projectId, Integer page, Integer pageSize) {
        Page<Requirement> pageParam = new Page<>(page, pageSize);
        LambdaQueryWrapper<Requirement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Requirement::getProjectId, projectId);
        wrapper.orderByDesc(Requirement::getCreatedAt);
        return this.page(pageParam, wrapper);
    }

    public Requirement getRequirementDetail(Integer id) {
        return this.getById(id);
    }

    @Transactional
    public Requirement createRequirement(Requirement requirement, Integer creatorId) {
        requirement.setCreatorId(creatorId);
        requirement.setStatus("active");
        this.save(requirement);
        
        RequirementHistory history = new RequirementHistory();
        history.setRequirementId(requirement.getId());
        history.setAction("create");
        history.setFieldName("status");
        history.setOldValue("");
        history.setNewValue("active");
        history.setUserId(creatorId);
        history.setCreatedAt(LocalDateTime.now());
        requirementHistoryMapper.insert(history);
        
        return requirement;
    }

    @Transactional
    public Requirement updateRequirement(Requirement requirement, Integer userId) {
        Requirement old = this.getById(requirement.getId());
        
        if (!old.getTitle().equals(requirement.getTitle())) {
            RequirementHistory history = new RequirementHistory();
            history.setRequirementId(requirement.getId());
            history.setAction("update");
            history.setFieldName("title");
            history.setOldValue(old.getTitle());
            history.setNewValue(requirement.getTitle());
            history.setUserId(userId);
            history.setCreatedAt(LocalDateTime.now());
            requirementHistoryMapper.insert(history);
        }
        
        if (!old.getDescription().equals(requirement.getDescription())) {
            RequirementHistory history = new RequirementHistory();
            history.setRequirementId(requirement.getId());
            history.setAction("update");
            history.setFieldName("description");
            history.setOldValue(old.getDescription() != null ? old.getDescription() : "");
            history.setNewValue(requirement.getDescription() != null ? requirement.getDescription() : "");
            history.setUserId(userId);
            history.setCreatedAt(LocalDateTime.now());
            requirementHistoryMapper.insert(history);
        }
        
        if (!old.getPriority().equals(requirement.getPriority())) {
            RequirementHistory history = new RequirementHistory();
            history.setRequirementId(requirement.getId());
            history.setAction("update");
            history.setFieldName("priority");
            history.setOldValue(old.getPriority());
            history.setNewValue(requirement.getPriority());
            history.setUserId(userId);
            history.setCreatedAt(LocalDateTime.now());
            requirementHistoryMapper.insert(history);
        }
        
        this.updateById(requirement);
        return requirement;
    }

    public List<RequirementHistory> getTimeline(Integer requirementId) {
        LambdaQueryWrapper<RequirementHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RequirementHistory::getRequirementId, requirementId);
        wrapper.orderByAsc(RequirementHistory::getCreatedAt);
        return requirementHistoryMapper.selectList(wrapper);
    }

    public List<Issue> getRelatedBugs(Integer requirementId) {
        LambdaQueryWrapper<Issue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Issue::getRequirementId, requirementId);
        return issueMapper.selectList(wrapper);
    }

    @Transactional
    public void linkBugs(Integer requirementId, List<Integer> bugIds) {
        for (Integer bugId : bugIds) {
            Issue issue = issueMapper.selectById(bugId);
            if (issue != null) {
                issue.setRequirementId(requirementId);
                issueMapper.updateById(issue);
            }
        }
    }
}
