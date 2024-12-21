package com.example.payment.service;

import com.example.payment.entity.*;
import com.example.payment.mapper.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueryService {

    private final AccountMapper accountMapper;
    private final RequestMapper requestMapper;
    private final UserMapper userMapper;
    private final EmailMapper emailMapper;
    private final PaymentMapper paymentMapper;

    public QueryService(AccountMapper accountMapper, RequestMapper requestMapper, UserMapper userMapper, EmailMapper emailMapper, PaymentMapper paymentMapper) {
        this.accountMapper = accountMapper;
        this.requestMapper = requestMapper;
        this.userMapper = userMapper;
        this.emailMapper = emailMapper;
        this.paymentMapper = paymentMapper;
    }

    /**
     * 查找用户账户逻辑
     * @param userId
     * @return 用户账户的 list
     */
    public List<Account> getUserCard(Integer userId) {
        return accountMapper.getAccountsByUserId(userId);
    }

    public List<Double> getUserMonthlyData(Integer userId) {
        List<Request> recipientList = requestMapper.getRequestsOfRecipient(userId);
        List<Request> requesterList = requestMapper.getRequestsOfRequester(userId);
        int validSum = 0;
        double payment = 0;
        double income = 0;
        for (Request request : recipientList) {
            if (request.getStatus().equals("completed")) {
                payment += request.getAmount();
                validSum ++;
            }
        }
        for (Request request : requesterList) {
            if (request.getStatus().equals("completed")) {
                income += request.getAmount();
                validSum ++;
            }
        }

        List<Payment> senderList = paymentMapper.getPaymentsOfSender(userId);
        List<Payment> recipientPList = paymentMapper.getPaymentsOfRecipient(userId);

        for (Payment payment1 : senderList) {
            if (payment1.getStatus().equals("completed")) {
                payment += payment1.getAmount();
                validSum ++;
            }
        }
        for (Payment payment1 : recipientPList) {
            if (payment1.getStatus().equals("completed")) {
                income += payment1.getAmount();
                validSum ++;
            }
        }

        return new ArrayList<>(Arrays.asList((double)validSum, income, payment));
    }

    public String getUserNameOfEmail(String emailAddress) {
        Email email = emailMapper.getEmailByEmail(emailAddress);
        if (email == null) return null;
        return userMapper.getUserById(email.getUserId()).getName();
    }

    public List<Map<String, String>> getTransactionData(Integer userId) {
        List<Map<String, String>> transactions = new ArrayList<>();

        List<Request> recipientList = requestMapper.getRequestsOfRecipient(userId);
        List<Request> requesterList = requestMapper.getRequestsOfRequester(userId);
        for (Request request : recipientList) {
            Map<String, String> tempTrans = new HashMap<>();
            tempTrans.put("trans_num", "1" + request.getId().toString());
            tempTrans.put("status", request.getStatus());
            transactions.add(tempTrans);
        }
        for (Request request : requesterList) {
            Map<String, String> tempTrans = new HashMap<>();
            tempTrans.put("trans_num", "1" + request.getId().toString());
            tempTrans.put("status", request.getStatus());
            transactions.add(tempTrans);
        }

        List<Payment> senderList = paymentMapper.getPaymentsOfSender(userId);
        List<Payment> recipientPList = paymentMapper.getPaymentsOfRecipient(userId);

        for (Payment payment1 : senderList) {
            Map<String, String> tempTrans = new HashMap<>();
            tempTrans.put("trans_num", "0" + payment1.getId().toString());
            tempTrans.put("status", payment1.getStatus());
            transactions.add(tempTrans);
        }
        for (Payment payment1 : recipientPList) {
            Map<String, String> tempTrans = new HashMap<>();
            tempTrans.put("trans_num", "0" + payment1.getId().toString());
            tempTrans.put("status", payment1.getStatus());
            transactions.add(tempTrans);
        }

        return transactions;
    }





}
