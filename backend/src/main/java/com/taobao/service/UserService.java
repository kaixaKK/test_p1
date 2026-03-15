package com.taobao.service;

import com.taobao.entity.User;
import com.taobao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public List<User> findByRole(String role) {
        return userMapper.findByRole(role);
    }

    public User register(User user) {
        user.setRole("USER");
        // 使用 BCrypt 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMapper.insert(user);
        return user;
    }

    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public int update(User user) {
        // 如果更新时提供了新密码，则加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userMapper.update(user);
    }

    public int deleteById(Long id) {
        return userMapper.deleteById(id);
    }
}
