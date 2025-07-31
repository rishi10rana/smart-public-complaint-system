package com.rishi.complaintsystem.gui;

import com.rishi.complaintsystem.ai.OpenWeatherAPI;
import com.rishi.complaintsystem.dao.AssignmentDAO;
import com.rishi.complaintsystem.dao.ComplaintDAO;
import com.rishi.complaintsystem.dao.StaffDAO;
import com.rishi.complaintsystem.dao.UserDAO;
import com.rishi.complaintsystem.models.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

public class AdminDashboard extends JFrame {
    User user;
    ComplaintDAO complaintDAO;
    UserDAO userDAO;
    StaffDAO staffDAO;

    private JTabbedPane jTabbedPane;
    private JPanel topPanel;

    private JLabel jLabel1;


    // Tables for Different Tabs
    JTable viewComplaintsTable; // "View Complaints Tab"
    JTable manageComplaintsTable; // "Manage Complaints Tab"
    JTable userTable; //"User Management Tab"
    JTable unassignedComplaintsTable;
    JTable availableStaffTable;
    JTable assignedComplaintTable;

    public AdminDashboard(User user){
        // JFrame Properties
        setTitle("Smart Complaint System - Admin Dashboard");
        setSize(1400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Initialize received objects
        complaintDAO = new ComplaintDAO();
        try {
            userDAO = new UserDAO();
            staffDAO = new StaffDAO();
        } catch (Exception e){
            e.printStackTrace();
        }

        topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setPreferredSize(new java.awt.Dimension(1200, 60));
        topPanel.setBackground(new Color(51,45 ,86));


        jTabbedPane = new JTabbedPane();

        jLabel1 = new JLabel();
        jLabel1.setFont(new Font("Consolas", 0, 20)); // NOI18N
        jLabel1.setText("Admin Dashboard");
        jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
        topPanel.add(jLabel1);
        jLabel1.setBounds(31, 19, 188, 24);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(1270, 16, 73, 23);
        topPanel.add(logoutButton);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame();
            }
        });

        // Adding Different Tabs
        setupWelcomeTab();
        setupViewComplaintsTab();
        setupManageComplaintsTab();
        setupUserManagementTab();
        setupAssignComplaintsTab();

        add(topPanel,BorderLayout.NORTH);
        add(jTabbedPane,BorderLayout.CENTER);
        setVisible(true);
    }

    // ------------------------------  Different Tabs created for Admin Dashboard -----------------------

    private void setupWelcomeTab(){
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Padding
        welcomePanel.setBackground(new Color(25, 25, 25)); // Dark background

        // Header Label
        JLabel welcomeLabel = new JLabel("Welcome, Admin!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Consolas", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);

        // Welcome TextArea (inside scroll pane)
        JTextArea infoArea = new JTextArea(
                "Welcome to Admin Dashboard!\n\n" +
                        "Here’s what you can do:\n" +
                        "• View all complaints submitted by users\n" +
                        "• Filter complaints by priority, status, or keywords\n" +
                        "• Assign staff or mark complaints as resolved (future)\n" +
                        "• View user details (future)\n" +
                        "• Logout anytime from below"
        );
        infoArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        infoArea.setBackground(new Color(30, 30, 30));
        infoArea.setForeground(Color.WHITE);
        infoArea.setCaretColor(Color.WHITE);
        infoArea.setEditable(false);
        infoArea.setFocusable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        welcomePanel.add(scrollPane, BorderLayout.CENTER);

        jTabbedPane.addTab("Welcome", welcomePanel);
    }

    private void setupViewComplaintsTab(){
        JPanel complaintsPanel = new JPanel(new BorderLayout(10, 10));
        complaintsPanel.setBackground(new Color(30, 30, 30));
        complaintsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------------------- TOP Panel ----------------------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("View All Complaints");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.setIcon(new ImageIcon("src/main/java/com/rishi/complaintsystem/images/refresh.png"));
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBackground(new Color(50, 50, 50));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.addActionListener(e -> loadAllComplaintsTable(viewComplaintsTable));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(refreshBtn, BorderLayout.EAST);
        complaintsPanel.add(topPanel, BorderLayout.NORTH);

        // ---------------------- FILTER Panel ----------------------
        JPanel filterPanel = new JPanel(new GridLayout(1, 3, 20, 10));
        filterPanel.setBackground(new Color(30, 30, 30));

        // Status Filter
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(new Color(30, 30, 30));
        JLabel statusLabel = new JLabel("Filter by Status:");
        statusLabel.setForeground(Color.LIGHT_GRAY);
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"All", "Pending", "In Progress", "Resolved"});
        statusBox.addActionListener(e -> {
            String selected = (String) statusBox.getSelectedItem();
            if ("All".equals(selected)) loadAllComplaintsTable(viewComplaintsTable);
            else loadComplaintsByStatus(viewComplaintsTable, selected);
        });
        statusPanel.add(statusLabel);
        statusPanel.add(statusBox);

        // Priority Filter
        JPanel priorityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        priorityPanel.setBackground(new Color(30, 30, 30));
        JLabel priorityLabel = new JLabel("Filter by Priority:");
        priorityLabel.setForeground(Color.LIGHT_GRAY);
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"All", "Low", "Normal", "Urgent"});
        priorityBox.addActionListener(e -> {
            String selected = (String) priorityBox.getSelectedItem();
            if ("All".equals(selected)) loadAllComplaintsTable(viewComplaintsTable);
            else loadComplaintsByPriority(viewComplaintsTable, selected);
        });
        priorityPanel.add(priorityLabel);
        priorityPanel.add(priorityBox);

        // Assignment Filter
        JPanel assignPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        assignPanel.setBackground(new Color(30, 30, 30));
        JLabel assignLabel = new JLabel("Filter by Assignment:");
        assignLabel.setForeground(Color.LIGHT_GRAY);
        JComboBox<String> assignBox = new JComboBox<>(new String[]{"All", "Assigned", "Not Assigned"});
        assignBox.addActionListener(e -> {
            String selected = (String) assignBox.getSelectedItem();
            if ("All".equals(selected)) loadAllComplaintsTable(viewComplaintsTable);
            else if ("Assigned".equals(selected)) loadAllAssignedComplaintsTable(viewComplaintsTable);
            else loadAllUnAssignedComplaintsTable(viewComplaintsTable);
        });
        assignPanel.add(assignLabel);
        assignPanel.add(assignBox);

        filterPanel.add(statusPanel);
        filterPanel.add(priorityPanel);
        filterPanel.add(assignPanel);
        complaintsPanel.add(filterPanel, BorderLayout.BEFORE_FIRST_LINE);

        // ---------------------- TABLE Panel ----------------------
        viewComplaintsTable = new JTable();
        viewComplaintsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        viewComplaintsTable.setRowHeight(24);
        viewComplaintsTable.setBackground(new Color(45, 45, 45));
        viewComplaintsTable.setForeground(Color.WHITE);
        viewComplaintsTable.setGridColor(Color.DARK_GRAY);
        viewComplaintsTable.setFillsViewportHeight(true);

        viewComplaintsTable.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 15));
        viewComplaintsTable.getTableHeader().setBackground(new Color(60, 60, 60));
        viewComplaintsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(viewComplaintsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        complaintsPanel.add(scrollPane, BorderLayout.CENTER);
        loadAllComplaintsTable(viewComplaintsTable);

        // Set Column Widths
        TableColumnModel columnModel = viewComplaintsTable.getColumnModel();
        int[] widths = {20, 50, 60, 40, 150, 60, 50, 50, 90, 50, 20};
        for (int i = 0; i < widths.length && i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }

        jTabbedPane.addTab("View Complaints", complaintsPanel);
    }

    void setupManageComplaintsTab(){
        JPanel manageComplaintsPanel = new JPanel();
        manageComplaintsPanel.setLayout(new BorderLayout(10, 10));
        manageComplaintsPanel.setBackground(new Color(30, 30, 30));

        // Top Panel: Title + Refresh Button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel titleLabel = new JLabel("Manage Complaints");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setIcon(new ImageIcon("src/main/java/com/rishi/complaintsystem/images/refresh.png"));
        refreshButton.setFocusPainted(false);
        refreshButton.setBackground(new Color(50, 50, 50));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> loadManageComplaintTable(manageComplaintsTable));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);

        // Table
        manageComplaintsTable = new JTable();
        manageComplaintsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        manageComplaintsTable.setBackground(new Color(45, 45, 45));
        manageComplaintsTable.setForeground(Color.WHITE);
        manageComplaintsTable.setGridColor(Color.DARK_GRAY);
        manageComplaintsTable.setFillsViewportHeight(true);
        manageComplaintsTable.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 15));
        manageComplaintsTable.getTableHeader().setBackground(new Color(60, 60, 60));
        manageComplaintsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(manageComplaintsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        loadManageComplaintTable(manageComplaintsTable);

        // Column Widths
        TableColumnModel columnModel = manageComplaintsTable.getColumnModel();
        int[] widths = {20, 50, 40, 150, 60, 50, 50, 90};
        for (int i = 0; i < widths.length && i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }

        // Bottom Panel: Update Complaint Status Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Update Complaint Status", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));
        formPanel.setBackground(new Color(30, 30, 30));

        JLabel compIdLabel = new JLabel("Complaint ID:");
        compIdLabel.setForeground(Color.WHITE);
        JTextField complaintIDField = new JTextField();
        complaintIDField.putClientProperty("JTextField.placeholderText", "Enter Complaint ID");

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setForeground(Color.WHITE);
        String[] statuses = {"Pending", "In Progress", "Resolved", "Rejected"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);

        JButton updateButton = new JButton("Update");
        updateButton.setFocusPainted(false);
        updateButton.setBackground(new Color(60, 60, 60));
        updateButton.setForeground(Color.WHITE);

        updateButton.addActionListener(e -> {
            String idText = complaintIDField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Complaint ID is required.");
                return;
            }
            int complaintID;
            try {
                complaintID = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Complaint ID must be a number.");
                return;
            }

            String newStatus = (String) statusComboBox.getSelectedItem();
            boolean updated = complaintDAO.updateComplaintStatus(complaintID, newStatus);
            if (updated) {
                JOptionPane.showMessageDialog(null, "Status updated to: " + newStatus);
                loadManageComplaintTable(manageComplaintsTable);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update status.");
            }
        });

        formPanel.add(compIdLabel);
        formPanel.add(complaintIDField);
        formPanel.add(statusLabel);
        formPanel.add(statusComboBox);
        formPanel.add(new JLabel()); // empty cell
        formPanel.add(updateButton);

        // Add all to Main Panel
        manageComplaintsPanel.add(topPanel, BorderLayout.NORTH);
        manageComplaintsPanel.add(scrollPane, BorderLayout.CENTER);
        manageComplaintsPanel.add(formPanel, BorderLayout.SOUTH);

        jTabbedPane.addTab("Manage Complaints", manageComplaintsPanel);
    }

    void setupUserManagementTab(){
        JPanel userPanel = new JPanel(new BorderLayout(10, 10));
        userPanel.setBackground(new Color(30, 30, 30));
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------- Title & Refresh ----------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("User Management Panel");
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setIcon(new ImageIcon("src/main/java/com/rishi/complaintsystem/images/refresh.png"));
        refreshButton.setBackground(new Color(50, 50, 50));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(e -> loadUsersTable(userTable));

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);

        // ---------- User Table ----------
        userTable = new JTable();
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setFont(new Font("Arial", Font.PLAIN, 13));
        userTable.setBackground(new Color(45, 45, 45));
        userTable.setForeground(Color.WHITE);
        userTable.setGridColor(Color.DARK_GRAY);
        userTable.setFillsViewportHeight(true);
        userTable.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 14));
        userTable.getTableHeader().setBackground(new Color(60, 60, 60));
        userTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(userTable);
        loadUsersTable(userTable);

        JTextField selectedUserIdField = new JTextField();
        selectedUserIdField.setEditable(false);
        selectedUserIdField.putClientProperty("JTextField.placeholderText", "Selected User ID");

        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = userTable.getSelectedRow();
                if (row >= 0) {
                    selectedUserIdField.setText(userTable.getValueAt(row, 0).toString());
                }
            }
        });

        // ---------- Filter Panel ----------
        JPanel filterPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        filterPanel.setBackground(new Color(30, 30, 30));
        filterPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Filter Users", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        JLabel filterRoleLabel = new JLabel("Filter by Role:");
        filterRoleLabel.setForeground(Color.WHITE);

        String[] roles = {"all", "admin", "user"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.addActionListener(e -> {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            if ("all".equals(selectedRole)) {
                loadUsersTable(userTable);
            } else {
                loadUsersByRole(userTable, selectedRole);
            }
        });

        filterPanel.add(filterRoleLabel);
        filterPanel.add(roleComboBox);

        // ---------- Search Panel ----------
        JPanel searchPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        searchPanel.setBackground(new Color(30, 30, 30));
        searchPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Search Users", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        JTextField nameSearchField = new JTextField();
        nameSearchField.putClientProperty("JTextField.placeholderText", "Enter name");
        JButton nameSearchButton = new JButton("Search by Name");
        nameSearchButton.addActionListener(e -> {
            String userName = nameSearchField.getText().trim();
            loadUsersFilterByName(userTable, userName);
        });

        JTextField emailSearchField = new JTextField();
        emailSearchField.putClientProperty("JTextField.placeholderText", "Enter email");
        JButton emailSearchButton = new JButton("Search by Email");
        emailSearchButton.addActionListener(e -> {
            String email = emailSearchField.getText().trim();
            loadUsersFilterByEmail(userTable, email);
        });

        searchPanel.add(new JLabel("Name:") {{
            setForeground(Color.WHITE);
        }});
        searchPanel.add(nameSearchField);
        searchPanel.add(new JLabel("Email:") {{
            setForeground(Color.WHITE);
        }});
        searchPanel.add(emailSearchField);
        searchPanel.add(nameSearchButton);
        searchPanel.add(emailSearchButton);

        // ---------- Delete Panel ----------
        JPanel deletePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        deletePanel.setBackground(new Color(30, 30, 30));
        deletePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Delete User", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        JButton deleteButton = new JButton("Delete Selected User");
        deleteButton.setFocusPainted(false);
        deleteButton.setBackground(new Color(60, 60, 60));
        deleteButton.setForeground(Color.WHITE);

        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Please select a user to delete.");
                return;
            }

            String role = (String) userTable.getValueAt(selectedRow, 3);
            if ("admin".equals(role)) {
                JOptionPane.showMessageDialog(null, "Cannot delete an admin user!");
                return;
            }

            int userId = Integer.parseInt(userTable.getValueAt(selectedRow, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the user?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = userDAO.deleteUser(userId);
                if (success) {
                    ((DefaultTableModel) userTable.getModel()).removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "User deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete the user.");
                }
            }
        });

        deletePanel.add(new JLabel("Selected User ID:") {{
            setForeground(Color.WHITE);
        }});
        deletePanel.add(selectedUserIdField);
        deletePanel.add(new JLabel());
        deletePanel.add(deleteButton);

        // ---------- Combine all bottom panels ----------
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.add(filterPanel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(searchPanel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(deletePanel);

        // ---------- Add to main panel ----------
        userPanel.add(topPanel, BorderLayout.NORTH);
        userPanel.add(scrollPane, BorderLayout.CENTER);
        userPanel.add(bottomPanel, BorderLayout.SOUTH);

        jTabbedPane.addTab("Manage Users", userPanel);
    }

    public void setupAssignComplaintsTab(){
        JPanel assignTabPanel = new JPanel(new BorderLayout(10, 10));
        assignTabPanel.setBackground(new Color(30, 30, 30));
        assignTabPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ------------------- NORTH -------------------
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Assign Complaints To Staff");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setIcon(new ImageIcon("src/main/java/com/rishi/complaintsystem/images/refresh.png"));
        refreshButton.setBackground(new Color(50, 50, 50));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);
        assignTabPanel.add(topPanel, BorderLayout.NORTH);

        // ------------------- CENTER (Tables) -------------------
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(new Color(30, 30, 30));

        // First row
        JPanel tableRow1 = new JPanel(new GridLayout(1, 2, 10, 0));
        tableRow1.setBackground(new Color(30, 30, 30));

        unassignedComplaintsTable = new JTable();
        unassignedComplaintsTable.setBackground(new Color(45, 45, 45));
        unassignedComplaintsTable.setForeground(Color.WHITE);
        unassignedComplaintsTable.setGridColor(Color.GRAY);
        unassignedComplaintsTable.setFillsViewportHeight(true);
        unassignedComplaintsTable.getTableHeader().setBackground(new Color(60, 60, 60));
        unassignedComplaintsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane unassignedScroll = new JScrollPane(unassignedComplaintsTable);
        loadUnassignedComplaintsData(unassignedComplaintsTable);
        tableRow1.add(unassignedScroll);

        availableStaffTable = new JTable();
        availableStaffTable.setBackground(new Color(45, 45, 45));
        availableStaffTable.setForeground(Color.WHITE);
        availableStaffTable.setGridColor(Color.GRAY);
        availableStaffTable.setFillsViewportHeight(true);
        availableStaffTable.getTableHeader().setBackground(new Color(60, 60, 60));
        availableStaffTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane staffScroll = new JScrollPane(availableStaffTable);
        loadAvailableStaffs(availableStaffTable);
        tableRow1.add(staffScroll);

        centerPanel.add(tableRow1);

        // Second row
        JPanel assignedPanel = new JPanel(new BorderLayout());
        assignedPanel.setBackground(new Color(30, 30, 30));

        JLabel assignedLabel = new JLabel("Assigned Complaints Table:");
        assignedLabel.setForeground(Color.WHITE);
        assignedPanel.add(assignedLabel, BorderLayout.NORTH);

        assignedComplaintTable = new JTable();
        assignedComplaintTable.setBackground(new Color(45, 45, 45));
        assignedComplaintTable.setForeground(Color.WHITE);
        assignedComplaintTable.setGridColor(Color.GRAY);
        assignedComplaintTable.setFillsViewportHeight(true);
        assignedComplaintTable.getTableHeader().setBackground(new Color(60, 60, 60));
        assignedComplaintTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane assignedScroll = new JScrollPane(assignedComplaintTable);
        loadAllAssignedComplaints(assignedComplaintTable);
        assignedPanel.add(assignedScroll, BorderLayout.CENTER);

        centerPanel.add(assignedPanel);
        assignTabPanel.add(centerPanel, BorderLayout.CENTER);

        // ------------------- SOUTH -------------------
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(new Color(30, 30, 30));
        formPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Assign Complaint", 0, 0, new Font("Arial", Font.BOLD, 14), Color.WHITE));

        // Complaint ID selection
        JPanel complaintPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        complaintPanel.setBackground(new Color(30, 30, 30));
        JLabel complaintLabel = new JLabel("Select the Unassigned Complaint:");
        complaintLabel.setForeground(Color.WHITE);
        JComboBox<Integer> complaintComboBox = new JComboBox<>();
        loadUnassignedComplaintsIds(complaintComboBox);
        complaintPanel.add(complaintLabel);
        complaintPanel.add(complaintComboBox);

        // Staff selection
        JPanel staffPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        staffPanel.setBackground(new Color(30, 30, 30));
        JLabel staffLabel = new JLabel("Select the Staff to Assign To:");
        staffLabel.setForeground(Color.WHITE);
        JComboBox<Integer> staffComboBox = new JComboBox<>();
        loadStaffsId(staffComboBox);
        staffPanel.add(staffLabel);
        staffPanel.add(staffComboBox);

        // Weather Info
        JPanel weatherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        weatherPanel.setBackground(new Color(30, 30, 30));
        JLabel weatherTitleLabel = new JLabel("Weather Condition of the City from where Complaint Came:");
        weatherTitleLabel.setForeground(Color.WHITE);
        JLabel weatherLabel = new JLabel();
        weatherLabel.setForeground(Color.WHITE);
        JLabel weatherIcon = new JLabel();
        weatherPanel.add(weatherTitleLabel);
        weatherPanel.add(weatherLabel);
        weatherPanel.add(weatherIcon);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(30, 30, 30));
        JButton checkWeatherButton = new JButton("Check Weather");
        JButton assignButton = new JButton("Assign Staff");
        assignButton.setEnabled(false);
        buttonPanel.add(checkWeatherButton);
        buttonPanel.add(assignButton);

        formPanel.add(complaintPanel);
        formPanel.add(staffPanel);
        formPanel.add(weatherPanel);
        formPanel.add(buttonPanel);

        assignTabPanel.add(formPanel, BorderLayout.SOUTH);

        // ------------------- Button Actions -------------------
        complaintComboBox.addActionListener(e -> assignButton.setEnabled(false));

        checkWeatherButton.addActionListener(e -> {
            int selectedComplaint = (int) complaintComboBox.getSelectedItem();
            String city = complaintDAO.getCityFromComplaintId(selectedComplaint);

            try {
                WeatherResult result = OpenWeatherAPI.getCurrentWeather(city);
                weatherLabel.setText("City: " + city + " | " + result.getDescription() + " | " + result.getTemp() + "°C");

                String condition = result.getDescription().toLowerCase();
                ImageIcon gif = null;

                if (condition.contains("rain")) {
                    gif = new ImageIcon("src/main/java/com/rishi/complaintsystem/images/rain.png");
                    JOptionPane.showMessageDialog(null, "Weather conditions are not good. Try assigning later.");
                    assignButton.setEnabled(false);
                } else {
                    gif = switch (condition) {
                        case "sunny" -> new ImageIcon("src/main/java/com/rishi/complaintsystem/images/sun.png");
                        case "cloudy" -> new ImageIcon("src/main/java/com/rishi/complaintsystem/images/cloudy.png");
                        default -> new ImageIcon("src/main/java/com/rishi/complaintsystem/images/clear-sky.png");
                    };
                    assignButton.setEnabled(true);
                }

                weatherIcon.setIcon(gif);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Failed to fetch weather data.");
                ex.printStackTrace();
            }
        });

        assignButton.addActionListener(e -> {
            int complaintId = (int) complaintComboBox.getSelectedItem();
            int staffId = (int) staffComboBox.getSelectedItem();

            AssignmentDAO dao = new AssignmentDAO();
            boolean success = dao.assignStaffToComplaint(complaintId, staffId);

            if (success) {
                boolean updated = complaintDAO.updateComplaintStatus(complaintId, "In Progress");
                if (updated) {
                    JOptionPane.showMessageDialog(null, "Assigned and status updated!");
                    loadUnassignedComplaintsIds(complaintComboBox);
                    loadUnassignedComplaintsData(unassignedComplaintsTable);
                    loadAllAssignedComplaints(assignedComplaintTable);
                    assignButton.setEnabled(false);
                    weatherLabel.setText("");
                    weatherIcon.setIcon(null);
                } else {
                    JOptionPane.showMessageDialog(null, "Assigned but status not updated!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Assignment failed!");
            }
        });

        refreshButton.addActionListener(e -> {
            loadUnassignedComplaintsData(unassignedComplaintsTable);
            loadAvailableStaffs(availableStaffTable);
            loadAllAssignedComplaints(assignedComplaintTable);
        });

        jTabbedPane.addTab("Assign Complaint", assignTabPanel);
    }


    // --------------------    Important Functions ----------------------

    void loadAllComplaintsTable(JTable table){
        List<JoinedComplaintInfo> complaints = complaintDAO.getAllcomplaints();

        // columns for Jtable
        String[] cols = {"ID", "User Name", "User Email", "Category", "Description", "City",
                "Priority", "Status", "Date", "Assigned Staff", "Staff Contact"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(JoinedComplaintInfo info: complaints){
            model.addRow(new Object[]{
                    info.getComplaintID(),
                    info.getUserName(),
                    info.getUserEmail(),
                    info.getCategory(),
                    info.getDescription(),
                    info.getCity(),
                    info.getPriority(),
                    info.getStatus(),
                    info.getDateSubmitted(),
                    info.getStaffName() == null ? "Not Assigned": info.getStaffName(),
                    info.getStaffContact() == null ? "-":info.getStaffContact()
            });
        }

        table.setModel(model);
    }


    void loadAllAssignedComplaintsTable(JTable table){
        List<JoinedComplaintInfo> complaints = complaintDAO.getAllAssignedComplaints();

        // columns for Jtable
        String[] cols = {"ID", "User Name", "User Email", "Category", "Description", "City",
                "Priority", "Status", "Date", "Assigned Staff", "Staff Contact"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(JoinedComplaintInfo info: complaints){
            model.addRow(new Object[]{
                    info.getComplaintID(),
                    info.getUserName(),
                    info.getUserEmail(),
                    info.getCategory(),
                    info.getDescription(),
                    info.getCity(),
                    info.getPriority(),
                    info.getStatus(),
                    info.getDateSubmitted(),
                    info.getStaffName() == null ? "Not Assigned": info.getStaffName(),
                    info.getStaffContact() == null ? "-":info.getStaffContact()
            });
        }

        table.setModel(model);
    }

    void loadAllUnAssignedComplaintsTable(JTable table){
        List<JoinedComplaintInfo> complaints = complaintDAO.getAllUnAssignedComplaints();

        // columns for Jtable
        String[] cols = {"ID", "User Name", "User Email", "Category", "Description", "City",
                "Priority", "Status", "Date"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(JoinedComplaintInfo info: complaints){
            model.addRow(new Object[]{
                    info.getComplaintID(),
                    info.getUserName(),
                    info.getUserEmail(),
                    info.getCategory(),
                    info.getDescription(),
                    info.getCity(),
                    info.getPriority(),
                    info.getStatus(),
                    info.getDateSubmitted(),
            });
        }

        table.setModel(model);
    }

    // helps to display the filtered complaints based on status
    void loadComplaintsByStatus(JTable table,String status){
        List<JoinedComplaintInfo> complaints = complaintDAO.getComplaintByStatus(status);

        // columns for Jtable
        String[] cols = {"ID", "User Name", "User Email", "Category", "Description", "City",
                "Priority", "Status", "Date", "Assigned Staff", "Staff Contact"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(JoinedComplaintInfo info: complaints){
            model.addRow(new Object[]{
                    info.getComplaintID(),
                    info.getUserName(),
                    info.getUserEmail(),
                    info.getCategory(),
                    info.getDescription(),
                    info.getCity(),
                    info.getPriority(),
                    info.getStatus(),
                    info.getDateSubmitted(),
                    info.getStaffName() == null ? "Not Assigned": info.getStaffName(),
                    info.getStaffContact() == null ? "-":info.getStaffContact()
            });
        }

        table.setModel(model);
    }

    void loadComplaintsByPriority(JTable table,String priority){
        List<JoinedComplaintInfo> complaints = complaintDAO.getComplaintByPriority(priority);

        // columns for Jtable
        String[] cols = {"ID", "User Name", "User Email", "Category", "Description", "City",
                "Priority", "Status", "Date", "Assigned Staff", "Staff Contact"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(JoinedComplaintInfo info: complaints){
            model.addRow(new Object[]{
                    info.getComplaintID(),
                    info.getUserName(),
                    info.getUserEmail(),
                    info.getCategory(),
                    info.getDescription(),
                    info.getCity(),
                    info.getPriority(),
                    info.getStatus(),
                    info.getDateSubmitted(),
                    info.getStaffName() == null ? "Not Assigned": info.getStaffName(),
                    info.getStaffContact() == null ? "-":info.getStaffContact()
            });
        }

        table.setModel(model);
    }

    void loadManageComplaintTable(JTable manageComplaintsTable){
        List<Complaint> complaintList = complaintDAO.getAllNotResolvedComplaints();

        // columns for Jtable
        String[] cols = {"ID", "User ID", "Category", "Description", "City", "Priority", "Status", "Date Submitted"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(Complaint c: complaintList){
            model.addRow(new Object[]{
                    c.getId(),
                    c.getUserId(),
                    c.getCategory(),
                    c.getDescription(),
                    c.getCity(),
                    c.getStatus(),
                    c.getPriority(),
                    c.getDate_submitted()
            });
        }
        manageComplaintsTable.setModel(model);
    }


    void loadUsersTable(JTable table){
        List<User> usersList = userDAO.getAllUsers();

        // Columns
        String[] cols = {"User ID","Name","Email","Contact","Role","Total Complaints"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(User u: usersList){
            model.addRow(new Object[]{
                    u.getUserID(),
                    u.getName(),
                    u.getEmail(),
                    u.getContact(),
                    u.getRole(),
                    u.getTotalComplaints()
            });
        }
        table.setModel(model);
    }

    void loadUsersByRole(JTable table, String role){
        List<String[]> users = userDAO.getUsersByRole(role);

        String[] cols = {"User ID","Name","Email","Role"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(String[] rows: users){
            model.addRow(rows);
        }
        table.setModel(model);
    }

    private void loadUnassignedComplaintsIds(JComboBox complaintComboBox){
        complaintComboBox.removeAllItems();
        List<Integer> complaintsIds = complaintDAO.getUnassignedComplaintsID();

        for(int id : complaintsIds){
            complaintComboBox.addItem(id);
        }
    }

    private void loadUnassignedComplaintsData(JTable unassignedComplaintsTable){
        List<Complaint> complaintList = complaintDAO.getUnassignedComplaintsData();

        String[] cols = {"Complaint ID","Category","City","Priority","Status"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(Complaint c: complaintList){
            model.addRow(new Object[]{
                    c.getId(),
                    c.getCategory(),
                    c.getCity(),
                    c.getPriority(),
                    c.getStatus()
            });
        }
        unassignedComplaintsTable.setModel(model);
    }


    private void loadStaffsId(JComboBox staffComboBox){
        List<Integer> staffIds = staffDAO.getAllStaffIds();

        for(int id : staffIds){
            staffComboBox.addItem(id);
        }
    }

    private void loadAllAssignedComplaints(JTable assignedComplaintsTable){
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        List<Assignment> assignmentList = assignmentDAO.getAllAssignedComplaints();

        String[] cols = {"Assignment ID","Complaint ID","Staff ID","Staff Name","Assigned Date"};
        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(Assignment a : assignmentList){
            model.addRow(new Object[]{
                    a.getAssignmentId(),
                    a.getComplaintId(),
                    a.getStaffId(),
                    a.getStaffName(),
                    a.getAssignedDate()
            });
        }
        assignedComplaintsTable.setModel(model);
    }

    private void loadAvailableStaffs(JTable availableStaffTable){
        List<Staff> staffList = staffDAO.getAllAvailableStaff();

        String[] cols = {"Staff ID","Name","Contact","Availability"};
        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(Staff s: staffList){
            model.addRow(new Object[]{
                    s.getStaffId(),
                    s.getName(),
                    s.getContact(),
                    s.getIs_available()
            });
        }
        availableStaffTable.setModel(model);
    }

    public void loadUsersFilterByName(JTable userTable, String userName){
        List<User> usersList = userDAO.getAllUsersFilterByName(userName);

        // Columns
        String[] cols = {"User ID","Name","Email","Contact","Role","Total Complaints"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(User u: usersList){
            model.addRow(new Object[]{
                    u.getUserID(),
                    u.getName(),
                    u.getEmail(),
                    u.getContact(),
                    u.getRole(),
                    u.getTotalComplaints()
            });
        }
        userTable.setModel(model);
    }

    public void loadUsersFilterByEmail(JTable userTable, String userEmail){
        List<User> usersList = userDAO.getAllUsersFilterByEmail(userEmail);

        // Columns
        String[] cols = {"User ID","Name","Email","Contact","Role","Total Complaints"};

        DefaultTableModel model = new DefaultTableModel(cols,0);

        for(User u: usersList){
            model.addRow(new Object[]{
                    u.getUserID(),
                    u.getName(),
                    u.getEmail(),
                    u.getContact(),
                    u.getRole(),
                    u.getTotalComplaints()
            });
        }
        userTable.setModel(model);
    }
}
