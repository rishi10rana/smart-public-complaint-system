package com.rishi.complaintsystem.gui;

import javax.swing.*;
import java.awt.*;

public class aiSuggestionDialog extends JDialog {

    public aiSuggestionDialog(String problem, String aiSuggestion){
        setTitle("Complaint Summary + AI Suggestion");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setModalityType(ModalityType.APPLICATION_MODAL);

        JTextArea area = new JTextArea();
        area.setBackground(new Color(30, 30, 30));
        area.setEditable(false);
        area.setFont(new Font("Arial" , Font.PLAIN , 14));
        area.setFocusable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        String text = "Complaint Submitted Successfully!\n\n"
                   + "Description:\n" + problem + "\n\n"
                   + "AI Suggestion:\n" + aiSuggestion;

        area.setText(text);

        JScrollPane scrollPane = new JScrollPane(area);
        add(scrollPane);

        setVisible(true);
    }
}
