package com.taobao.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product implements Serializable {
    private Long id;
    private Long merchantId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private String category;
    private String status;
    private Boolean isSeckill;
    private BigDecimal seckillPrice;
    private LocalDateTime seckillStartTime;
    private LocalDateTime seckillEndTime;
    private LocalDateTime createdAt;

    // 购物车相关字段（不映射数据库）
    private Integer cartQuantity;
    private Integer cartId;
}
