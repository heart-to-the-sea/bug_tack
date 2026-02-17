package com.bugtack.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bugtack.app.entity.Issue;
import com.bugtack.app.entity.Project;
import com.bugtack.app.entity.User;
import com.bugtack.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tack/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer projectId) {
        IPage<User> pageResult = userService.getUserPage(page, pageSize, keyword, role, projectId);
        
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
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Integer id) {
        User user = userService.getUserDetail(id);
        List<Project> projects = userService.getUserProjects(id);
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("name", user.getName());
        data.put("email", user.getEmail());
        data.put("phone", user.getPhone());
        data.put("role", user.getRole());
        data.put("avatar", user.getAvatar());
        data.put("status", user.getStatus());
        data.put("projects", projects);
        data.put("created_at", user.getCreatedAt());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, Object> params) {
        User user = new User();
        user.setUsername((String) params.get("username"));
        user.setPassword((String) params.get("password"));
        user.setName((String) params.get("name"));
        user.setEmail((String) params.get("email"));
        user.setPhone((String) params.get("phone"));
        user.setRole((String) params.getOrDefault("role", "member"));
        
        User created = userService.createUser(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        User user = new User();
        user.setName((String) params.get("name"));
        user.setEmail((String) params.get("email"));
        user.setPhone((String) params.get("phone"));
        user.setRole((String) params.get("role"));
        user.setStatus((String) params.get("status"));
        
        User updated = userService.updateUser(id, user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Integer id) {
        userService.removeById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/issues")
    public ResponseEntity<Map<String, Object>> getUserIssues(
            @PathVariable Integer id,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Issue> pageResult = userService.getUserIssues(id, status, page, pageSize);
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", pageResult.getRecords());
        result.put("total", pageResult.getTotal());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/projects")
    public ResponseEntity<Map<String, Object>> getUserProjects(@PathVariable Integer id) {
        List<Project> projects = userService.getUserProjects(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", projects);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<Map<String, Object>> updateUserRole(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        User user = new User();
        user.setRole((String) params.get("role"));
        
        User updated = userService.updateUser(id, user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }
}
