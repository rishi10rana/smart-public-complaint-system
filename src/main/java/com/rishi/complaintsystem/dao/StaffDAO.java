package com.rishi.complaintsystem.dao;

import com.rishi.complaintsystem.db.DBConnection;
import com.rishi.complaintsystem.models.Staff;

import java.security.DrbgParameters;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    private Connection connection;

    public StaffDAO(){
        try {
            this.connection = DBConnection.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Staff> getAllAvailableStaff(){
        List<Staff> staffsList = new ArrayList<>();

        String query = "SELECT * FROM staffs where is_available = TRUE";
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Staff s = new Staff();
                s.setStaffId(rs.getInt("staff_id"));
                s.setName(rs.getString("name"));
                s.setContact(rs.getString("contact"));
                s.setIs_available(rs.getString("is_available"));
                staffsList.add(s);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return staffsList;
    }

    public boolean updateStaffAvailability(int staffID, boolean isAvailable){
        String query = "UPDATE staffs SET is_available = ? WHERE staff_id = ?";

        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setBoolean(1,isAvailable);
            ps.setInt(2,staffID);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> getAllStaffIds(){
        List<Integer> staffIds = new ArrayList<>();
        String query =  "SELECT staff_id FROM staffs";

        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                staffIds.add(rs.getInt("staff_id"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return staffIds;
    }
}
