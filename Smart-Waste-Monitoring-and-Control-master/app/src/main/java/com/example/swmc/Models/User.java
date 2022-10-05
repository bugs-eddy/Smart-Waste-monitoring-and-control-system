package com.example.swmc.Models;

public class User {
    private String id;
    private int userType;
    private String userName;
    private String password;

    public User() {
    }

    public User(String id, int userType, String userName, String password) {
        this.id = id;
        this.userType = userType;
        this.userName = userName;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
