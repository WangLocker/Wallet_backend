package com.example.payment.mapper;

import com.example.payment.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO users (name, ssn, password) VALUES (#{name}, #{ssn}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(Integer id);

    @Select("SELECT * FROM users WHERE ssn = #{ssn}")
    User getUserBySsn(String ssn);

    @Select("SELECT * FROM users WHERE name = #{name}")
    User getUserByName(String name);

}
