package com.taobao.entity;

import lombok.Data;

@Data
public class CartItem {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
}
