package com.example.payment.mapper;

import com.example.payment.entity.Phone;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PhoneMapper {

    // 插入新的电话记录
    @Insert("INSERT INTO user_phone (user_id, phone, is_verified) VALUES (#{userId}, #{phone}, #{isVerified})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPhone(Phone phone);

    // 根据用户 ID 获取电话记录
    @Select("SELECT * FROM user_phone WHERE user_id = #{userId}")
    List<Phone> getPhonesByUserId(Integer userId);

    // 根据电话号码获取记录
    @Select("SELECT * FROM user_phone WHERE phone = #{phone}")
    Phone getPhoneByNumber(String phone);

    // 更新电话号码的验证状态
    @Update("UPDATE user_phone SET is_verified = #{isVerified} WHERE id = #{id}")
    void updatePhoneVerificationStatus(@Param("id") Integer id, @Param("isVerified") Boolean isVerified);

    // 删除电话号码
    @Delete("DELETE FROM user_phone WHERE id = #{id}")
    void deletePhone(Integer id);
}
