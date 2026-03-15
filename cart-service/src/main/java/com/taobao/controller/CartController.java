package com.taobao.controller;

import com.taobao.entity.Cart;
import com.taobao.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carts")
@CrossOrigin
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/user/{userId}")
    public List<Cart> getUserCart(@PathVariable Long userId) {
        return cartService.findByUserId(userId);
    }

    @PostMapping
    public Map<String, Object> addToCart(@RequestBody Cart cart) {
        Map<String, Object> result = new HashMap<>();
        Cart saved = cartService.addToCart(cart);
        result.put("success", true);
        result.put("cart", saved);
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", cartService.updateQuantity(id, quantity));
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> removeFromCart(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", cartService.removeFromCart(id));
        return result;
    }

    @DeleteMapping("/user/{userId}")
    public Map<String, Object> clearCart(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", cartService.clearCart(userId));
        return result;
    }
}
