package com.raphaeltannous;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class App {

    public static void main(String[] args) {
        JFrame frame = new JFrame("EPM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton createButton = new JButton("Create");
        JButton openButton = new JButton("Open");

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

        buttonPanel.add(createButton);
        buttonPanel.add(openButton);

        frame.add(buttonPanel, gbc);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openCreateDatabasePage();
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileSelectionPage();
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void openCreateDatabasePage() {
        JFrame createFrame = new JFrame("EPM");
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createFrame.setSize(600, 400);
        createFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel dbNameLabel = new JLabel("Database Name:");
        dbNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        createFrame.add(dbNameLabel, gbc);

        JTextField dbNameField = new JTextField(20);
        gbc.gridx = 1;
        createFrame.add(dbNameField, gbc);

        JButton choosePathButton = new JButton("Choose Path");
        choosePathButton.setBackground(new Color(0, 123, 255));
        choosePathButton.setForeground(Color.WHITE);
        choosePathButton.setFocusPainted(false);
        choosePathButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        createFrame.add(choosePathButton, gbc);

        JLabel pathLabel = new JLabel("No path selected");
        pathLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        createFrame.add(pathLabel, gbc);

        JLabel passwordLabel = new JLabel("Enter Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        createFrame.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        createFrame.add(passwordField, gbc);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        createFrame.add(confirmPasswordLabel, gbc);

        JPasswordField confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        createFrame.add(confirmPasswordField, gbc);

        JButton createButton = new JButton("Create");
        createButton.setBackground(new Color(40, 167, 69));
        createButton.setForeground(Color.WHITE);
        createButton.setFocusPainted(false);
        createButton.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        createFrame.add(createButton, gbc);

        final String[] selectedPath = {null};

        choosePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(createFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedPath[0] = fileChooser.getSelectedFile().getAbsolutePath();
                    pathLabel.setText("Selected Path: " + selectedPath[0]);
                }
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dbName = dbNameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (dbName.isEmpty()) {
                    JOptionPane.showMessageDialog(createFrame, "Please enter a database name.");
                    return;
                }

                if (selectedPath[0] == null) {
                    JOptionPane.showMessageDialog(createFrame, "Please choose a path to save the database.");
                    return;
                }

                File file = new File(selectedPath[0] + "/" + dbName + ".db");
                if (file.exists()) {
                    JOptionPane.showMessageDialog(createFrame, "A file with this name already exists in the selected path.");
                    return;
                }

                if (password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(createFrame, "Please enter and confirm the password.");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(createFrame, "Passwords do not match.");
                    return;
                }

                try {
                    if (file.createNewFile()) {
                        JOptionPane.showMessageDialog(createFrame, "Database created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(createFrame, "Failed to create the database file.");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(createFrame, "An error occurred while creating the database file: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        createFrame.setLocationRelativeTo(null);
        createFrame.setVisible(true);
    }


    private static void openFileSelectionPage() {
        JFrame fileFrame = new JFrame("EPM");
        fileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        fileFrame.setSize(600, 300);
        fileFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel filePanel = new JPanel();
        filePanel.setLayout(new GridBagLayout());
        filePanel.setBorder(BorderFactory.createTitledBorder("File Selection"));
        GridBagConstraints fileGbc = new GridBagConstraints();
        fileGbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel fileLabel = new JLabel("No file selected");
        fileLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        fileGbc.gridx = 0;
        fileGbc.gridy = 0;
        fileGbc.gridwidth = 2;
        filePanel.add(fileLabel, fileGbc);

        JButton chooseFileButton = new JButton("Choose File");
        chooseFileButton.setBackground(new Color(0, 123, 255));
        chooseFileButton.setForeground(Color.WHITE);
        chooseFileButton.setFocusPainted(false);
        chooseFileButton.setBorderPainted(false);
        chooseFileButton.setPreferredSize(new Dimension(120, 30));
        fileGbc.gridx = 0;
        fileGbc.gridy = 1;
        fileGbc.gridwidth = 2;
        filePanel.add(chooseFileButton, fileGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        fileFrame.add(filePanel, gbc);

        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new GridBagLayout());
        passwordPanel.setBorder(BorderFactory.createTitledBorder("Password Entry"));
        GridBagConstraints passwordGbc = new GridBagConstraints();
        passwordGbc.insets = new Insets(5, 5, 5, 5);

        JLabel passwordLabel = new JLabel("Enter Master Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordGbc.gridx = 0;
        passwordGbc.gridy = 0;
        passwordPanel.add(passwordLabel, passwordGbc);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setPreferredSize(new Dimension(200, 30));
        passwordGbc.gridx = 1;
        passwordPanel.add(passwordField, passwordGbc);

        JButton openButton = new JButton("Open");
        openButton.setBackground(new Color(40, 167, 69));
        openButton.setForeground(Color.WHITE);
        openButton.setFocusPainted(false);
        openButton.setBorderPainted(false);
        openButton.setPreferredSize(new Dimension(120, 30));
        passwordGbc.gridx = 0;
        passwordGbc.gridy = 1;
        passwordGbc.gridwidth = 2;
        passwordGbc.anchor = GridBagConstraints.CENTER;
        passwordPanel.add(openButton, passwordGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        fileFrame.add(passwordPanel, gbc);

        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(fileFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileLabel.setText("Selected: " + fileChooser.getSelectedFile().getName());
                }
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                if (!password.isEmpty()) {
                    JOptionPane.showMessageDialog(fileFrame, "File opened with the provided password.");
                } else {
                    JOptionPane.showMessageDialog(fileFrame, "Please enter the master password.");
                }
            }
        });

        fileFrame.setLocationRelativeTo(null);
        fileFrame.setVisible(true);
    }
}
