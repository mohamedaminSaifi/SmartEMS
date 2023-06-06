package com.example.newone;

public class item {
    private int number;
    private String status;

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public item(int number, String status) {
        this.number = number;
        this.status = status;
    }
}
