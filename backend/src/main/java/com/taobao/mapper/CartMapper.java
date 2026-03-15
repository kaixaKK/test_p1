package com.taobao.mapper;

import com.taobao.entity.CartItem;
import com.taobao.entity.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CartMapper {
    @Select("SELECT * FROM cart_items WHERE user_id = #{userId}")
    List<CartItem> findByUserId(Long userId);

    @Select("SELECT p.*, c.id as cartId, c.quantity as cartQuantity FROM cart_items c JOIN products p ON c.product_id = p.id WHERE c.user_id = #{userId}")
    List<Product> findCartProducts(Long userId);

    @Select("SELECT * FROM cart_items WHERE user_id = #{userId} AND product_id = #{productId}")
    CartItem findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Insert("INSERT INTO cart_items (user_id, product_id, quantity) VALUES (#{userId}, #{productId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(CartItem cartItem);

    @Update("UPDATE cart_items SET quantity = #{quantity} WHERE id = #{id}")
    int updateQuantity(CartItem cartItem);

    @Delete("DELETE FROM cart_items WHERE id = #{id}")
    int deleteById(Long id);

    @Delete("DELETE FROM cart_items WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);
}
