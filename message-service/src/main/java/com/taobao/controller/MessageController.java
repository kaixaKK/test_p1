package com.taobao.controller;

import com.taobao.entity.Message;
import com.taobao.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@CrossOrigin
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/user/{userId}")
    public List<Message> getUserMessages(@PathVariable Long userId) {
        return messageService.findByUserId(userId);
    }

    @GetMapping("/user/{userId}/unread")
    public List<Message> getUnreadMessages(@PathVariable Long userId) {
        return messageService.findUnreadByUserId(userId);
    }

    @GetMapping("/{id}")
    public Message getMessage(@PathVariable Long id) {
        return messageService.findById(id);
    }

    @PostMapping
    public Map<String, Object> sendMessage(@RequestBody Message message) {
        Map<String, Object> result = new HashMap<>();
        Message saved = messageService.sendMessage(message);
        result.put("success", true);
        result.put("message", saved);
        return result;
    }

    @PutMapping("/{id}/read")
    public Map<String, Object> markAsRead(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", messageService.markAsRead(id));
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteMessage(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", messageService.deleteById(id));
        return result;
    }
}
