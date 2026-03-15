package com.taobao.controller;

import com.taobao.entity.User;
import com.taobao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/role/{role}")
    public List<User> getUsersByRole(@PathVariable String role) {
        return userService.findByRole(role);
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
        result.put("success", true);
        result.put("user", user);
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        User user = userService.login(username, password);
        Map<String, Object> result = new HashMap<>();
        if (user != null) {
            result.put("success", true);
            result.put("user", user);
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
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", userService.deleteById(id) > 0);
        return result;
    }
}
