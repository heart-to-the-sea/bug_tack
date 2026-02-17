package com.bugtack.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bugtack.app.entity.Project;
import com.bugtack.app.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tack/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProjects(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        IPage<Project> pageResult = projectService.getProjectPage(page, pageSize, keyword);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createProject(@RequestBody Map<String, Object> params) {
        Project project = new Project();
        project.setName((String) params.get("name"));
        project.setDescription((String) params.get("description"));
        project.setStatus("active");
        
        @SuppressWarnings("unchecked")
        List<Integer> leaderIds = (List<Integer>) params.get("leader_ids");
        @SuppressWarnings("unchecked")
        List<Integer> memberIds = (List<Integer>) params.get("member_ids");
        
        Project created = projectService.createProject(project, leaderIds, memberIds);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProject(@PathVariable Integer id) {
        Project project = projectService.getById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", project);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateProject(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        Project project = new Project();
        project.setId(id);
        project.setName((String) params.get("name"));
        project.setDescription((String) params.get("description"));
        
        @SuppressWarnings("unchecked")
        List<Integer> leaderIds = (List<Integer>) params.get("leader_ids");
        @SuppressWarnings("unchecked")
        List<Integer> memberIds = (List<Integer>) params.get("member_ids");
        
        Project updated = projectService.updateProject(project, leaderIds, memberIds);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProject(@PathVariable Integer id) {
        projectService.removeById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }
}
