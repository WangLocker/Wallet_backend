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

    public Integer getUserId(String userName) {
        User user = userMapper.getUserByName(userName);
        if (user == null) return null;
        else  return user.getId();
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

    public List<Map<String, String>> getPendingRequestsAndPayments(Integer userId) {
        List<Map<String, String>> pendingRequests = new ArrayList<>();

        List<Request> requestsList = requestMapper.getRequestsOfRecipient(userId);
        List<Payment> paymentsList = paymentMapper.getPaymentsOfRecipient(userId);
        for (Request request : requestsList) {
            if (request.getStatus().equals("pending")) {
                Map<String, String> tempTrans = new HashMap<>();
                tempTrans.put("t_id", String.valueOf(request.getId()));
                tempTrans.put("t_type", "request");
                tempTrans.put("t_requester_id", String.valueOf(userMapper.getUserById(request.getRequesterId()).getName()));
                tempTrans.put("t_recipient_id", String.valueOf(userMapper.getUserById(request.getRecipientId()).getName()));
                tempTrans.put("t_amount", String.valueOf(request.getAmount()));
                tempTrans.put("t_memo", String.valueOf(request.getMemo()));
                pendingRequests.add(tempTrans);
            }
        }
        for (Payment payment : paymentsList) {
            if (payment.getStatus().equals("pending")) {
                Map<String, String> tempTrans = new HashMap<>();
                tempTrans.put("t_id", String.valueOf(payment.getId()));
                tempTrans.put("t_type", "payment");
                tempTrans.put("t_requester_id", String.valueOf(userMapper.getUserById(payment.getSenderId()).getName()));
                tempTrans.put("t_recipient_id", String.valueOf(userMapper.getUserById(payment.getRecipientId()).getName()));
                tempTrans.put("t_amount", String.valueOf(payment.getAmount()));
                tempTrans.put("t_memo", String.valueOf(payment.getMemo()));
                pendingRequests.add(tempTrans);
            }
        }
        return pendingRequests;
    }

    public Map<String, String> getTransDetail(String transNum) {
        Map<String, String> queryForm = new HashMap<>();
        char transType = transNum.charAt(0);
        Integer transId = Integer.parseInt(transNum.substring(1));
        queryForm.put("q_id", String.valueOf(transId));
        queryForm.put("q_type", transType == '0' ? "payment" : "request");
        if (transType == '0') {
            Payment payment = paymentMapper.getPaymentOfId(transId);
            if (payment == null) return null;
            queryForm.put("q_sender", userMapper.getUserById(payment.getSenderId()).getName());
            queryForm.put("q_sender_card", payment.getSenderAccountNumber() == null ? "" : payment.getSenderAccountNumber());
            queryForm.put("q_receiver", userMapper.getUserById(payment.getRecipientId()).getName());
            queryForm.put("q_receiver_card", payment.getRecipientAccountNumber() == null ? "" : payment.getRecipientAccountNumber());
            queryForm.put("q_amount", String.valueOf(payment.getAmount()));
            queryForm.put("q_memo", payment.getMemo());
            queryForm.put("q_status", payment.getStatus());
            queryForm.put("q_initiated_at", payment.getInitiatedAt().toString());
            queryForm.put("q_completed_at", payment.getCompletedAt() == null ? "" : payment.getCompletedAt().toString());
        } else {
            Request request = requestMapper.getRequestOfId(transId);
            if (request == null) return null;
            queryForm.put("q_sender", userMapper.getUserById(request.getRequesterId()).getName());
            queryForm.put("q_sender_card", request.getRequesterAccountNumber() == null ? "" : request.getRequesterAccountNumber());
            queryForm.put("q_receiver", userMapper.getUserById(request.getRecipientId()).getName());
            queryForm.put("q_receiver_card", request.getRecipientAccountNumber() == null ? "" : request.getRecipientAccountNumber());
            queryForm.put("q_amount", String.valueOf(request.getAmount()));
            queryForm.put("q_memo", request.getMemo());
            queryForm.put("q_status", request.getStatus());
            queryForm.put("q_initiated_at", request.getInitiatedAt().toString());
            queryForm.put("q_completed_at", request.getCompletedAt() == null ? "" : request.getCompletedAt().toString());
        }
        return queryForm;
    }



}
