package com.example.payment.service;

import com.example.payment.entity.*;
import com.example.payment.mapper.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

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


    public Map paymentCheckAndInsert(String senderName, String recipientEmailOrPhone, String recipientType, Double amount, String memo) {

        if (userMapper.getUserByName(senderName) == null) {
            return null; // sender not found
        }
        int senderId = userMapper.getUserByName(senderName).getId();
        int recipientId = -1;
        if (recipientType.equals("phone")) {
            Phone phone = phoneMapper.getPhoneByNumber(recipientEmailOrPhone);
            if (phone == null) return null; // recipient not found
            else recipientId = phone.getUserId();
        } else if (recipientType.equals("email")) {
            Email email = emailMapper.getEmailByEmail(recipientEmailOrPhone);
            if (email == null) return null;
            else recipientId = email.getUserId();
        } else {
            return null;
        }
        if (recipientId == -1) return null;
        if (amount < 0) return null; // invalid payment amount
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setSenderId(senderId);
        payment.setRecipientId(recipientId);
        payment.setRecipientEmailOrPhone(recipientEmailOrPhone);
        payment.setRecipientType(recipientType);
        payment.setMemo(memo);
        payment.setStatus("completed");
        paymentMapper.insertPayment(payment);

        Map payOutForm = new HashMap();
        payOutForm.put("p_id", "");
        payOutForm.put("p_sender_id", userMapper.getUserById(senderId).getName());
        payOutForm.put("p_recipient_id", userMapper.getUserById(recipientId).getName());
        payOutForm.put("p_recipient_email_or_phone", recipientEmailOrPhone);
        payOutForm.put("p_recipient_type", recipientType);
        payOutForm.put("p_amount", amount.toString());
        payOutForm.put("p_memo", memo);
        payOutForm.put("p_status", payment.getStatus());
        payOutForm.put("p_initiated_at", new Date());
        payOutForm.put("p_completed_at", "");
        payOutForm.put("p_cancellation_reason", "");

        return payOutForm;
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

    public int handleRequest(Integer requestId, String requesterName, String recipientName) {
        Request request = requestMapper.getRequestOfId(requestId);
        if (request == null) return -1;
        User requester = userMapper.getUserByName(requesterName);
        if (requester == null) return -2;
        User recipient = userMapper.getUserByName(recipientName);
        if (recipient == null) return -3;
        Integer recipientId = recipient.getId();
        Integer requesterId = requester.getId();
        requestMapper.completeRequest(requestId, requesterId, recipientId);
        return 0;
    }

    public Integer getUserIdByEmailOrPhone(String userInfo, String userType) {
        if (userType.equals("email")) {
            Email email = emailMapper.getEmailByEmail(userInfo);
            if (email == null) return null;
            else return email.getUserId();
        } else {
            Phone phone = phoneMapper.getPhoneByNumber(userInfo);
            if (phone == null) return null;
            else return phone.getUserId();
        }
    }

    public Map submitFetch(String requesterName, Map<?, ?> fetchForm) {
        User requester = userMapper.getUserByName(requesterName);
        if (requester == null) return null;
        Map tempForm = new HashMap<>();
        double totalAmount = 0;
        Integer primaryRecipientId = getUserIdByEmailOrPhone((String) fetchForm.get("infofetchee"), (String) fetchForm.get("fetcheeType"));
        if (primaryRecipientId == null) return null;

        // get total amount and check user availability
        totalAmount += Double.parseDouble((String) fetchForm.get("amount"));
        List<Map<?, ?>> extraPayers = (List<Map<?, ?>>) fetchForm.get("extraPayers");
        if (!extraPayers.isEmpty()) {
            for (Map<?, ?> extraPayer : extraPayers) {
                Integer currentRecipientId = getUserIdByEmailOrPhone((String) extraPayer.get("infofetchee"), (String) extraPayer.get("fetcheeType"));
                if (currentRecipientId == null) return null;
                else totalAmount += Double.parseDouble((String) extraPayer.get("amount"));
            }
        }

        // add form
        Request request = new Request();
        request.setRequesterId(requester.getId());
        request.setRecipientId(primaryRecipientId);
        request.setRecipientEmailOrPhone((String) fetchForm.get("infofetchee"));
        request.setAmount(Double.parseDouble((String) fetchForm.get("amount")));
        request.setTotalAmount(totalAmount);
        request.setMemo((String) fetchForm.get("memo"));
        request.setStatus("pending");
        request.setInitiatedAt(new Date());

        System.out.println(request.toString());
        requestMapper.insertRequest(request);
        System.out.println("insert success");

        tempForm.put("f_id", " ");
        tempForm.put("f_requester_id", requesterName);
        tempForm.put("f_recipient_id", userMapper.getUserById(primaryRecipientId).getName());
        tempForm.put("f_recipient_email_or_phone", request.getRecipientEmailOrPhone());
        tempForm.put("f_amount", request.getAmount().toString());
        tempForm.put("f_status", request.getStatus());
        tempForm.put("f_initiated_at", request.getInitiatedAt().toString());
        tempForm.put("f_completed_at", "");

        List<Map<String, String>> extraList = new ArrayList<>();
        if (!extraPayers.isEmpty()) {
            for (Map<?, ?> extraPayer : extraPayers) {
                Integer currentRecipientId = getUserIdByEmailOrPhone((String) extraPayer.get("infofetchee"), (String) extraPayer.get("fetcheeType"));
                request.setRecipientId(currentRecipientId);
                request.setRecipientEmailOrPhone((String) extraPayer.get("infofetchee"));
                request.setAmount(Double.parseDouble((String) extraPayer.get("amount")));

                System.out.println(request.toString());
                requestMapper.insertRequest(request);
                System.out.println("insert success");

                Map<String, String> nowExtra = new HashMap<>();
                nowExtra.put("f_id", " ");
                nowExtra.put("f_requester_id", requesterName);
                nowExtra.put("f_recipient_id", userMapper.getUserById(currentRecipientId).getName());
                nowExtra.put("f_amount", request.getAmount().toString());

                extraList.add(nowExtra);
            }
        }

        tempForm.put("f_extraPayers", extraList);
        return tempForm;

    }
}
