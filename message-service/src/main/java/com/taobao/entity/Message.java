package com.taobao.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Message implements Serializable {
    private Long id;
    private Long userId;
    private String type;
    private String title;
    private String content;
    private Integer isRead;
    private LocalDateTime createTime;
    private LocalDateTime readTime;
}
