package com.raphaeltannous;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

/**
 * EPMCreatePanel
 */
public class EPMCreatePanel extends JPanel {
    private EPMFrame frame;

    private JLabel pathLabel;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    EPMCreatePanel(
        EPMFrame frame
    ) {
        this.frame = frame;

        initPanelComponents();
    }

	private void initPanelComponents() {
        // Enabling closeMenuItem
        frame.closeMenuItem.setEnabled(true);

        JLabel databaseFilenameLabel = new JLabel();
        pathLabel = new JLabel();
        JLabel passwordFieldLabel = new JLabel();
        JLabel confirmPasswordFieldLabel = new JLabel();

        JTextField databaseFilenameTextField = new JTextField();

        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        JButton choosePathButton = new JButton();
        JButton createDatabaseButton = new JButton();

        this.setLayout(new MigLayout());

        databaseFilenameLabel.setText("Database Name:");
        add(databaseFilenameLabel, "cell 0 0");

        databaseFilenameTextField.setColumns(20);
        add(databaseFilenameTextField, "cell 1 0");

        choosePathButton.setText("Choose Path");
        choosePathButton.setMnemonic('P');
        choosePathButton.setDisplayedMnemonicIndex(0);
        choosePathButton.addActionListener(e -> choosePathActionListener());
        add(choosePathButton, "cell 0 1 2 1,align center");

        pathLabel.setText("No path selected.");
        add(pathLabel, "cell 0 2 2 1,align center");

        passwordFieldLabel.setText("Password:");
        add(passwordFieldLabel, "cell 0 3");

        passwordField.setColumns(20);
        add(passwordField, "cell 1 3");

        confirmPasswordFieldLabel.setText("Confirm Password:");
        add(confirmPasswordFieldLabel, "cell 0 4");

        confirmPasswordField.setColumns(20);
        add(confirmPasswordField, "cell 1 4");

        createDatabaseButton.setText("Create");
        createDatabaseButton.setMnemonic('C');
        createDatabaseButton.setDisplayedMnemonicIndex(0);
        createDatabaseButton.addActionListener(e -> createActionListener());
        add(createDatabaseButton, "cell 0 5 2 1,align center");
    }

	private Object createActionListener() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'createActionListener'");
	}

	private Object choosePathActionListener() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'choosePathActionListener'");
	}
}
