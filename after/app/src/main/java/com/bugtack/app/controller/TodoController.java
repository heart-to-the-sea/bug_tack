package com.bugtack.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bugtack.app.entity.Todo;
import com.bugtack.app.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tack/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getTodos(
            @RequestParam Integer userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Integer projectId,
            @RequestParam(required = false) String deadlineFrom,
            @RequestParam(required = false) String deadlineTo,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<Todo> pageResult = todoService.getTodoPage(userId, status, priority, projectId, deadlineFrom, deadlineTo, sort, order, page, pageSize);
        
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
    public ResponseEntity<Map<String, Object>> createTodo(@RequestBody Map<String, Object> params) {
        Todo todo = new Todo();
        todo.setUserId((Integer) params.get("user_id"));
        todo.setIssueId((Integer) params.get("issue_id"));
        todo.setPriority((String) params.getOrDefault("priority", "medium"));
        
        String deadlineStr = (String) params.get("deadline");
        if (deadlineStr != null) {
            todo.setDeadline(LocalDateTime.parse(deadlineStr));
        }
        
        todo.setRemark((String) params.get("remark"));
        
        Todo created = todoService.createTodo(todo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTodo(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        Todo todo = new Todo();
        
        String deadlineStr = (String) params.get("deadline");
        if (deadlineStr != null) {
            todo.setDeadline(LocalDateTime.parse(deadlineStr));
        }
        
        todo.setPriority((String) params.get("priority"));
        todo.setStatus((String) params.get("status"));
        todo.setRemark((String) params.get("remark"));
        
        Todo updated = todoService.updateTodo(id, todo);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTodo(@PathVariable Integer id) {
        todoService.removeById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeTodo(@PathVariable Integer id) {
        todoService.completeTodo(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }
}
