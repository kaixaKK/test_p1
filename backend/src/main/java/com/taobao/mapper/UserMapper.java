package com.taobao.mapper;

import com.taobao.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Select("SELECT * FROM users")
    List<User> findAll();

    @Select("SELECT * FROM users WHERE role = #{role}")
    List<User> findByRole(String role);

    @Insert("INSERT INTO users (username, password, role, phone, email) VALUES (#{username}, #{password}, #{role}, #{phone}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE users SET username=#{username}, password=#{password}, role=#{role}, phone=#{phone}, email=#{email} WHERE id=#{id}")
    int update(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(Long id);
}
