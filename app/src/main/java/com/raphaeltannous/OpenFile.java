package com.raphaeltannous;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OpenFile {

    public static void main(String[] args) {
        JFrame frame = new JFrame("File Opener with Master Password");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components

        // Create a label for file chooser
        JLabel fileLabel = new JLabel("No file selected");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        frame.add(fileLabel, gbc);

        // Create a button to open file chooser
        JButton chooseFileButton = new JButton("Choose File");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset to default
        frame.add(chooseFileButton, gbc);

        // Create a label and password field for entering the master password
        JLabel passwordLabel = new JLabel("Enter Master Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        frame.add(passwordField, gbc);

        // Create the "Open" button
        JButton openButton = new JButton("Open");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(openButton, gbc);

        // Add action listener to the "Choose File" button
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    fileLabel.setText("Selected: " + selectedFile.getName());
                }
            }
        });

        // Add action listener to the "Open" button
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                if (!password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "File opened with the provided password.");
                    // Further logic to handle file opening with password would go here
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter the master password.");
                }
            }
        });

        // Show the frame
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
    }
}
