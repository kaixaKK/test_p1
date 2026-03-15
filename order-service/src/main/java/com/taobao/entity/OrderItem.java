package com.taobao.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderItem implements Serializable {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
}
