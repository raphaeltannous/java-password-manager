package com.raphaeltannous;

import java.awt.*;
import javax.swing.*;

public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame("EPM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Use GridBagLayout to center the buttons vertically and horizontally
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding around buttons
        gbc.anchor = GridBagConstraints.CENTER; // Center the buttons

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton createButton = new JButton("Create");
        JButton openButton = new JButton("Open");

        // Customize buttons
        createButton.setBackground(new Color(0, 123, 255));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setBorderPainted(false);
        createButton.setPreferredSize(new Dimension(80, 30));

        openButton.setBackground(new Color(40, 167, 69));
        openButton.setForeground(Color.WHITE);
        openButton.setFocusPainted(false);
        openButton.setBorderPainted(false);
        openButton.setPreferredSize(new Dimension(80, 30));

        // Add buttons to the panel
        buttonPanel.add(createButton);
        buttonPanel.add(openButton);

        // Add the button panel to the frame using GridBagLayout to center it
        frame.add(buttonPanel, gbc);

        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
    }
}
