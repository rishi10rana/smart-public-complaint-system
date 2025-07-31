package com.rishi.complaintsystem.models;

public class User {
    private int userID;
    private String name;
    private String email;
    private String contact;
    private String role;
    private String password;

    private int totalComplaints;

    public User(){}

    public User(String name, String email, String role, String password, String contact){
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
        this.contact = contact;
    }

    // getters and setter
    public int getUserID(){
        return this.userID;
    }
    public void setUserID(int userID){
        this.userID = userID;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRole(){
        return this.role;
    }
    public void setRole(String role){
        this.role = role;
    }

    public String getPassword(){
        return this.password;
    }
    public void setPassword(String password){
        this.password = password;
    }

    public int getTotalComplaints() {
        return totalComplaints;
    }
    public void setTotalComplaints(int totalComplaints) {
        this.totalComplaints = totalComplaints;
    }
}
