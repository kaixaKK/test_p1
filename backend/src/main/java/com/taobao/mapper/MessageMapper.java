package com.taobao.mapper;

import com.taobao.entity.Message;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MessageMapper {
    @Select("SELECT m.*, u.username FROM messages m JOIN users u ON m.user_id = u.id WHERE m.product_id = #{productId} ORDER BY m.created_at DESC")
    List<Message> findByProductId(Long productId);

    @Select("SELECT m.*, u.username FROM messages m JOIN users u ON m.user_id = u.id ORDER BY m.created_at DESC")
    List<Message> findAll();

    @Select("SELECT m.*, u.username FROM messages m JOIN users u ON m.user_id = u.id WHERE m.product_id IN (SELECT id FROM products WHERE merchant_id = #{merchantId}) ORDER BY m.created_at DESC")
    List<Message> findByMerchantId(Long merchantId);

    @Insert("INSERT INTO messages (product_id, user_id, content) VALUES (#{productId}, #{userId}, #{content})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Message message);

    @Update("UPDATE messages SET reply = #{reply} WHERE id = #{id}")
    int updateReply(Message message);

    @Delete("DELETE FROM messages WHERE id = #{id}")
    int deleteById(Long id);
}
