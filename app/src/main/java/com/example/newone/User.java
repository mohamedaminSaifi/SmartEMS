package com.example.newone;

public class User {
    private int id ;
    private String password;

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public User() {
        this.id = id;
        this.password = password;
    }
}
