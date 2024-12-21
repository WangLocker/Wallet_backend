package com.example.payment.mapper;

import com.example.payment.entity.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountMapper {

    @Insert("INSERT INTO bank_accounts (user_id, bank_id, account_number, is_verified, is_primary) VALUES (#{userId}, #{bankId}, #{accountNumber}, #{isVerified}, #{isPrimary})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertAccount(Account account);

    @Select("SELECT * FROM bank_accounts WHERE user_id = #{userId}")
    List<Account> getAccountsByUserId(Integer userId);

    @Select("SELECT * FROM bank_accounts WHERE account_number = #{accountNumber}")
    Account getAccountByAccountNumber(String accountNumber);


}
