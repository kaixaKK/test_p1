package com.taobao.service;

import com.taobao.entity.Cart;
import com.taobao.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;

    @Value("${service.product.url:http://product-service}")
    private String productServiceUrl;

    public List<Cart> findByUserId(Long userId) {
        List<Cart> carts = cartMapper.findByUserId(userId);
        // 填充商品信息
        for (Cart cart : carts) {
            fillProductInfo(cart);
        }
        return carts;
    }

    private void fillProductInfo(Cart cart) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = productServiceUrl + "/products/" + cart.getProductId();
            Object product = restTemplate.getForObject(url, Object.class);
            // 这里简化处理，实际应该定义Product响应对象
            cart.setProductName("商品" + cart.getProductId());
        } catch (Exception e) {
            cart.setProductName("商品" + cart.getProductId());
        }
    }

    public Cart addToCart(Cart cart) {
        Cart existing = cartMapper.findByUserIdAndProductId(cart.getUserId(), cart.getProductId());
        if (existing != null) {
            // 购物车已存在该商品，增加数量
            cartMapper.increaseQuantity(cart.getUserId(), cart.getProductId(), cart.getQuantity());
            return cartMapper.findByUserIdAndProductId(cart.getUserId(), cart.getProductId());
        } else {
            cartMapper.insert(cart);
            return cart;
        }
    }

    public boolean updateQuantity(Long cartId, Integer quantity) {
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setQuantity(quantity);
        return cartMapper.update(cart) > 0;
    }

    public boolean removeFromCart(Long cartId) {
        return cartMapper.deleteById(cartId) > 0;
    }

    public boolean clearCart(Long userId) {
        return cartMapper.deleteByUserId(userId) > 0;
    }
}
