package com.example.newone;

public class item {
    private Long id;
    private String status;
    private String name;
    private String lastSeen;

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setNumber(long id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getNumber() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public item(long id, String status, String name ,String lastSeen) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.lastSeen = lastSeen;
    }
}
