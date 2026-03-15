package com.taobao.controller;

import com.taobao.entity.Product;
import com.taobao.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/on-sale")
    public List<Product> getOnSaleProducts() {
        return productService.findOnSale();
    }

    @GetMapping("/merchant/{merchantId}")
    public List<Product> getProductsByMerchantId(@PathVariable Long merchantId) {
        return productService.findByMerchantId(merchantId);
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.findByCategory(category);
    }

    @GetMapping("/seckill")
    public List<Product> getSeckillProducts() {
        return productService.findSeckillProducts();
    }

    @PostMapping
    public Map<String, Object> saveProduct(@RequestBody Product product) {
        Map<String, Object> result = new HashMap<>();
        productService.save(product);
        result.put("success", true);
        result.put("product", product);
        return result;
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", productService.update(product) > 0);
        return result;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteProduct(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", productService.deleteById(id) > 0);
        return result;
    }

    @PostMapping("/{id}/reduce-stock")
    public Map<String, Object> reduceStock(@PathVariable Long id, @RequestParam Integer quantity) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", productService.reduceStock(id, quantity));
        return result;
    }
}
