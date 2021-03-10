package com.example.sparktrials.models;

public class Profile {
    Integer id;
    String username;
    String contact;

    public Profile(){
        this.id = null;
        this.username = null;
        this.contact = null;
    }

    public Profile(Integer id){
        this.id = id;
        this.username = "user"+id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
