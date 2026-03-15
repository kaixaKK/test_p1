package com.taobao.controller;

import com.taobao.config.JwtUtil;
import com.taobao.entity.User;
import com.taobao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/{id}")
    public Map<String, Object> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            user.setPassword(null); // 隐藏密码
            result.put("success", true);
            result.put("data", user);
        } else {
            result.put("success", false);
            result.put("message", "用户不存在");
        }
        return result;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MERCHANT')")
    public Map<String, Object> getAllUsers() {
        List<User> users = userService.findAll();
        // 隐藏密码
        users.forEach(user -> user.setPassword(null));
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", users);
        return result;
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.findByRole(role);
        users.forEach(user -> user.setPassword(null));
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", users);
        return result;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        User existing = userService.findByUsername(user.getUsername());
        Map<String, Object> result = new HashMap<>();
        if (existing != null) {
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }
        userService.register(user);
        // 注册成功后返回 token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        user.setPassword(null);
        result.put("success", true);
        result.put("message", "注册成功");
        result.put("user", user);
        result.put("token", token);
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        User user = userService.login(username, password);
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            // 生成 Token
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
            user.setPassword(null);
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("user", user);
            result.put("token", token);
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", userService.update(user) > 0);
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", userService.deleteById(id) > 0);
        return result;
    }
}
