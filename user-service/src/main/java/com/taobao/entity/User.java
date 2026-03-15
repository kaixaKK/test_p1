package com.taobao.entity;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
}
