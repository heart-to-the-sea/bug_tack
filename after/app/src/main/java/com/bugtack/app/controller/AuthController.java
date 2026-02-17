package com.bugtack.app.controller;

import com.bugtack.app.entity.User;
import com.bugtack.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tack/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> params) {
        String username = (String) params.get("username");
        String password = (String) params.get("password");
        
        User user = userService.login(username, password);
        
        Map<String, Object> response = new HashMap<>();
        if (user != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("token", "mock-token-" + user.getId());
            data.put("user_id", user.getId());
            data.put("username", user.getUsername());
            data.put("name", user.getName());
            data.put("role", user.getRole());
            
            response.put("code", 0);
            response.put("message", "success");
            response.put("data", data);
        } else {
            response.put("code", 401);
            response.put("message", "用户名或密码错误");
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 0);
        response.put("message", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser(@RequestHeader(value = "Authorization", required = false) String token) {
        Map<String, Object> response = new HashMap<>();
        
        if (token != null && token.startsWith("mock-token-")) {
            try {
                Integer userId = Integer.parseInt(token.replace("mock-token-", ""));
                User user = userService.getById(userId);
                
                if (user != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", user.getId());
                    data.put("username", user.getUsername());
                    data.put("name", user.getName());
                    data.put("email", user.getEmail());
                    data.put("role", user.getRole());
                    
                    response.put("code", 0);
                    response.put("message", "success");
                    response.put("data", data);
                    return ResponseEntity.ok(response);
                }
            } catch (Exception e) {
                // ignore
            }
        }
        
        response.put("code", 401);
        response.put("message", "未登录");
        response.put("data", null);
        return ResponseEntity.ok(response);
    }
}
