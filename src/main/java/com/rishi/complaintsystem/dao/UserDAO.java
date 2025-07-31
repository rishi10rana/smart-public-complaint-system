package com.rishi.complaintsystem.dao;

import com.rishi.complaintsystem.db.DBConnection;
import com.rishi.complaintsystem.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    Connection conenction;

    public UserDAO() throws SQLException, ClassNotFoundException {
        this.conenction = DBConnection.createConnection();
    }

    public boolean registerUser(User user){
        // query to insert a new user
        String query = "INSERT INTO users(name ,email, role, password, contact) VALUES(?,?,?,?,?)";

        try{
            PreparedStatement ps = conenction.prepareStatement(query);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getPassword());
            ps.setString(5,user.getContact());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // returns true if record inserted successfully
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User loginUser(String email, String password){
        String query = "SELECT * FROM users where email = ? AND password = ?";

        try{
            PreparedStatement ps = conenction.prepareStatement(query);
            ps.setString(1,email);
            ps.setString(2,password);

            ResultSet result = ps.executeQuery();
            if(result.next()){
                User user = new User();
                user.setUserID(result.getInt("user_id"));
                user.setName(result.getString("name"));
                user.setEmail(email);
                user.setContact(result.getString("contact"));
                user.setRole(result.getString("role"));
                user.setPassword(password);

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // This function gets all the users information from the database
    public List<User> getAllUsers(){
        List<User> usersList = new ArrayList<>();

        String query = "SELECT u.user_id, u.name , u.email, u.contact, u.role, COUNT(c.complaint_id) AS total_complaints FROM users u " +
                "LEFT JOIN complaints c ON u.user_id = c.user_id GROUP BY u.user_id";
        try{
//            Connection con = DBConnection.createConnection();
            PreparedStatement ps = conenction.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User u = new User();
                u.setUserID(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setContact(rs.getString("contact"));
                u.setRole(rs.getString("role"));
                u.setTotalComplaints(rs.getInt("total_complaints"));
                usersList.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public List<User> getAllUsersFilterByName(String userName){
        List<User> usersList = new ArrayList<>();

        String query = "SELECT u.user_id, u.name , u.email, u.contact, u.role, COUNT(c.complaint_id) AS total_complaints FROM users u " +
                "LEFT JOIN complaints c ON u.user_id = c.user_id where u.name LIKE ? GROUP BY u.user_id";
        try{
//            Connection con = DBConnection.createConnection();
            PreparedStatement ps = conenction.prepareStatement(query);
            ps.setString(1,"%" + userName + "%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User u = new User();
                u.setUserID(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setContact(rs.getString("contact"));
                u.setRole(rs.getString("role"));
                u.setTotalComplaints(rs.getInt("total_complaints"));
                usersList.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public List<User> getAllUsersFilterByEmail(String userEmail){
        List<User> usersList = new ArrayList<>();

        String query = "SELECT u.user_id, u.name , u.email, u.contact, u.role, COUNT(c.complaint_id) AS total_complaints FROM users u " +
                "LEFT JOIN complaints c ON u.user_id = c.user_id where u.email LIKE ? GROUP BY u.user_id";
        try{
//            Connection con = DBConnection.createConnection();
            PreparedStatement ps = conenction.prepareStatement(query);
            ps.setString(1,"%" + userEmail + "%");
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                User u = new User();
                u.setUserID(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setContact(rs.getString("contact"));
                u.setRole(rs.getString("role"));
                u.setTotalComplaints(rs.getInt("total_complaints"));
                usersList.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public List<String[]> getUsersByRole(String role){
        List<String[]> users = new ArrayList<>();

        String query = "SELECT * FROM users WHERE role = ?";
        try{
//            Connection con = DBConnection.createConnection();
            PreparedStatement ps = conenction.prepareStatement(query);
            ps.setString(1,role);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                String[] user = new String[4];
                user[0] = String.valueOf(rs.getInt("user_id"));
                user[1] = rs.getString("name");
                user[2] = rs.getString("email");
                user[3] = rs.getString("role");
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // methods to delete a user from users table in db
    public boolean deleteUser(int userID){
        String query = "DELETE FROM users WHERE user_id = ?";
        try{
            PreparedStatement ps = conenction.prepareStatement(query);
            ps.setInt(1,userID);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
