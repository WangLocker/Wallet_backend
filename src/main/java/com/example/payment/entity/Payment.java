package com.example.payment.entity;

import java.util.Date;

public class Payment {
    private Integer id;
    private Integer senderId;
    private Integer recipientId;
    private String recipientEmailOrPhone;
    private String recipientType;
    private Double amount;
    private String memo;
    private String status;
    private Date initiatedAt;
    private Date completedAt;
    private String cancellationReason;
    private String senderAccountNumber;
    private String recipientAccountNumber;

    public Integer getId() {
        return id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public String getRecipientEmailOrPhone() {
        return recipientEmailOrPhone;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public Double getAmount() {
        return amount;
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

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public void setRecipientEmailOrPhone(String recipientEmailOrPhone) {
        this.recipientEmailOrPhone = recipientEmailOrPhone;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public String getRecipientAccountNumber() {
        return recipientAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public void setRecipientAccountNumber(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
    }

    public String toSearchString() {
        return id + "," + senderId + "," + recipientId + "," + recipientEmailOrPhone +
                "," + recipientType + "," + amount + "," + memo + "," + status +
                "," + initiatedAt + "," + completedAt + "," + cancellationReason +
                "," + senderAccountNumber + "," + recipientAccountNumber;
    }
}
