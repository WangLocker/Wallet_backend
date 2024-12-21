package com.example.payment.service;

import com.example.payment.entity.Email;
import com.example.payment.entity.Payment;
import com.example.payment.entity.Phone;
import com.example.payment.mapper.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class RequestService {

    private final UserMapper userMapper;
    private final PhoneMapper phoneMapper;
    private final EmailMapper emailMapper;
    private final RequestMapper requestMapper;
    private final PaymentMapper paymentMapper;

    public RequestService(UserMapper userMapper, PhoneMapper phoneMapper, EmailMapper emailMapper, RequestMapper requestMapper, PaymentMapper paymentMapper) {
        this.userMapper = userMapper;
        this.phoneMapper = phoneMapper;
        this.emailMapper = emailMapper;
        this.requestMapper = requestMapper;
        this.paymentMapper = paymentMapper;
    }


    public int paymentCheckAndInsert(Integer senderId, String recipientEmailOrPhone, String recipientType, Double amount, String memo) {
        if (userMapper.getUserById(senderId) == null) {
            return -1; // sender not found
        }
        int recipientId = -1;
        if (recipientType.equals("phone")) {
            Phone phone = phoneMapper.getPhoneByNumber(recipientEmailOrPhone);
            if (phone == null) return -2; // recipient not found
            else recipientId = phone.getUserId();
        } else if (recipientType.equals("email")) {
            Email email = emailMapper.getEmailByEmail(recipientEmailOrPhone);
            if (email == null) return -2;
            else recipientId = email.getUserId();
        } else {
            return -2;
        }
        if (recipientId == -1) return -2;
        if (amount < 0) return -3; // invalid payment amount
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setSenderId(senderId);
        payment.setRecipientId(recipientId);
        payment.setRecipientEmailOrPhone(recipientEmailOrPhone);
        payment.setRecipientType(recipientType);
        payment.setMemo(memo);
        payment.setStatus("pending");
        paymentMapper.insertPayment(payment);
        return 0;
    }
}
