package com.bugtack.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bugtack.app.entity.Issue;
import com.bugtack.app.entity.IssueHistory;
import com.bugtack.app.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tack/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getIssues(
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) Integer requirementId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer assigneeId,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder) {
        IPage<Issue> pageResult = issueService.getIssuePage(projectId, requirementId, status, assigneeId, severity, keyword, page, pageSize, sortField, sortOrder);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getIssue(@PathVariable Integer id) {
        Issue issue = issueService.getIssueDetail(id);
        List<IssueHistory> history = issueService.getIssueHistory(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", issue.getId());
        data.put("project_id", issue.getProjectId());
        data.put("requirement_id", issue.getRequirementId());
        data.put("title", issue.getTitle());
        data.put("description", issue.getDescription());
        data.put("type", issue.getType());
        data.put("severity", issue.getSeverity());
        data.put("status", issue.getStatus());
        data.put("environment", issue.getEnvironment());
        data.put("version", issue.getVersion());
        data.put("assignee_id", issue.getAssigneeId());
        data.put("creator_id", issue.getCreatorId());
        data.put("created_at", issue.getCreatedAt());
        data.put("updated_at", issue.getUpdatedAt());
        data.put("history", history);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createIssue(@RequestBody Map<String, Object> params) {
        Issue issue = new Issue();
        issue.setProjectId((Integer) params.get("project_id"));
        issue.setRequirementId((Integer) params.get("requirement_id"));
        issue.setTitle((String) params.get("title"));
        issue.setDescription((String) params.get("description"));
        issue.setType((String) params.get("type"));
        issue.setSeverity((String) params.get("severity"));
        issue.setEnvironment((String) params.get("environment"));
        issue.setVersion((String) params.get("version"));
        issue.setAssigneeId((Integer) params.get("assignee_id"));
        
        Issue created = issueService.createIssue(issue, 1);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateIssue(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        Issue issue = new Issue();
        issue.setId(id);
        issue.setTitle((String) params.get("title"));
        issue.setDescription((String) params.get("description"));
        issue.setType((String) params.get("type"));
        issue.setSeverity((String) params.get("severity"));
        issue.setStatus((String) params.get("status"));
        issue.setEnvironment((String) params.get("environment"));
        issue.setVersion((String) params.get("version"));
        issue.setAssigneeId((Integer) params.get("assignee_id"));
        
        Issue updated = issueService.updateIssue(issue, 1);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteIssue(@PathVariable Integer id) {
        issueService.removeById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<Map<String, Object>> getIssueHistory(@PathVariable Integer id) {
        List<IssueHistory> history = issueService.getIssueHistory(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", history);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/assign")
    public ResponseEntity<Map<String, Object>> assignIssue(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        Integer assigneeId = (Integer) params.get("assignee_id");
        issueService.assignIssue(id, assigneeId, 1);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }
}
