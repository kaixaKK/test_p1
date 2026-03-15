package com.taobao.service;

import com.taobao.entity.*;
import com.taobao.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CartMapper cartMapper;

    public Order findById(Long id) {
        Order order = orderMapper.findById(id);
        if (order != null) {
            order.setItems(orderItemMapper.findByOrderId(id));
        }
        return order;
    }

    public List<Order> findByUserId(Long userId) {
        List<Order> orders = orderMapper.findByUserId(userId);
        for (Order order : orders) {
            order.setItems(orderItemMapper.findByOrderId(order.getId()));
        }
        return orders;
    }

    public List<Order> findAll() {
        List<Order> orders = orderMapper.findAll();
        for (Order order : orders) {
            order.setItems(orderItemMapper.findByOrderId(order.getId()));
        }
        return orders;
    }

    @Transactional
    public Order createOrder(Long userId, List<Integer> cartItemIds) {
        try {
            // 直接使用购物车中的所有商品
            List<Product> products = cartMapper.findCartProducts(userId);
            if (products == null || products.isEmpty()) {
                return null;
            }

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Product p : products) {
                Integer qty = p.getCartQuantity();
                if (qty == null || qty <= 0) {
                    qty = 1;
                }
                totalAmount = totalAmount.add(p.getPrice().multiply(new BigDecimal(qty)));
            }

            Order order = new Order();
            order.setUserId(userId);
            order.setTotalAmount(totalAmount);
            order.setStatus("PENDING");
            order.setCreatedAt(LocalDateTime.now());
            orderMapper.insert(order);

            for (Product p : products) {
                Integer qty = p.getCartQuantity();
                if (qty == null || qty <= 0) {
                    qty = 1;
                }

                OrderItem item = new OrderItem();
                item.setOrderId(order.getId());
                item.setProductId(p.getId());
                item.setProductName(p.getName());
                item.setPrice(p.getPrice());
                item.setQuantity(qty);
                orderItemMapper.insert(item);

                productMapper.reduceStock(p.getId(), qty);
            }

            // 删除购物车项
            if (cartItemIds != null) {
                for (Integer cartItemId : cartItemIds) {
                    cartMapper.deleteById(cartItemId.longValue());
                }
            }

            order.setItems(orderItemMapper.findByOrderId(order.getId()));
            return order;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建订单失败: " + e.getMessage());
        }
    }

    public int payOrder(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("PAID");
        order.setPayTime(LocalDateTime.now());
        return orderMapper.update(order);
    }

    public int shipOrder(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("SHIPPED");
        order.setShipTime(LocalDateTime.now());
        return orderMapper.update(order);
    }

    public int deliverOrder(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("DELIVERED");
        return orderMapper.update(order);
    }

    public int cancelOrder(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("CANCELLED");
        return orderMapper.update(order);
    }
}
