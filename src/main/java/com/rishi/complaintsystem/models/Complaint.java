package com.rishi.complaintsystem.models;

import java.sql.Timestamp;

public class Complaint {
    private int id;
    private int userId;
    private String category;
    private String description;
    private String city;
    private String priority;
    private String status;
    private Timestamp date_submitted;

    //Constructor
    public Complaint(){}

    public Complaint(int id,int userId, String category,String description,String city, String priority,String status,Timestamp date_submitted){
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.description = description;
        this.city = city;
        this.priority = priority;
        this.status = status;
        this.date_submitted = date_submitted;
    }

    // Getter and Setter
    public int getId() {
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }

    public int getUserId(){
        return this.userId;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getCategory(){
        return this.category;
    }
    public void setCategory(String category){
        this.category = category;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPriority(){
        return this.priority;
    }
    public void setPriority(String priority){
        this.priority = priority;
    }

    public String getStatus(){
        return this.status;
    }
    public void setStatus(String status){
        this.status = status;
    }

    public Timestamp getDate_submitted() {
        return date_submitted;
    }

    public void setDate_submitted(Timestamp date_submitted) {
        this.date_submitted = date_submitted;
    }
}
