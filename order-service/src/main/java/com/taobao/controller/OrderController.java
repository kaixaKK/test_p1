package com.taobao.controller;

import com.taobao.entity.Order;
import com.taobao.entity.OrderItem;
import com.taobao.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/no/{orderNo}")
    public Order getOrderByNo(@PathVariable String orderNo) {
        return orderService.findByOrderNo(orderNo);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return orderService.findByUserId(userId);
    }

    @GetMapping("/{id}/items")
    public List<OrderItem> getOrderItems(@PathVariable Long id) {
        return orderService.findOrderItems(id);
    }

    @PostMapping
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> params) {
        Order order = new Order();
        order.setUserId(Long.parseLong(params.get("userId").toString()));
        order.setReceiverName(params.get("receiverName").toString());
        order.setReceiverPhone(params.get("receiverPhone").toString());
        order.setReceiverAddress(params.get("receiverAddress").toString());
        order.setRemark(params.getOrDefault("remark", "").toString());

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemsData = (List<Map<String, Object>>) params.get("items");

        List<OrderItem> items = new java.util.ArrayList<>();
        for (Map<String, Object> itemData : itemsData) {
            OrderItem item = new OrderItem();
            item.setProductId(Long.parseLong(itemData.get("productId").toString()));
            item.setProductName(itemData.get("productName").toString());
            item.setProductImage(itemData.getOrDefault("productImage", "").toString());
            item.setPrice(new java.math.BigDecimal(itemData.get("price").toString()));
            item.setQuantity(Integer.parseInt(itemData.get("quantity").toString()));
            item.setTotalPrice(item.getPrice().multiply(new java.math.BigDecimal(item.getQuantity())));
            items.add(item);
        }

        Order created = orderService.createOrder(order, items);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("order", created);
        return result;
    }

    @PutMapping("/{id}/pay")
    public Map<String, Object> payOrder(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", orderService.payOrder(id));
        return result;
    }

    @PutMapping("/{id}/deliver")
    public Map<String, Object> deliverOrder(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", orderService.deliverOrder(id));
        return result;
    }

    @PutMapping("/{id}/receive")
    public Map<String, Object> receiveOrder(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", orderService.receiveOrder(id));
        return result;
    }

    @PutMapping("/{id}/cancel")
    public Map<String, Object> cancelOrder(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", orderService.cancelOrder(id));
        return result;
    }
}
