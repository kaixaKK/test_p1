package com.taobao.service;

import com.taobao.config.OrderTimeoutConfig;
import com.taobao.entity.Order;
import com.taobao.entity.OrderItem;
import com.taobao.mapper.OrderMapper;
import com.taobao.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderTimeoutConfig orderTimeoutConfig;

    /**
     * 订单支付超时时间（毫秒），默认30分钟
     */
    private static final long ORDER_TIMEOUT = 30 * 60 * 1000;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public Order findById(Long id) {
        return orderMapper.findById(id);
    }

    public Order findByOrderNo(String orderNo) {
        return orderMapper.findByOrderNo(orderNo);
    }

    public List<Order> findByUserId(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    public List<OrderItem> findOrderItems(Long orderId) {
        return orderItemMapper.findByOrderId(orderId);
    }

    @Transactional
    public Order createOrder(Order order, List<OrderItem> items) {
        // 生成订单号
        String orderNo = LocalDateTime.now().format(FORMATTER) + String.format("%04d", new Random().nextInt(10000));
        order.setOrderNo(orderNo);
        order.setStatus(0); // 待支付
        order.setCreateTime(LocalDateTime.now());

        // 计算总价
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : items) {
            totalAmount = totalAmount.add(item.getTotalPrice());
        }
        order.setTotalAmount(totalAmount);

        // 保存订单
        orderMapper.insert(order);

        // 保存订单项
        for (OrderItem item : items) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        // 添加订单超时取消任务
        long expireTime = System.currentTimeMillis() + ORDER_TIMEOUT;
        orderTimeoutConfig.addOrderTimeout(order.getId(), expireTime);

        return order;
    }

    public boolean payOrder(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(1); // 已支付
            order.setPayTime(LocalDateTime.now());
            boolean success = orderMapper.update(order) > 0;
            // 支付成功，移除超时取消任务
            if (success) {
                orderTimeoutConfig.removeOrderTimeout(orderId);
            }
            return success;
        }
        return false;
    }

    public boolean deliverOrder(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order != null && order.getStatus() == 1) {
            order.setStatus(2); // 已发货
            order.setDeliverTime(LocalDateTime.now());
            return orderMapper.update(order) > 0;
        }
        return false;
    }

    public boolean receiveOrder(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order != null && order.getStatus() == 2) {
            order.setStatus(3); // 已完成
            order.setReceiveTime(LocalDateTime.now());
            return orderMapper.update(order) > 0;
        }
        return false;
    }

    public boolean cancelOrder(Long orderId) {
        Order order = orderMapper.findById(orderId);
        if (order != null && order.getStatus() == 0) {
            order.setStatus(4); // 已取消
            boolean success = orderMapper.update(order) > 0;
            // 取消成功，移除超时取消任务
            if (success) {
                orderTimeoutConfig.removeOrderTimeout(orderId);
            }
            return success;
        }
        return false;
    }
}
