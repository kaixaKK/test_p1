package com.taobao.entity;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order implements Serializable {
    private Long id;
    private String orderNo;
    private Long userId;
    private BigDecimal totalAmount;
    private Integer status;
    private String statusName;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
    private LocalDateTime deliverTime;
    private LocalDateTime receiveTime;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private String remark;
}
