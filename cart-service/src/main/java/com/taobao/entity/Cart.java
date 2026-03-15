package com.taobao.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Cart implements Serializable {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;

    // 关联的商品信息（非数据库字段）
    private String productName;
    private String productImage;
    private Integer productStock;
}
