package com.taobao.service;

import com.taobao.entity.Message;
import com.taobao.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    public List<Message> findByProductId(Long productId) {
        return messageMapper.findByProductId(productId);
    }

    public List<Message> findAll() {
        return messageMapper.findAll();
    }

    public List<Message> findByMerchantId(Long merchantId) {
        return messageMapper.findByMerchantId(merchantId);
    }

    public Message create(Message message) {
        messageMapper.insert(message);
        return message;
    }

    public int reply(Long messageId, String reply) {
        Message message = new Message();
        message.setId(messageId);
        message.setReply(reply);
        return messageMapper.updateReply(message);
    }

    public int deleteById(Long id) {
        return messageMapper.deleteById(id);
    }
}
