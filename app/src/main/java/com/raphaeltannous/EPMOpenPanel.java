package com.raphaeltannous;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import net.miginfocom.swing.MigLayout;

/**
 * EPMOpenPanel
 */
public class EPMOpenPanel extends JPanel {
    EPMFrame frame;

    EPMOpenPanel(
        EPMFrame frame
    ) {
        this.frame = frame;

        initPanelComponents();
    }

	private void initPanelComponents() {
        // Enabling closeMenuItem
        frame.closeMenuItem.setEnabled(true);

        JLabel passwordLabel = new JLabel();

        JLabel filePathLabel = new JLabel();

        JPasswordField passwordField = new JPasswordField();

        JButton chooseFilePathButton = new JButton();
        JButton openDatabaseButton = new JButton();

        setLayout(new MigLayout());

        chooseFilePathButton.setText("Choose Database File");
        chooseFilePathButton.setMnemonic('C');
        chooseFilePathButton.setDisplayedMnemonicIndex(0);
        chooseFilePathButton.addActionListener(e -> chooseActionListener());
        add(chooseFilePathButton, "cell 0 0 2 1,align center");

        filePathLabel.setText("No database file selected.");
        add(filePathLabel, "cell 0 1 2 1, align center");

        passwordLabel.setText("Master Password:");
        add(passwordLabel, "cell 0 2");

        passwordField.setColumns(20);
        add(passwordField, "cell 1 2");

        openDatabaseButton.setText("Open Database");
        openDatabaseButton.setMnemonic('O');
        openDatabaseButton.setDisplayedMnemonicIndex(0);
        openDatabaseButton.addActionListener(e -> openActionListener());
        add(openDatabaseButton, "cell 0 3 2 1,align center");
	}

	private Object openActionListener() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'openActionListener'");
	}

	private Object chooseActionListener() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'chooseActionListener'");
	}
}
