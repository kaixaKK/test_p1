package com.taobao.service;

import com.taobao.entity.Product;
import com.taobao.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public Product findById(Long id) {
        return productMapper.findById(id);
    }

    public List<Product> findAll() {
        return productMapper.findAll();
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

    public Product save(Product product) {
        if (product.getId() == null) {
            productMapper.insert(product);
        } else {
            productMapper.update(product);
        }
        return product;
    }

    public int update(Product product) {
        return productMapper.update(product);
    }

    public int deleteById(Long id) {
        return productMapper.deleteById(id);
    }

    public boolean reduceStock(Long productId, Integer quantity) {
        return productMapper.reduceStock(productId, quantity) > 0;
    }
}
