package com.example.payment.mapper;

import com.example.payment.entity.Payment;
import com.example.payment.entity.Request;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RequestMapper {

    @Insert("INSERT INTO requests (id, requester_id, recipient_id, recipient_email_or_phone, amount, total_amount, memo, status, initiated_at, completed_at) VALUES (#{id}, #{requesterId}, #{recipientId}, #{recipientEmailOrPhone}, #{amount}, #{totalAmount}, #{memo}, #{status}, #{initiatedAt}, #{completed_at})")
    void insertRequest(Request request);
//    @Options(useGeneratedKeys = true, keyProperty = "id")


    @Select("SELECT * FROM requests WHERE requester_id = #{requesterId}")
    List<Request> getRequestsOfRequester(Integer requesterId);

    @Select("SELECT * FROM requests WHERE recipient_id = #{recipientId}")
    List<Request> getRequestsOfRecipient(Integer recipientId);

    @Select("SELECT * FROM requests WHERE id = #{id}")
    Request getRequestOfId(Integer id);

}
