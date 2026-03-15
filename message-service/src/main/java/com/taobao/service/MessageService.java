package com.taobao.service;

import com.taobao.entity.Message;
import com.taobao.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    public List<Message> findByUserId(Long userId) {
        return messageMapper.findByUserId(userId);
    }

    public List<Message> findUnreadByUserId(Long userId) {
        return messageMapper.findUnreadByUserId(userId);
    }

    public Message findById(Long id) {
        return messageMapper.findById(id);
    }

    public Message sendMessage(Message message) {
        message.setIsRead(0);
        message.setCreateTime(LocalDateTime.now());
        messageMapper.insert(message);
        return message;
    }

    public boolean markAsRead(Long id) {
        return messageMapper.markAsRead(id) > 0;
    }

    public boolean deleteById(Long id) {
        return messageMapper.deleteById(id) > 0;
    }

    public void sendSystemMessage(Long userId, String title, String content) {
        Message message = new Message();
        message.setUserId(userId);
        message.setType("SYSTEM");
        message.setTitle(title);
        message.setContent(content);
        sendMessage(message);
    }
}
