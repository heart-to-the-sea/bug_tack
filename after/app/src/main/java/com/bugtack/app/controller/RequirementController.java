package com.bugtack.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bugtack.app.entity.Requirement;
import com.bugtack.app.entity.RequirementHistory;
import com.bugtack.app.entity.Issue;
import com.bugtack.app.service.RequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tack")
@RequiredArgsConstructor
public class RequirementController {

    private final RequirementService requirementService;

    @GetMapping("/tack/projects/{projectId}/requirements")
    public ResponseEntity<Map<String, Object>> getRequirements(
            @PathVariable Integer projectId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Requirement> pageResult = requirementService.getRequirementPage(projectId, page, pageSize);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tack/projects/{projectId}/requirements")
    public ResponseEntity<Map<String, Object>> createRequirement(
            @PathVariable Integer projectId,
            @RequestBody Map<String, Object> params) {
        Requirement requirement = new Requirement();
        requirement.setProjectId(projectId);
        requirement.setTitle((String) params.get("title"));
        requirement.setDescription((String) params.get("description"));
        requirement.setPriority((String) params.getOrDefault("priority", "P1"));
        
        Requirement created = requirementService.createRequirement(requirement, 1);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tack/requirements/{id}")
    public ResponseEntity<Map<String, Object>> getRequirement(@PathVariable Integer id) {
        Requirement requirement = requirementService.getRequirementDetail(id);
        List<Issue> relatedBugs = requirementService.getRelatedBugs(id);
        List<RequirementHistory> timeline = requirementService.getTimeline(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", requirement.getId());
        data.put("project_id", requirement.getProjectId());
        data.put("title", requirement.getTitle());
        data.put("description", requirement.getDescription());
        data.put("priority", requirement.getPriority());
        data.put("status", requirement.getStatus());
        data.put("related_bugs", relatedBugs);
        data.put("timeline", timeline);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/tack/requirements/{id}")
    public ResponseEntity<Map<String, Object>> updateRequirement(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> params) {
        Requirement requirement = new Requirement();
        requirement.setId(id);
        requirement.setTitle((String) params.get("title"));
        requirement.setDescription((String) params.get("description"));
        requirement.setPriority((String) params.get("priority"));
        requirement.setStatus((String) params.get("status"));
        
        Requirement updated = requirementService.updateRequirement(requirement, 1);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/tack/requirements/{id}")
    public ResponseEntity<Map<String, Object>> deleteRequirement(@PathVariable Integer id) {
        requirementService.removeById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tack/requirements/{id}/timeline")
    public ResponseEntity<Map<String, Object>> getTimeline(@PathVariable Integer id) {
        List<RequirementHistory> timeline = requirementService.getTimeline(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", timeline);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/tack/requirements/{id}/bugs")
    public ResponseEntity<Map<String, Object>> linkBugs(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> params) {
        List<Integer> bugIds = (List<Integer>) params.get("bug_ids");
        requirementService.linkBugs(id, bugIds);
        
        Map<String, Object> result = new HashMap<>();
        result.put("linked_count", bugIds.size());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }
}
