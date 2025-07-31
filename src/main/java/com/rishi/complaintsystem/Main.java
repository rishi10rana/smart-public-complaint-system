package com.rishi.complaintsystem;

import com.formdev.flatlaf.FlatDarkLaf;
import com.rishi.complaintsystem.db.DBConnection;
import com.rishi.complaintsystem.gui.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize FlatLaF");
            e.printStackTrace();
        }

        // Customize Title Bar Colors (FlatLaf Decoration)
        UIManager.put("TitlePane.unifiedBackground", false);
        UIManager.put("TitlePane.background", new java.awt.Color(23, 23, 23));
        UIManager.put("TitlePane.buttonSize",new Dimension(40,28));

        // Customize Main Panel Background
        UIManager.put("Panel.background", new java.awt.Color(30, 30, 30));

        new LoginFrame();
    }
}
