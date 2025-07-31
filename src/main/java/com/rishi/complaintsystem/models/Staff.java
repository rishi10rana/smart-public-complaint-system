package com.rishi.complaintsystem.models;

public class Staff {
    private int staffId;
    private String name;
    private String contact;
    private String is_available;

    public Staff(){}

    public Staff(int staffId, String name, String contact, String is_available){
        this.staffId = staffId;
        this.name = name;
        this.contact = contact;
        this.is_available = is_available;
    }

    // Getter , Setters
    public int getStaffId(){
        return this.staffId;
    }
    public  void setStaffId(int staffId){
        this.staffId = staffId;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getContact(){
        return this.contact;
    }
    public void setContact(String contact){
        this.contact = contact;
    }

    public String getIs_available(){
        return this.is_available;
    }
    public void setIs_available(String is_available){
        this.is_available = is_available;
    }
}
