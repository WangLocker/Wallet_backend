package com.example.payment.entity;

import java.util.Date;

public class Request {
    private static Integer currentIdNum = 0;
    private Integer id;
    private Integer requesterId;
    private Integer recipientId;
    private String recipientEmailOrPhone;
    private Double amount;
    private Double totalAmount;
    private String memo;
    private String status;
    private Date initiatedAt;
    private Date completedAt;

    public Request() {
        this.status = "pending";
        this.initiatedAt = new Date();
    }

    public static Integer getCurrentIdNum() {
        return currentIdNum;
    }

    public static void addCurrentIdNum() {
        currentIdNum ++;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRequesterId() {
        return requesterId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public String getRecipientEmailOrPhone() {
        return recipientEmailOrPhone;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public String getMemo() {
        return memo;
    }

    public String getStatus() {
        return status;
    }

    public Date getInitiatedAt() {
        return initiatedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRequesterId(Integer requesterId) {
        this.requesterId = requesterId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public void setRecipientEmailOrPhone(String recipientEmailOrPhone) {
        this.recipientEmailOrPhone = recipientEmailOrPhone;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInitiatedAt(Date initiatedAt) {
        this.initiatedAt = initiatedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }
}
