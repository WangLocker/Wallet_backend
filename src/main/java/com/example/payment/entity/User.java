package com.example.payment.entity;

public class User {
    private Integer id;
    private String name;
    private String ssn;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String hashedPassword) {
        this.password=hashedPassword;
    }
}
