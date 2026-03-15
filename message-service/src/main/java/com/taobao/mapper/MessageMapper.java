package com.taobao.mapper;

import com.taobao.entity.Message;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MessageMapper {
    @Select("SELECT * FROM messages WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Message> findByUserId(Long userId);

    @Select("SELECT * FROM messages WHERE user_id = #{userId} AND is_read = 0 ORDER BY create_time DESC")
    List<Message> findUnreadByUserId(Long userId);

    @Select("SELECT * FROM messages WHERE id = #{id}")
    Message findById(Long id);

    @Insert("INSERT INTO messages (user_id, type, title, content, is_read) VALUES (#{userId}, #{type}, #{title}, #{content}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Message message);

    @Update("UPDATE messages SET is_read = 1, read_time = NOW() WHERE id = #{id}")
    int markAsRead(Long id);

    @Delete("DELETE FROM messages WHERE id = #{id}")
    int deleteById(Long id);
}
