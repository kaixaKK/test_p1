package com.taobao.controller;

import com.taobao.entity.Product;
import com.taobao.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Map<String, Object> getProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        Map<String, Object> result = new HashMap<>();
        if (product != null) {
            result.put("success", true);
            result.put("data", product);
        } else {
            result.put("success", false);
            result.put("message", "商品不存在");
        }
        return result;
    }

    @GetMapping
    public Map<String, Object> getAllProducts() {
        List<Product> products = productService.findAll();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", products);
        return result;
    }

    @GetMapping("/onsale")
    public Map<String, Object> getOnSaleProducts() {
        List<Product> products = productService.findOnSale();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", products);
        return result;
    }

    @GetMapping("/merchant/{merchantId}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> getProductsByMerchant(@PathVariable Long merchantId) {
        List<Product> products = productService.findByMerchantId(merchantId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", products);
        return result;
    }

    @GetMapping("/category/{category}")
    public Map<String, Object> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.findByCategory(category);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", products);
        return result;
    }

    @GetMapping("/seckill")
    public Map<String, Object> getSeckillProducts() {
        List<Product> products = productService.findSeckillProducts();
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", products);
        return result;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> createProduct(@RequestBody Product product) {
        productService.create(product);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "商品创建成功");
        result.put("data", product);
        return result;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", productService.update(product) > 0);
        return result;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MERCHANT', 'ADMIN')")
    public Map<String, Object> deleteProduct(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", productService.deleteById(id) > 0);
        return result;
    }
}
