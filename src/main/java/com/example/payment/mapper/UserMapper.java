package com.example.payment.mapper;

import com.example.payment.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserById(Integer id);
    @Select("SELECT * FROM users WHERE email = #{email}")
    User getUserByEmail(String email);
    @Select("SELECT * FROM users WHERE phone = #{phone}")
    User getUserByPhone(String phone);
    @Select("SELECT * FROM users WHERE ssn = #{ssn}")
    User getUserBySsn(String ssn);
    @Insert("INSERT INTO users (name, ssn, phone, email,password, is_email_verified, is_phone_verified) " +
            "VALUES (#{name}, #{ssn}, #{phone}, #{email},#{password}, #{isEmailVerified}, #{isPhoneVerified})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

}
