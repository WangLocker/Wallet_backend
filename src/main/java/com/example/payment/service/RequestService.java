package com.example.payment.service;

import com.example.payment.entity.*;
import com.example.payment.mapper.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class RequestService {

    private final UserMapper userMapper;
    private final PhoneMapper phoneMapper;
    private final EmailMapper emailMapper;
    private final RequestMapper requestMapper;
    private final PaymentMapper paymentMapper;
    private final AccountMapper accountMapper;

    public RequestService(UserMapper userMapper, PhoneMapper phoneMapper, EmailMapper emailMapper, RequestMapper requestMapper, PaymentMapper paymentMapper, AccountMapper accountMapper) {
        this.userMapper = userMapper;
        this.phoneMapper = phoneMapper;
        this.emailMapper = emailMapper;
        this.requestMapper = requestMapper;
        this.paymentMapper = paymentMapper;
        this.accountMapper = accountMapper;
    }


    public int paymentCheckAndInsert(String senderName, String recipientEmailOrPhone, String recipientType, Double amount, String memo) {

        if (userMapper.getUserByName(senderName) == null) {
            return -1; // sender not found
        }
        int senderId = userMapper.getUserByName(senderName).getId();
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

    public int addCard(String userName, String bankId, String accountNum) {
        User user = userMapper.getUserByName(userName);
        if (user == null) return -1;
        Integer userId = user.getId();
        Account account = new Account();
        account.setVerified(false);
        account.setAccountNumber(accountNum);
        account.setBankId(bankId);
        account.setUserId(userId);
        List<Account> accounts = accountMapper.getAccountsByUserId(userId);
        if (accounts.isEmpty()) account.setPrimary(true);
        else account.setPrimary(false);
        accountMapper.insertAccount(account);
        return 0;
    }
}
