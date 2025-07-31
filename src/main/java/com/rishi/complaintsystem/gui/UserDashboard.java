package com.rishi.complaintsystem.gui;

import com.rishi.complaintsystem.ai.GeminiAPI;
import com.rishi.complaintsystem.dao.ComplaintDAO;
import com.rishi.complaintsystem.models.JoinedComplaintInfo;
import com.rishi.complaintsystem.models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserDashboard extends JFrame{

    User user;
    ComplaintDAO complaintDAO;

    private JTabbedPane jTabbedPane;
    private JPanel topPanel;

    private JLabel jLabel1;

    JTable complaintsTable;
    JTable userComplaintsTable;

    public UserDashboard(User user){
        // JFrame Properties
        setTitle("Smart Complaint System - User Dashboard");
        setSize(1400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // initialize received objects
        this.user = user;
        this.complaintDAO = new ComplaintDAO();

        topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setPreferredSize(new java.awt.Dimension(1200, 60));
        topPanel.setBackground(new Color(51,45 ,86));

        jTabbedPane = new JTabbedPane();

        jLabel1 = new JLabel();
        jLabel1.setFont(new Font("Consolas", 0, 20)); // NOI18N
        jLabel1.setText("User Dashboard");
        jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
        topPanel.add(jLabel1);
        jLabel1.setBounds(31, 19, 188, 24);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(1300, 16, 73, 23);
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
        setupComplaintsTable();
        setupSubmitComplaintTab(user);

        add(topPanel,BorderLayout.NORTH);
        add(jTabbedPane,BorderLayout.CENTER);
        setVisible(true);
    }

    private void setupWelcomeTab() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout(10, 10));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        welcomePanel.setBackground(new Color(20, 20, 20)); // flat dark background

        // Welcome Label at the Top
        JLabel jLabel1 = new JLabel("Welcome " + user.getName());
        jLabel1.setFont(new Font("Consolas", Font.BOLD, 22));
        jLabel1.setForeground(Color.WHITE);
        jLabel1.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.add(jLabel1);

        // Instruction Text Area in Center
        JTextArea jTextArea1 = new JTextArea(
                "User Dashboard Instructions:\n\n" +
                        "• View your submitted complaints\n" +
                        "• File a new complaint\n" +
                        "• Track status and priority\n" +
                        "• Logout anytime from the top-right"
        );
        jTextArea1.setFont(new Font("Consolas", Font.PLAIN, 16));
        jTextArea1.setBackground(new Color(30, 30, 30));
        jTextArea1.setForeground(Color.WHITE);
        jTextArea1.setEditable(false);
        jTextArea1.setFocusable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setWrapStyleWord(true);

        JScrollPane jScrollPane1 = new JScrollPane(jTextArea1);
        jScrollPane1.setPreferredSize(new Dimension(740, 425));
        jScrollPane1.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));

        // Add to main panel
        welcomePanel.add(topPanel, BorderLayout.NORTH);
        welcomePanel.add(jScrollPane1, BorderLayout.CENTER);

        jTabbedPane.addTab("Welcome", welcomePanel);
    }

    private void setupComplaintsTable(){
        JPanel complaintsPanel = new JPanel(new BorderLayout(10, 10));
        complaintsPanel.setBackground(new Color(30, 30, 30));

        // Top Panel (Title + Refresh)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("All the Complaints issued by: " + user.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setIcon(new ImageIcon("src/main/java/com/rishi/complaintsystem/images/refresh.png"));
        refreshButton.setFocusPainted(false);
        refreshButton.setBackground(new Color(50, 50, 50));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> loadUserComplaintsTable(userComplaintsTable));
        topPanel.add(refreshButton, BorderLayout.EAST);

        complaintsPanel.add(topPanel, BorderLayout.NORTH);

        // User Info Panel (Above table)
        JPanel userInfoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        userInfoPanel.setBackground(new Color(30, 30, 30));
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JLabel jLabel2 = new JLabel("User Name: " + user.getName());
        jLabel2.setFont(new Font("Arial", Font.PLAIN, 13));
        jLabel2.setForeground(Color.WHITE);

        JLabel jLabel3 = new JLabel("Email: " + user.getEmail());
        jLabel3.setFont(new Font("Arial", Font.PLAIN, 13));
        jLabel3.setForeground(Color.WHITE);

        JLabel jLabel4 = new JLabel("Role: " + user.getRole());
        jLabel4.setFont(new Font("Arial", Font.PLAIN, 13));
        jLabel4.setForeground(Color.WHITE);

        JLabel jLabel5 = new JLabel("Contact: " + user.getContact());
        jLabel5.setFont(new Font("Arial", Font.PLAIN, 13));
        jLabel5.setForeground(Color.WHITE);

        userInfoPanel.add(jLabel2);
        userInfoPanel.add(jLabel3);
        userInfoPanel.add(jLabel4);
        userInfoPanel.add(jLabel5);

        // Center Panel contains User Info + Table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(30, 30, 30));
        centerPanel.add(userInfoPanel, BorderLayout.NORTH);

        userComplaintsTable = new JTable();
        userComplaintsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userComplaintsTable.setForeground(Color.WHITE);
        userComplaintsTable.setBackground(new Color(45, 45, 45));
        userComplaintsTable.setGridColor(Color.DARK_GRAY);
        userComplaintsTable.setFillsViewportHeight(true);
        userComplaintsTable.getTableHeader().setFont(new Font("Consolas", Font.BOLD, 15));
        userComplaintsTable.getTableHeader().setBackground(new Color(60, 60, 60));
        userComplaintsTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(userComplaintsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        complaintsPanel.add(centerPanel, BorderLayout.CENTER);

        // Load data
        loadUserComplaintsTable(userComplaintsTable);
        TableColumnModel columnModel = userComplaintsTable.getColumnModel();
        int[] widths = {50, 50, 80, 300, 80, 120, 150};
        for (int i = 0; i < widths.length && i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }

        // Add to tabbedPane
        jTabbedPane.addTab("My Complaints", complaintsPanel);
    }

    private void loadUserComplaintsTable(JTable userComplaintsTable){
        int userId = user.getUserID();
        List<JoinedComplaintInfo> complaints = complaintDAO.getUserComplaintDetailsWithStaff(userId);

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

        userComplaintsTable.setModel(model);
    }

    private void setupSubmitComplaintTab(User user){
        JPanel jPanel1 = new JPanel(new BorderLayout(10, 10));
        jPanel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // padding

// ----- TOP: Title and User Info -----
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        JLabel jLabel1 = new JLabel("User Complaint Submission Panel");
        jLabel1.setFont(new Font("Consolas", Font.BOLD, 16));
        topPanel.add(jLabel1);

        JPanel userInfoPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        userInfoPanel.add(new JLabel("User Name: " + user.getName()));
        userInfoPanel.add(new JLabel("Email: " + user.getEmail()));
        userInfoPanel.add(new JLabel("Role: " + user.getRole()));
        userInfoPanel.add(new JLabel("Contact: " + user.getContact()));
        topPanel.add(userInfoPanel);

        jPanel1.add(topPanel, BorderLayout.NORTH);

// ----- CENTER: Complaint Form -----
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("New Complaint"));

