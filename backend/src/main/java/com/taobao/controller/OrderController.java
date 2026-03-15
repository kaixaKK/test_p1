package com.taobao.controller;

import com.taobao.entity.Order;
import com.taobao.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public Map<String, Object> getOrder(@PathVariable Long id) {
        Order order = orderService.findById(id);
        Map<String, Object> result = new HashMap<>();
        if (order != null) {
            result.put("success", true);
            result.put("data", order);
        } else {
            result.put("success", false);
            result.put("message", "订单不存在");
        }
        return result;
    }

    @GetMapping("/user/{userId}")
    public Map<String, Object> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.findByUserId(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", orders);
        return result;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> getAllOrders() {
        List<Order> orders = orderService.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", orders);
        return result;
    }

    @PostMapping("/create")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        @SuppressWarnings("unchecked")
        List<Integer> cartItemIds = (List<Integer>) params.get("cartItemIds");
        Order order = orderService.createOrder(userId, cartItemIds);
        Map<String, Object> result = new HashMap<>();
        if (order != null) {
            result.put("success", true);
            result.put("message", "订单创建成功");
            result.put("data", order);
        } else {
            result.put("success", false);
            result.put("message", "购物车为空");
        }
        return result;
    }

    @PostMapping("/pay/{orderId}")
    public Map<String, Object> payOrder(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", orderService.payOrder(orderId) > 0);
        return result;
    }

    @PostMapping("/ship/{orderId}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> shipOrder(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", orderService.shipOrder(orderId) > 0);
        return result;
    }

    @PostMapping("/deliver/{orderId}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> deliverOrder(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", orderService.deliverOrder(orderId) > 0);
        return result;
    }

    @PostMapping("/cancel/{orderId}")
    public Map<String, Object> cancelOrder(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", orderService.cancelOrder(orderId) > 0);
        return result;
    }
}
