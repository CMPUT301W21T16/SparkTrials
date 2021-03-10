package com.example.sparktrials.models;

public class Profile {
    String userID;
    String username;
    String contact;

    public Profile(){
        this.userID = null;
        this.username = null;
        this.contact = null;
    }

    public Profile(String userID){
        this.userID = userID;
        this.username = "user"+userID;
    }

    public String getId() {
        return userID;
    }

    public void setId(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
