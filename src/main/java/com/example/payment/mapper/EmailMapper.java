package com.example.payment.mapper;

import com.example.payment.entity.Email;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmailMapper {

    @Insert("INSERT INTO user_emails (user_id, email, is_verified) VALUES (#{userId}, #{email}, #{isVerified})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertEmail(Email email);

    @Select("SELECT * FROM user_emails WHERE user_id = #{userId}")
    Email getEmailByUserId(Integer userId);

    @Select("SELECT * FROM user_emails WHERE email = #{email}")
    Email getEmailByEmail(String email);
}
