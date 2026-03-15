package com.taobao.mapper;

import com.taobao.entity.OrderItem;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface OrderItemMapper {
    @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
    List<OrderItem> findByOrderId(Long orderId);

    @Insert("INSERT INTO order_items (order_id, product_id, product_name, price, quantity) VALUES (#{orderId}, #{productId}, #{productName}, #{price}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderItem orderItem);

    @Delete("DELETE FROM order_items WHERE order_id = #{orderId}")
    int deleteByOrderId(Long orderId);
}
