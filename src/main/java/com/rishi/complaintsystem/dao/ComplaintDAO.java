package com.rishi.complaintsystem.dao;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.rishi.complaintsystem.db.DBConnection;
import com.rishi.complaintsystem.models.Complaint;
import com.rishi.complaintsystem.models.JoinedComplaintInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {
//    public static boolean insertComplaint(Complaint complaint){
//        // query to insert the data in complaints table
//        String query = "INSERT INTO complaints(user_id, category, description, priority, status) VALUES(?,?,?,?,?,?)";
//
//        Connection con;
//        PreparedStatement ps;
//        try{
//            // first establish a connection to db
//            con = DBConnection.createConnection();
//            ps = con.prepareStatement(query);
//
//            // set placeholder values
//            ps.setInt(1,complaint.getUserId());
//            ps.setString(2,complaint.getCategory());
//            ps.setString(3,complaint.getDescription());
//            ps.setString(4,complaint.getPriority());
//            ps.setString(5, complaint.getStatus());
//
//            // execute the query as it is a update query
//            int affectedRows = ps.executeUpdate();
//            return affectedRows > 0;
//        } catch (ClassNotFoundException | SQLException e) {
//            System.out.println("Error inserting complaint: ");
//            e.printStackTrace();
//            return false;
//        }
//    }

    // it helps to get all the complaints from the database
//    public List<String[]> getAllcomplaints(){
//        List<String[]> complaintsList = new ArrayList<>();
//
//        try{
//            Connection con = DBConnection.createConnection();
//            String query = "SELECT complaint_id, user_id, category, description, city, priority, status, date_submitted FROM complaints ORDER BY date_submitted DESC";
//            PreparedStatement ps = con.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//
//            while(rs.next()){
//                String[] complaint = new String[8];
//                complaint[0] = String.valueOf(rs.getInt("complaint_id"));
//                complaint[1] = String.valueOf(rs.getInt("user_id"));
//                complaint[2] = rs.getString("category");
//                complaint[3] = rs.getString("description");
//                complaint[4] = rs.getString("city");
//                complaint[5] = rs.getString("priority");
//                complaint[6] = rs.getString("status");
//                complaint[7] = rs.getString("date_submitted");
//                complaintsList.add(complaint);
//            }
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return complaintsList;
//    }

    public List<JoinedComplaintInfo> getAllcomplaints(){
        List<JoinedComplaintInfo> complaintsList = new ArrayList<>();

        try{
            Connection con = DBConnection.createConnection();
            String query = "SELECT c.complaint_id, u.name AS user_name, u.email AS user_email, " +
                           "c.category, c.description, c.city , c.priority, c.status, c.date_submitted, " +
                           "s.name AS staff_name, s.contact AS staff_contact " +
                           "FROM complaints c " +
                           "JOIN users u ON c.user_id = u.user_id " +
                           "LEFT JOIN assigned_complaints ac ON c.complaint_id = ac.complaint_id " +
                           "LEFT JOIN staffs s ON ac.staff_id = s.staff_id";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                JoinedComplaintInfo info = new JoinedComplaintInfo();
                info.setComplaintID(rs.getInt("complaint_id"));
                info.setUserName(rs.getString("user_name"));
                info.setUserEmail(rs.getString("user_email"));
                info.setCategory(rs.getString("category"));
                info.setDescription(rs.getString("description"));
                info.setCity(rs.getString("city"));
                info.setPriority(rs.getString("priority"));
                info.setStatus(rs.getString("status"));
                info.setDateSubmitted(rs.getTimestamp("date_submitted"));
                info.setStaffName(rs.getString("staff_name"));
                info.setStaffContact(rs.getString("staff_contact"));
                complaintsList.add(info);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return complaintsList;
    }

    public List<JoinedComplaintInfo> getUserComplaintDetailsWithStaff(int userId){
        List<JoinedComplaintInfo> complaintsList = new ArrayList<>();

        try{
            Connection con = DBConnection.createConnection();
            String query = "SELECT c.complaint_id, u.name AS user_name, u.email AS user_email, " +
                    "c.category, c.description, c.city , c.priority, c.status, c.date_submitted, " +
                    "s.name AS staff_name, s.contact AS staff_contact " +
                    "FROM complaints c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "LEFT JOIN assigned_complaints ac ON c.complaint_id = ac.complaint_id " +
                    "LEFT JOIN staffs s ON ac.staff_id = s.staff_id " +
                    "WHERE c.user_id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                JoinedComplaintInfo info = new JoinedComplaintInfo();
                info.setComplaintID(rs.getInt("complaint_id"));
                info.setUserName(rs.getString("user_name"));
                info.setUserEmail(rs.getString("user_email"));
                info.setCategory(rs.getString("category"));
                info.setDescription(rs.getString("description"));
                info.setCity(rs.getString("city"));
                info.setPriority(rs.getString("priority"));
                info.setStatus(rs.getString("status"));
                info.setDateSubmitted(rs.getTimestamp("date_submitted"));
                info.setStaffName(rs.getString("staff_name"));
                info.setStaffContact(rs.getString("staff_contact"));
                complaintsList.add(info);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return complaintsList;
    }

    public List<JoinedComplaintInfo> getAllAssignedComplaints(){
        List<JoinedComplaintInfo> complaintsList = new ArrayList<>();

        try{
            Connection con = DBConnection.createConnection();
            String query = "SELECT c.complaint_id, u.name AS user_name, u.email AS user_email, " +
                    "c.category, c.description, c.city , c.priority, c.status, c.date_submitted, " +
                    "s.name AS staff_name, s.contact AS staff_contact " +
                    "FROM complaints c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "JOIN assigned_complaints ac ON c.complaint_id = ac.complaint_id " +
                    "JOIN staffs s ON ac.staff_id = s.staff_id";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                JoinedComplaintInfo info = new JoinedComplaintInfo();
                info.setComplaintID(rs.getInt("complaint_id"));
                info.setUserName(rs.getString("user_name"));
                info.setUserEmail(rs.getString("user_email"));
                info.setCategory(rs.getString("category"));
                info.setDescription(rs.getString("description"));
                info.setCity(rs.getString("city"));
                info.setPriority(rs.getString("priority"));
                info.setStatus(rs.getString("status"));
                info.setDateSubmitted(rs.getTimestamp("date_submitted"));
                info.setStaffName(rs.getString("staff_name"));
                info.setStaffContact(rs.getString("staff_contact"));
                complaintsList.add(info);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return complaintsList;
    }

    public List<JoinedComplaintInfo> getAllUnAssignedComplaints(){
        List<JoinedComplaintInfo> complaintsList = new ArrayList<>();

        try{
            Connection con = DBConnection.createConnection();
            String query = "SELECT c.complaint_id, u.name AS user_name, u.email AS user_email, " +
                    "c.category, c.description, c.city , c.priority, c.status, c.date_submitted " +
                    "FROM complaints c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "WHERE c.complaint_id NOT IN (SELECT complaint_id FROM assigned_complaints)";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                JoinedComplaintInfo info = new JoinedComplaintInfo();
                info.setComplaintID(rs.getInt("complaint_id"));
                info.setUserName(rs.getString("user_name"));
                info.setUserEmail(rs.getString("user_email"));
                info.setCategory(rs.getString("category"));
                info.setDescription(rs.getString("description"));
                info.setCity(rs.getString("city"));
                info.setPriority(rs.getString("priority"));
                info.setStatus(rs.getString("status"));
                info.setDateSubmitted(rs.getTimestamp("date_submitted"));
                complaintsList.add(info);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return complaintsList;
    }

    public List<Complaint> getAllNotResolvedComplaints(){
        List<Complaint> complaintsList = new ArrayList<>();

        try{
            Connection con = DBConnection.createConnection();
            String query = "SELECT complaint_id, user_id ,category, description, city, status, priority, status, date_submitted " +
                    "FROM complaints WHERE status != 'Resolved'";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Complaint c = new Complaint();
                c.setId(rs.getInt("complaint_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setCategory(rs.getString("category"));
                c.setDescription(rs.getString("description"));
                c.setCity(rs.getString("city"));
                c.setPriority(rs.getString("priority"));
                c.setStatus(rs.getString("status"));
                c.setDate_submitted(rs.getTimestamp("date_submitted"));
                complaintsList.add(c);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return complaintsList;
    }

    public List<String[]> getComplaintsByUserId(int userId){
        List<String[]> complaintsList = new ArrayList<>();

        try{
            Connection con = DBConnection.createConnection();
            String query = "SELECT complaint_id, user_id, category, description, priority, status, date_submitted FROM complaints WHERE user_id = ? ORDER BY date_submitted DESC";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String[] complaint = new String[7];
                complaint[0] = String.valueOf(rs.getInt("complaint_id"));
                complaint[1] = String.valueOf(rs.getInt("user_id"));
                complaint[2] = rs.getString("category");
                complaint[3] = rs.getString("description");
                complaint[4] = rs.getString("priority");
                complaint[5] = rs.getString("status");
                complaint[6] = rs.getString("date_submitted");
                complaintsList.add(complaint);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return complaintsList;
    }

    public List<JoinedComplaintInfo> getComplaintByStatus(String status){
        List<JoinedComplaintInfo> complaintsList = new ArrayList<>();

        try{
            Connection con = DBConnection.createConnection();
            String query = "SELECT c.complaint_id, u.name AS user_name, u.email AS user_email, " +
                    "c.category, c.description, c.city , c.priority, c.status, c.date_submitted, " +
                    "s.name AS staff_name, s.contact AS staff_contact " +
                    "FROM complaints c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "LEFT JOIN assigned_complaints ac ON c.complaint_id = ac.complaint_id " +
                    "LEFT JOIN staffs s ON ac.staff_id = s.staff_id WHERE status = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,status);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                JoinedComplaintInfo info = new JoinedComplaintInfo();
                info.setComplaintID(rs.getInt("complaint_id"));
                info.setUserName(rs.getString("user_name"));
                info.setUserEmail(rs.getString("user_email"));
                info.setCategory(rs.getString("category"));
                info.setDescription(rs.getString("description"));
                info.setCity(rs.getString("city"));
                info.setPriority(rs.getString("priority"));
                info.setStatus(rs.getString("status"));
                info.setDateSubmitted(rs.getTimestamp("date_submitted"));
                info.setStaffName(rs.getString("staff_name"));
                info.setStaffContact(rs.getString("staff_contact"));
                complaintsList.add(info);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return complaintsList;
    }

    public List<JoinedComplaintInfo> getComplaintByPriority(String priority){
        List<JoinedComplaintInfo> complaintsList = new ArrayList<>();

        try{
            Connection con = DBConnection.createConnection();
            String query = "SELECT c.complaint_id, u.name AS user_name, u.email AS user_email, " +
                    "c.category, c.description, c.city , c.priority, c.status, c.date_submitted, " +
                    "s.name AS staff_name, s.contact AS staff_contact " +
                    "FROM complaints c " +
                    "JOIN users u ON c.user_id = u.user_id " +
                    "LEFT JOIN assigned_complaints ac ON c.complaint_id = ac.complaint_id " +
                    "LEFT JOIN staffs s ON ac.staff_id = s.staff_id WHERE priority = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,priority);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                JoinedComplaintInfo info = new JoinedComplaintInfo();
                info.setComplaintID(rs.getInt("complaint_id"));
                info.setUserName(rs.getString("user_name"));
                info.setUserEmail(rs.getString("user_email"));
                info.setCategory(rs.getString("category"));
                info.setDescription(rs.getString("description"));
                info.setCity(rs.getString("city"));
                info.setPriority(rs.getString("priority"));
                info.setStatus(rs.getString("status"));
                info.setDateSubmitted(rs.getTimestamp("date_submitted"));
                info.setStaffName(rs.getString("staff_name"));
                info.setStaffContact(rs.getString("staff_contact"));
                complaintsList.add(info);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return complaintsList;
    }

    public boolean submitComplaint(int userId, String category, String description, String city, String priority){
        String query = "INSERT INTO complaints(user_id, category, description,city, priority, status) VALUES(?,?,?,?,?, 'Pending')";

        try{
            Connection con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,userId);
            ps.setString(2,category);
            ps.setString(3,description);
            ps.setString(4,city);
            ps.setString(5,priority);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateComplaintStatus(int complaintID, String newStatus){
        String query = "UPDATE complaints SET status = ? WHERE complaint_id = ?";
        try{
            Connection con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, newStatus);
            ps.setInt(2, complaintID);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0; // return true if record updated
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Integer> getUnassignedComplaintsID(){
        List<Integer> complaintIds = new ArrayList<>();
        String query = "SELECT complaint_id FROM complaints WHERE complaint_id NOT IN (SELECT complaint_id FROM assigned_complaints)";

        try{
            Connection connection = DBConnection.createConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                complaintIds.add(rs.getInt("complaint_id"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return complaintIds;
    }

    public List<Complaint> getUnassignedComplaintsData(){
        List<Complaint> complaintsData = new ArrayList<>();
        String query = "SELECT * FROM complaints WHERE complaint_id NOT IN (SELECT complaint_id FROM assigned_complaints)";

        try{
            Connection connection = DBConnection.createConnection();
            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Complaint c = new Complaint();
                c.setId(rs.getInt("complaint_id"));
                c.setCategory(rs.getString("category"));
                c.setCity(rs.getString("city"));
                c.setPriority(rs.getString("priority"));
                c.setStatus(rs.getString("status"));
                complaintsData.add(c);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return complaintsData;
    }

    public String getCityFromComplaintId(int complaintId){
        String query = "SELECT city FROM complaints WHERE complaint_id = ?";

        try{
            Connection con = DBConnection.createConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1,complaintId);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getString("city");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
