package com.taobao.mapper;

import com.taobao.entity.Order;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order findById(Long id);

    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Order findByOrderNo(String orderNo);

    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Order> findByUserId(Long userId);

    @Insert("INSERT INTO orders (order_no, user_id, total_amount, status, receiver_name, receiver_phone, receiver_address, remark) VALUES (#{orderNo}, #{userId}, #{totalAmount}, #{status}, #{receiverName}, #{receiverPhone}, #{receiverAddress}, #{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    @Update("UPDATE orders SET status = #{status}, pay_time = #{payTime}, deliver_time = #{deliverTime}, receive_time = #{receiveTime} WHERE id = #{id}")
    int update(Order order);

    @Delete("DELETE FROM orders WHERE id = #{id}")
    int deleteById(Long id);
}
