package com.snd.app.domain;


import java.util.List;

public class UserDTO {
    private String id;
    private String password;
    private String name;
    private String company;
    private String position;
    private String phone;
    private String email;
    private String authority;
    private List<Integer> inserted;
    private boolean certification;


    // Getter & Setter
    public String getId() {return id;}
    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAuthority() {
        return authority;
    }
    public void setAuthority(String authority) {
        this.authority = authority;
    }
    public List<Integer> getInserted() {
        return inserted;
    }
    public void setInserted(List<Integer> inserted) {
        this.inserted = inserted;
    }
    public boolean isCertification() {
        return certification;
    }
    public void setCertification(boolean certification) {
        this.certification = certification;
    }

}
