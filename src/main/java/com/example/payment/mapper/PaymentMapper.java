package com.example.payment.mapper;

import com.example.payment.entity.Payment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PaymentMapper {

    @Insert("INSERT INTO payments (sender_id, recipient_id, recipient_email_or_phone, recipient_type, amount, memo, status, cancellation_reason) VALUES (#{senderId}, #{recipientId}, #{recipientEmailOrPhone}, #{recipientType}, #{amount}, #{memo}, #{status}, #{cancellationReason})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertPayment(Payment payment);

    @Select("SELECT * FROM payments WHERE sender_id = #{senderId}")
    List<Payment> getPaymentsOfSender(Integer senderId);

    @Select("SELECT * FROM payments WHERE recipient_id = #{recipientId}")
    List<Payment> getPaymentsOfRecipient(Integer recipientId);

    @Select("SELECT * FROM payments WHERE id = #{id}")
    Payment getPaymentOfId(Integer id);



}
