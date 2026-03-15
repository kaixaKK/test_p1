package com.taobao.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Message {
    private Long id;
    private Long productId;
    private Long userId;
    private String content;
    private String reply;
    private LocalDateTime createdAt;
    private String username;
}
