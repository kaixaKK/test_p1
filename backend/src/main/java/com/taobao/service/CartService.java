package com.taobao.service;

import com.taobao.entity.CartItem;
import com.taobao.entity.Product;
import com.taobao.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;

    public List<Product> findCartProducts(Long userId) {
        return cartMapper.findCartProducts(userId);
    }

    public Map<String, Object> addToCart(Long userId, Long productId, Integer quantity) {
        CartItem existing = cartMapper.findByUserIdAndProductId(userId, productId);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            cartMapper.updateQuantity(existing);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartMapper.insert(cartItem);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }

    public int updateQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setQuantity(quantity);
        return cartMapper.updateQuantity(cartItem);
    }

    public int deleteById(Long id) {
        return cartMapper.deleteById(id);
    }

    public int clearCart(Long userId) {
        return cartMapper.deleteByUserId(userId);
    }
}
