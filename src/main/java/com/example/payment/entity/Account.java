package com.example.payment.entity;

public class Account {
    private Integer id;
    private Integer userId;
    private String bankId;
    private String accountNumber;
    private Boolean isVerified;
    private Boolean isPrimary;
    private Double money;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getBankId() {
        return bankId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
