package com.rishi.complaintsystem.db;

import com.rishi.complaintsystem.utils.ConfigReader;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection createConnection() throws SQLException, ClassNotFoundException {
        // First Load JDBC Driver
        Class.forName(ConfigReader.getValue("DRIVER"));

        // making a connection with database
        String URL = ConfigReader.getValue("CONNECTION_URL");
        String USER = ConfigReader.getValue("USERID");
        String PASSWORD = ConfigReader.getValue("PASSWORD");
        Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

        if(con!=null){
            System.out.println("Connection to Database established successfully.");
        }else{
            System.out.println("Connection not Created");
        }
        return con;
    }
}
