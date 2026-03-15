package com.taobao.mapper;

import com.taobao.entity.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProductMapper {
    @Select("SELECT * FROM products WHERE id = #{id}")
    Product findById(Long id);

    @Select("SELECT * FROM products ORDER BY created_at DESC")
    List<Product> findAll();

    @Select("SELECT * FROM products WHERE status = 'ON_SALE' ORDER BY created_at DESC")
    List<Product> findOnSale();

    @Select("SELECT * FROM products WHERE merchant_id = #{merchantId}")
    List<Product> findByMerchantId(Long merchantId);

    @Select("SELECT * FROM products WHERE category = #{category} AND status = 'ON_SALE'")
    List<Product> findByCategory(String category);

    @Select("SELECT * FROM products WHERE is_seckill = TRUE AND status = 'ON_SALE' AND seckill_start_time <= NOW() AND seckill_end_time >= NOW()")
    List<Product> findSeckillProducts();

    @Insert("INSERT INTO products (merchant_id, name, description, price, stock, image_url, category, status, is_seckill, seckill_price, seckill_start_time, seckill_end_time) VALUES (#{merchantId}, #{name}, #{description}, #{price}, #{stock}, #{imageUrl}, #{category}, #{status}, #{isSeckill}, #{seckillPrice}, #{seckillStartTime}, #{seckillEndTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Product product);

    @Update("UPDATE products SET name=#{name}, description=#{description}, price=#{price}, stock=#{stock}, image_url=#{imageUrl}, category=#{category}, status=#{status}, is_seckill=#{isSeckill}, seckill_price=#{seckillPrice}, seckill_start_time=#{seckillStartTime}, seckill_end_time=#{seckillEndTime} WHERE id=#{id}")
    int update(Product product);

    @Delete("DELETE FROM products WHERE id = #{id}")
    int deleteById(Long id);

    @Update("UPDATE products SET stock = stock - #{quantity} WHERE id = #{productId} AND stock >= #{quantity}")
    int reduceStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);
}