// Category
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category Field:"));
        JTextField complaintCategory = new JTextField(25);
        complaintCategory.putClientProperty("JTextField.placeholderText", "Enter complaint category");
        categoryPanel.add(complaintCategory);
        formPanel.add(categoryPanel);

// Description
        JPanel descPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descPanel.add(new JLabel("Description:"));
        JTextArea complaintDescription = new JTextArea(5, 30);
        JScrollPane jScrollPane1 = new JScrollPane(complaintDescription);
        descPanel.add(jScrollPane1);
        formPanel.add(descPanel);

// Priority and City
        JPanel priorityCityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        priorityCityPanel.add(new JLabel("Priority:"));
        String[] priorities = {"Low", "Normal", "Urgent"};
        JComboBox<String> priorityBox = new JComboBox<>(priorities);
        priorityCityPanel.add(priorityBox);

        priorityCityPanel.add(new JLabel("City:"));
        JTextField cityField = new JTextField(15);
        cityField.putClientProperty("JTextField.placeholderText", "Enter city");
        priorityCityPanel.add(cityField);

        formPanel.add(priorityCityPanel);

// Submit Button and Status
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.add(new JLabel("Submit Complaint:"));
        JButton submitButton = new JButton("Submit");
        actionPanel.add(submitButton);

        ImageIcon loading = new ImageIcon("src/main/java/com/rishi/complaintsystem/images/loading.gif");
        JLabel loadingIcon = new JLabel(loading);
        loadingIcon.setVisible(false);
        actionPanel.add(loadingIcon);

        formPanel.add(actionPanel);

// Status Label
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel jLabel11 = new JLabel("Status:");
        JLabel statusLabel = new JLabel("");
        statusPanel.add(jLabel11);
        statusPanel.add(statusLabel);
        formPanel.add(statusPanel);

        jPanel1.add(formPanel, BorderLayout.CENTER);

// ----- Bottom: Separator -----
        jPanel1.add(new JSeparator(), BorderLayout.SOUTH);

// ----- Submit Action -----
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category = complaintCategory.getText();
                String description = complaintDescription.getText();
                String priority = (String) priorityBox.getSelectedItem();
                String city = cityField.getText();

                if (category.isEmpty() || description.isEmpty()) {
                    statusLabel.setText("Please fill in all fields.");
                    return;
                }

                boolean success = complaintDAO.submitComplaint(user.getUserID(), category, description, city, priority);
                if (success) {
                    statusLabel.setText("Complaint submitted successfully. Generating AI Response ...");
                    loadingIcon.setVisible(true);

                    String problem = description;

                    SwingWorker<String, Void> worker = new SwingWorker<>() {
                        @Override
                        protected String doInBackground() throws Exception {
                            GeminiAPI geminiAPI = new GeminiAPI();
                            return geminiAPI.getSolution(problem);
                        }

                        @Override
                        protected void done() {
                            loadingIcon.setVisible(false);
                            complaintCategory.setText("");
                            complaintDescription.setText("");
                            statusLabel.setText("");
                            try {
                                String aiSuggestion = get();
                                new aiSuggestionDialog(problem, aiSuggestion);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    worker.execute();

                    loadUserComplaintsTable(userComplaintsTable);
                } else {
                    statusLabel.setText("Failed to submit complaint.");
                }
            }
        });

        jTabbedPane.add("Submit Complaint", jPanel1);
    }
}