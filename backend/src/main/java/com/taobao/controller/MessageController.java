package com.taobao.controller;

import com.taobao.entity.Message;
import com.taobao.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/product/{productId}")
    public Map<String, Object> getMessagesByProduct(@PathVariable Long productId) {
        List<Message> messages = messageService.findByProductId(productId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", messages);
        return result;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> getAllMessages() {
        List<Message> messages = messageService.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", messages);
        return result;
    }

    @GetMapping("/merchant/{merchantId}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> getMessagesByMerchant(@PathVariable Long merchantId) {
        List<Message> messages = messageService.findByMerchantId(merchantId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", messages);
        return result;
    }

    @PostMapping
    public Map<String, Object> createMessage(@RequestBody Message message) {
        messageService.create(message);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "留言成功");
        result.put("data", message);
        return result;
    }

    @PostMapping("/reply/{messageId}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> replyMessage(@PathVariable Long messageId, @RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", messageService.reply(messageId, params.get("reply")) > 0);
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> deleteMessage(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", messageService.deleteById(id) > 0);
        return result;
    }
}
