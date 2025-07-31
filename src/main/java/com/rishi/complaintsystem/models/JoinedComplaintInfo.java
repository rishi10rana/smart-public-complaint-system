package com.rishi.complaintsystem.models;

import java.sql.Time;
import java.sql.Timestamp;

public class JoinedComplaintInfo {
    private int complaintID;
    private String userName;
    private String userEmail;
    private String category;
    private String description;
    private String city;
    private String priority;
    private String status;
    private Timestamp dateSubmitted;
    private String staffName;
    private String staffContact;

    public JoinedComplaintInfo(){}

    public JoinedComplaintInfo(int complaintID, String userName, String userEmail, String category, String description, String city, String priority, String status, Timestamp dateSubmitted, String staffName, String staffContact){
        this.complaintID = complaintID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.category = category;
        this.description = description;
        this.city = city;
        this.priority = priority;
        this.status = status;
        this.dateSubmitted = dateSubmitted;
        this.staffName = staffName;
        this.staffContact = staffContact;
    }

    public int getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(int complaintID) {
        this.complaintID = complaintID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Timestamp dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffContact() {
        return staffContact;
    }

    public void setStaffContact(String staffContact) {
        this.staffContact = staffContact;
    }
}
