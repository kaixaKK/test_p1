package com.taobao.controller;

import com.taobao.entity.Product;
import com.taobao.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public Map<String, Object> getCartProducts(@PathVariable Long userId) {
        List<Product> products = cartService.findCartProducts(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", products);
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> addToCart(@RequestBody Map<String, Object> params) {
        Long userId = Long.valueOf(params.get("userId").toString());
        Long productId = Long.valueOf(params.get("productId").toString());
        Integer quantity = Integer.valueOf(params.get("quantity").toString());
        Map<String, Object> result = cartService.addToCart(userId, productId, quantity);
        return result;
    }

    @PutMapping("/update/{cartItemId}")
    public Map<String, Object> updateQuantity(@PathVariable Long cartItemId, @RequestBody Map<String, Integer> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", cartService.updateQuantity(cartItemId, params.get("quantity")) > 0);
        return result;
    }

    @DeleteMapping("/{cartItemId}")
    public Map<String, Object> deleteCartItem(@PathVariable Long cartItemId) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", cartService.deleteById(cartItemId) > 0);
        return result;
    }

    @DeleteMapping("/clear/{userId}")
    public Map<String, Object> clearCart(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", cartService.clearCart(userId) >= 0);
        return result;
    }
}
