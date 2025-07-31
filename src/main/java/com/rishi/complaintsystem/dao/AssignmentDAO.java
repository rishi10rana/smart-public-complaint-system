package com.rishi.complaintsystem.dao;

import com.rishi.complaintsystem.db.DBConnection;
import com.rishi.complaintsystem.models.Assignment;

import java.awt.*;
import java.awt.geom.RectangularShape;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class AssignmentDAO {
    Connection connection;

    public AssignmentDAO(){
        try {
            this.connection = DBConnection.createConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean assignStaffToComplaint(int complaintId, int staffId){
        String query = "INSERT INTO assigned_complaints(complaint_id , staff_id) VALUES(?,?)";

        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,complaintId);
            ps.setInt(2,staffId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Assignment> getAllAssignedComplaints(){
        List<Assignment> assignmentList = new ArrayList<>();
        String query = "SELECT a.assignment_id, a.complaint_id, a.staff_id, s.name AS staff_name,  a.assigned_date FROM assigned_complaints a LEFT JOIN staffs s ON a.staff_id = s.staff_id";

        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Assignment a = new Assignment();
                a.setAssignmentId(rs.getInt("assignment_id"));
                a.setComplaintId(rs.getInt("complaint_id"));
                a.setStaffId(rs.getInt("staff_id"));
                a.setStaffName(rs.getString("staff_name"));
                a.setAssignedDate(rs.getTimestamp("assigned_date"));
                assignmentList.add(a);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return assignmentList;
    }
}
