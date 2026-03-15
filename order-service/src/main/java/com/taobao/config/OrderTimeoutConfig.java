package com.taobao.config;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 订单超时取消处理器
 * 使用 Redis ZSet 存储订单过期时间戳和订单ID
 * 使用 RocketMQ 延迟消息触发检查
 */
@Component
public class OrderTimeoutConfig {

    private static final String ORDER_TIMEOUT_KEY = "order:timeout:zset";
    private static final String ROCKETMQ_TOPIC = "order-timeout-topic";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private OrderTimeoutService orderTimeoutService;

    /**
     * 初始化定时任务，每分钟检查一次超时订单
     */
    @PostConstruct
    public void init() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "order-timeout-checker");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(this::checkTimeoutOrders, 1, 1, TimeUnit.MINUTES);
    }

    /**
     * 创建订单时调用，添加超时任务
     * @param orderId 订单ID
     * @param expireTime 过期时间（毫秒时间戳）
     */
    public void addOrderTimeout(Long orderId, long expireTime) {
        // 1. 存入 Redis ZSet，score 为过期时间戳
        redisTemplate.opsForZSet().add(ORDER_TIMEOUT_KEY, orderId.toString(), expireTime);

        // 2. 发送 RocketMQ 延迟消息
        // 延迟级别：1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        // 这里设置30分钟后触发
        // 注意：使用syncSend替代asyncSend，因为API版本兼容性问题
        try {
            rocketMQTemplate.syncSend(ROCKETMQ_TOPIC, orderId.toString(), 30000, 3);
            System.out.println("Order timeout message sent, orderId: " + orderId);
        } catch (Exception e) {
            System.err.println("Failed to send order timeout message, orderId: " + orderId);
            e.printStackTrace();
        }
    }

    /**
     * 取消订单时调用，移除超时任务
     */
    public void removeOrderTimeout(Long orderId) {
        redisTemplate.opsForZSet().remove(ORDER_TIMEOUT_KEY, orderId.toString());
    }

    /**
     * 检查超时订单（由定时任务调用）
     */
    private void checkTimeoutOrders() {
        long now = Instant.now().toEpochMilli();
        // 获取所有已超时的订单
        Set<Object> timeoutOrders = redisTemplate.opsForZSet().rangeByScore(ORDER_TIMEOUT_KEY, 0, now);
        if (timeoutOrders != null && !timeoutOrders.isEmpty()) {
            for (Object orderIdObj : timeoutOrders) {
                Long orderId = Long.parseLong(orderIdObj.toString());
                // 取消超时订单
                boolean success = orderTimeoutService.cancelTimeoutOrder(orderId);
                if (success) {
                    // 移除已处理的订单
                    redisTemplate.opsForZSet().remove(ORDER_TIMEOUT_KEY, orderIdObj);
                    System.out.println("Order cancelled due to timeout, orderId: " + orderId);
                }
            }
        }
    }
}
