package com.taobao.mapper;

import com.taobao.entity.Cart;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CartMapper {
    @Select("SELECT * FROM cart WHERE user_id = #{userId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "productId", column = "product_id"),
        @Result(property = "productName", column = "product_name"),
        @Result(property = "productImage", column = "product_image"),
        @Result(property = "productStock", column = "product_stock")
    })
    List<Cart> findByUserId(Long userId);

    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND product_id = #{productId}")
    Cart findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Insert("INSERT INTO cart (user_id, product_id, quantity, price) VALUES (#{userId}, #{productId}, #{quantity}, #{price})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Cart cart);

    @Update("UPDATE cart SET quantity = #{quantity} WHERE id = #{id}")
    int update(Cart cart);

    @Delete("DELETE FROM cart WHERE id = #{id}")
    int deleteById(Long id);

    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    @Update("UPDATE cart SET quantity = quantity + #{quantity} WHERE user_id = #{userId} AND product_id = #{productId}")
    int increaseQuantity(@Param("userId") Long userId, @Param("productId") Long productId, @Param("quantity") Integer quantity);
}
