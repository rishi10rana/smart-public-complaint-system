package com.rishi.complaintsystem.gui;

import com.rishi.complaintsystem.dao.UserDAO;
import com.rishi.complaintsystem.db.DBConnection;
import com.rishi.complaintsystem.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {

    private JButton registerButton;

    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;

    private JPanel jPanel1;
    private JPanel jPanel2;

    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private ButtonGroup roleGroup;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField contactField;
    private JPasswordField passwordField;

    private String ROLE;

    public RegisterFrame(){
        // JFarme Properties
        setTitle("Smart Complaint System - Register");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Main GUi
        jPanel1 = new JPanel();
        jPanel1.setLayout(null);

        jPanel2 = new JPanel();
        jPanel2.setBackground(new Color(51,45,86));
        jPanel2.setLayout(null);

        jLabel1 = new JLabel();
        jLabel1.setFont(new Font("Consolas", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Smart Complaint System");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(420, 110, 330, 29);

        jLabel2 = new JLabel();
        jLabel2.setFont(new Font("Consolas", 0, 18)); // NOI18N
        jLabel2.setText("Registration Frame");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(254, 6, 200, 30);

        jLabel7 = new JLabel();
        jLabel7.setText("Name:");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(126, 88, 63, 16);

        nameField = new JTextField();
        nameField.setText("");
        jPanel2.add(nameField);
        nameField.setBounds(201, 80, 344, 33);
        nameField.putClientProperty("JTextField.placeholderText","Enter name");

        jLabel3 = new JLabel();
        jLabel3.setText("Email:");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(126, 137, 63, 16);

        jLabel4 = new JLabel();
        jLabel4.setText("Password:");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(130, 280, 63, 20);

        emailField = new JTextField();
        emailField.setText("");
        jPanel2.add(emailField);
        emailField.setBounds(201, 129, 344, 33);
        emailField.putClientProperty("JTextField.placeholderText","Enter email");

        passwordField = new JPasswordField();
        passwordField.setText("");
        jPanel2.add(passwordField);
        passwordField.setBounds(200, 270, 344, 33);
        passwordField.putClientProperty("JTextField.placeholderText","Enter password");

        jLabel9 = new JLabel();
        jLabel9.setText("Contact:");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(120, 180, 43, 16);

        contactField = new JTextField();
        contactField.setText("");
        contactField.putClientProperty("JTextField.placeholderText","Enter contact");
        jPanel2.add(contactField);
        contactField.setBounds(200, 183, 345, 37);

        jLabel8 = new JLabel();
        jLabel8.setText("Role:");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(130, 230, 37, 16);

        jRadioButton1 = new JRadioButton();
        jRadioButton1.setText("user");
        jPanel2.add(jRadioButton1);
        jRadioButton1.setBounds(200, 230, 98, 21);
        jRadioButton1.addActionListener(new getSelectedRole());

        jRadioButton2 = new JRadioButton();
        jRadioButton2.setText("admin");
        jPanel2.add(jRadioButton2);
        jRadioButton2.setBounds(300, 230, 98, 21);
        jRadioButton2.addActionListener(new getSelectedRole());

        roleGroup = new ButtonGroup();
        roleGroup.add(jRadioButton1);
        roleGroup.add(jRadioButton2);

        registerButton = new JButton();
        registerButton.setText("Register");
        jPanel2.add(registerButton);
        registerButton.setBounds(300, 330, 109, 33);
        registerButton.addActionListener(new handleRegister());

        jLabel5 = new JLabel();
        jLabel5.setText("Already have an account ?");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(248, 384, 138, 16);

        jLabel6 = new JLabel();
        jLabel6.setFont(new Font("Arial", Font.BOLD, 12)); // NOI18N
        jLabel6.setForeground(new Color(0, 102, 204));
        jLabel6.setText("Login Here");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(392, 384, 78, 16);
        jLabel6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabel6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginFrame();
                dispose();
            }
        });

        jPanel1.add(jPanel2);
        jPanel2.setBounds(230, 140, 720, 440);

        add(jPanel1, BorderLayout.CENTER);
        setVisible(true);
    }

    private class handleRegister implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            try{
//                Connection con = DBConnection.createConnection();
//                UserDAO userDAO = new UserDAO(con);

                UserDAO userDAO = new UserDAO();

                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String contact = contactField.getText().trim();
                String role = ROLE.trim();
                String password = new String(passwordField.getPassword()).trim();

                if(name.isEmpty() || email.isEmpty() || role.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please fill all the fields!","Missing Input",JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = new User(name, email, role, password, contact);
                if(userDAO.registerUser(user)){
                    JOptionPane.showMessageDialog(null, "Registered Successfully!");
                    dispose();
                    new LoginFrame();
                }else{
                    JOptionPane.showMessageDialog(null, "Registration Failed!");
                }
            }catch(ClassNotFoundException | SQLException ex){
                ex.printStackTrace();
            }
        }
    }


    private class getSelectedRole implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==jRadioButton1){
                ROLE = jRadioButton1.getText();
            }
            if(e.getSource()==jRadioButton2){
                ROLE = jRadioButton2.getText();
            }
        }
    }
}
