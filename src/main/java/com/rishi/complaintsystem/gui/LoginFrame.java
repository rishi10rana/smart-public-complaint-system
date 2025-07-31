package com.rishi.complaintsystem.gui;

import com.rishi.complaintsystem.dao.UserDAO;
import com.rishi.complaintsystem.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private JButton loginButton;

    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;

    private JPanel jPanel1;
    private JPanel jPanel2;

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame(){
        // JFarme Properties
        setTitle("Smart Complaint System - Login");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Main GUi
        jPanel1 = new JPanel();
        jPanel1.setLayout(null);

        jPanel2 = new JPanel();
        jPanel2.setBackground(new Color(30,30,30));
        jPanel2.setLayout(null);

        jLabel1 = new JLabel();
        jLabel1.setFont(new Font("Consolas", Font.PLAIN, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Smart Complaint System");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(420, 160, 330, 29);

        jLabel2 = new JLabel();
        jLabel2.setFont(new Font("Consolas", Font.PLAIN, 18)); // NOI18N
        jLabel2.setText("Login Frame");
        jPanel2.add(jLabel2);
        jPanel2.setBackground(new Color(51,45,86));
        jLabel2.setBounds(297, 6, 124, 30);

        jLabel3 = new JLabel();
        jLabel3.setText("Email:");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(126, 97, 63, 16);

        jLabel4 = new JLabel();
        jLabel4.setText("Password:");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(126, 148, 63, 16);

        emailField = new JTextField();

        emailField.setText("");
        jPanel2.add(emailField);
        emailField.setBounds(201, 89, 344, 33);
        emailField.putClientProperty("JTextField.placeholderText","Enter email");

        passwordField = new JPasswordField();
        passwordField.setText("");
        jPanel2.add(passwordField);
        passwordField.setBounds(201, 140, 344, 33);
        passwordField.putClientProperty("JTextField.placeholderText","Enter password");

        loginButton = new JButton();
        loginButton.setText("Login");
        jPanel2.add(loginButton);
        loginButton.setBounds(304, 221, 109, 33);
        loginButton.addActionListener(new HandleLogin());

        jLabel5 = new JLabel();

        jLabel5.setText("Are you a New User ?");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(250, 321, 113, 16);

        jLabel6 = new JLabel();
        jLabel6.setFont(new Font("Arial", Font.BOLD, 12)); // NOI18N
        jLabel6.setForeground(new Color(0, 102, 204));
        jLabel6.setText("Register Here");
        jLabel6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jPanel2.add(jLabel6);
        jLabel6.setBounds(375, 321, 80, 16);
        jLabel6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame();
                dispose();
            }
        });

        jPanel1.add(jPanel2);
        jPanel2.setBounds(230, 200, 751, 359);

        add(jPanel1, BorderLayout.CENTER);
        setVisible(true);
    }

    private class HandleLogin implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
//                Connection con = DBConnection.createConnection();
//                UserDAO userDAO = new UserDAO(con);
                UserDAO userDAO = new UserDAO();

                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if(email.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields!","Missing Input",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = userDAO.loginUser(email, password);
                if(user!=null){
                    JOptionPane.showMessageDialog(null,"Welcome " + user.getName());
                    dispose();
                    if(user.getRole().equals("admin")){
                        // open admin dashboard
                        new AdminDashboard(user);
                        dispose();
                    }
                    else{
                        // open user dashboard
                        new UserDashboard(user);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Invalid Credentials");
                }
            }catch (ClassNotFoundException | SQLException ex){
                ex.printStackTrace();
            }
        }
    }
}
