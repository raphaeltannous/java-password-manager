package com.raphaeltannous;

import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;

import net.miginfocom.swing.MigLayout;

/**
 * EPMCreatePanel
 */
public class EPMCreatePanel extends JPanel {
    private EPMFrame frame;

    private JLabel selectedPathLabel;

    private JTextField databaseFilenameTextField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private JButton createDatabaseButton;

    private Path chosenPath = null;

    private Color[] success = new Color[]{new Color(0, 255, 0), new Color(200, 255, 200)};

    EPMCreatePanel(
        EPMFrame frame
    ) {
        this.frame = frame;

        initPanelComponents();

        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        confirmPasswordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
    }

    private void initPanelComponents() {
        // Enabling closeMenuItem
        frame.closeMenuItem.setEnabled(true);
        frame.newPasswordMenuItem.setEnabled(false);

        JLabel databaseFilenameLabel = new JLabel();
        selectedPathLabel = new JLabel();
        JLabel passwordFieldLabel = new JLabel();
        JLabel confirmPasswordFieldLabel = new JLabel();

        databaseFilenameTextField = new JTextField();

        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        JButton choosePathButton = new JButton();
        createDatabaseButton = new JButton();

        this.setLayout(new MigLayout());

        databaseFilenameLabel.setText("Database Name:");
        add(databaseFilenameLabel, "cell 0 0");

        databaseFilenameTextField.setColumns(20);
        EPMUtilities.addChangeListener(databaseFilenameTextField, e -> databaseFilenameTextFieldChangeListener());
        add(databaseFilenameTextField, "cell 1 0");

        choosePathButton.setText("Choose Path");
        choosePathButton.setMnemonic('P');
        choosePathButton.setDisplayedMnemonicIndex(0);
        choosePathButton.addActionListener(e -> choosePathActionListener());
        add(choosePathButton, "cell 0 1 2 1,align center");

        selectedPathLabel.setText("No path selected.");
        add(selectedPathLabel, "cell 0 2 2 1,align center");

        passwordFieldLabel.setText("Password:");
        add(passwordFieldLabel, "cell 0 3");

        passwordField.setColumns(20);
        EPMUtilities.addChangeListener(passwordField, e -> passwordFieldChangeListener());
        add(passwordField, "cell 1 3");

        confirmPasswordFieldLabel.setText("Confirm Password:");
        add(confirmPasswordFieldLabel, "cell 0 4");

        confirmPasswordField.setColumns(20);
        EPMUtilities.addChangeListener(confirmPasswordField, e -> confirmPasswordFieldChangeListener());
        add(confirmPasswordField, "cell 1 4");

        createDatabaseButton.setText("Create");
        createDatabaseButton.setMnemonic('C');
        createDatabaseButton.setDisplayedMnemonicIndex(0);
        createDatabaseButton.setEnabled(false);
        createDatabaseButton.addActionListener(e -> createActionListener());
        add(createDatabaseButton, "cell 0 5 2 1,align center");
    }

    private void checkCreateButtonStatus() {
        if (
            passwordsConfirmation() &&
            filenameRestrictionCheck() &&
            Objects.nonNull(chosenPath)
        ) {
            createDatabaseButton.setEnabled(true);
            return;
        }

        createDatabaseButton.setEnabled(false);
    }

    private void passwordFieldChangeListener() {
        confirmPasswordFieldChangeListener();
    }

    private void confirmPasswordFieldChangeListener() {
        if (!passwordsConfirmation()) {
            passwordField.putClientProperty("JComponent.outline", "error");
            confirmPasswordField.putClientProperty("JComponent.outline", "error");
        } else {
            passwordField.putClientProperty("JComponent.outline", this.success);
            confirmPasswordField.putClientProperty("JComponent.outline", this.success);
        }

        checkCreateButtonStatus();
    }

    private boolean passwordsConfirmation() {
        if (
            passwordField.getPassword().length == 0 ||
            confirmPasswordField.getPassword().length == 0
        ) {
            return false;
        }

        if (
            Arrays.equals(
                passwordField.getPassword(),
                confirmPasswordField.getPassword()
            )
        ) {
            return true;
        }

        return false;
    }

    private boolean filenameRestrictionCheck() {
        String filename = databaseFilenameTextField.getText();

        if (filename.isEmpty()) {
            return false;
        }

        boolean directoryCheck = true;
        boolean filenameCheck = filename.matches("^(?! )[a-zA-Z0-9_. ]*(?<! )$");

        if (!Objects.isNull(chosenPath) && checkIfFilenameInDir(filename, chosenPath)) {
            directoryCheck = false;
        }

        return directoryCheck && filenameCheck;
    }

    private boolean checkIfFilenameInDir(String filename, Path directory) {
        Path filePath = Paths.get(directory.toAbsolutePath().toString(), filename + ".epm");

        return Files.exists(filePath);
    }

    private void databaseFilenameTextFieldChangeListener() {
        if (!filenameRestrictionCheck()) {
            databaseFilenameTextField.putClientProperty("JComponent.outline", "error");
        } else {
            databaseFilenameTextField.putClientProperty("JComponent.outline", this.success);
        }

        checkCreateButtonStatus();
    }

    private void createActionListener() {
        Path filePath = Paths.get(chosenPath.toAbsolutePath().toString(), databaseFilenameTextField.getText() + ".epm");

        EPMPasswordsPanel passwordsPanel = new EPMPasswordsPanel(
            this.frame,
            filePath,
            String.valueOf(passwordField.getPassword())
        );

        frame.contentPane.removeAll();
        frame.contentPane.add(passwordsPanel, "grow");

        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
    }

    private void choosePathActionListener() {
        JFileChooser pathChooser = new JFileChooser();

        pathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = pathChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            chosenPath = Paths.get(pathChooser.getSelectedFile().getAbsolutePath());
            chosenPath = chosenPath.normalize();

            databaseFilenameTextFieldChangeListener();

            selectedPathLabel.setText(chosenPath.toString());
        }

        checkCreateButtonStatus();
    }
}
