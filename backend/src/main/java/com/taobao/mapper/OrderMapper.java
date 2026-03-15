package com.taobao.mapper;

import com.taobao.entity.Order;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order findById(Long id);

    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Order> findByUserId(Long userId);

    @Select("SELECT * FROM orders ORDER BY created_at DESC")
    List<Order> findAll();

    @Insert("INSERT INTO orders (user_id, total_amount, status) VALUES (#{userId}, #{totalAmount}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Update("UPDATE orders SET status=#{status}, pay_time=#{payTime}, ship_time=#{shipTime} WHERE id=#{id}")
    int update(Order order);

    @Delete("DELETE FROM orders WHERE id = #{id}")
    int deleteById(Long id);
}
