package com.rishi.complaintsystem.models;

import java.sql.Timestamp;

public class Assignment {
    private int assignmentId;
    private int complaintId;
    private int staffId;
    private Timestamp assignedDate;

    // Extra Data Members
    private String staffName;

    public Assignment(){}

    public Assignment(int assignmentId, int complaintId, int staffId, Timestamp assignedDate){
        this.assignmentId = assignmentId;
        this.complaintId = complaintId;
        this.staffId = staffId;
        this.assignedDate = assignedDate;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public Timestamp getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Timestamp assignedDate) {
        this.assignedDate = assignedDate;
    }

    public String getStaffName() {
        return staffName;
    }
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
}
