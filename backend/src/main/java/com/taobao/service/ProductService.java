package com.taobao.service;

import com.taobao.entity.Product;
import com.taobao.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String PRODUCT_KEY = "product:";
    private static final String PRODUCT_LIST_KEY = "product:list:";
    private static final long CACHE_TIMEOUT = 30; // 缓存30分钟

    public Product findById(Long id) {
        // 先从 Redis 缓存获取
        String key = PRODUCT_KEY + id;
        Product cached = (Product) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        // 缓存未命中，从数据库查询
        Product product = productMapper.findById(id);
        if (product != null) {
            redisTemplate.opsForValue().set(key, product, CACHE_TIMEOUT, TimeUnit.MINUTES);
        }
        return product;
    }

    public List<Product> findAll() {
        String key = PRODUCT_LIST_KEY + "all";
        List<Product> cached = (List<Product>) redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return cached;
        }

        List<Product> products = productMapper.findAll();
        if (!products.isEmpty()) {
            redisTemplate.opsForValue().set(key, products, CACHE_TIMEOUT, TimeUnit.MINUTES);
        }
        return products;
    }

    public List<Product> findOnSale() {
        return productMapper.findOnSale();
    }

    public List<Product> findByMerchantId(Long merchantId) {
        return productMapper.findByMerchantId(merchantId);
    }

    public List<Product> findByCategory(String category) {
        return productMapper.findByCategory(category);
    }

    public List<Product> findSeckillProducts() {
        return productMapper.findSeckillProducts();
    }

    public Product create(Product product) {
        product.setStatus("ON_SALE");
        productMapper.insert(product);
        // 清除列表缓存
        redisTemplate.delete(PRODUCT_LIST_KEY + "all");
        return product;
    }

    public int update(Product product) {
        int result = productMapper.update(product);
        if (result > 0) {
            // 更新缓存
            String key = PRODUCT_KEY + product.getId();
            redisTemplate.opsForValue().set(key, product, CACHE_TIMEOUT, TimeUnit.MINUTES);
            // 清除列表缓存
            redisTemplate.delete(PRODUCT_LIST_KEY + "all");
        }
        return result;
    }

    public int deleteById(Long id) {
        int result = productMapper.deleteById(id);
        if (result > 0) {
            // 删除缓存
            redisTemplate.delete(PRODUCT_KEY + id);
            // 清除列表缓存
            redisTemplate.delete(PRODUCT_LIST_KEY + "all");
        }
        return result;
    }

    public boolean reduceStock(Long productId, Integer quantity) {
        // 先更新数据库
        boolean success = productMapper.reduceStock(productId, quantity) > 0;
        if (success) {
            // 清除缓存
            redisTemplate.delete(PRODUCT_KEY + productId);
            redisTemplate.delete(PRODUCT_LIST_KEY + "all");
        }
        return success;
    }
}
