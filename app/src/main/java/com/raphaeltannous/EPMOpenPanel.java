package com.raphaeltannous;

import java.awt.Color;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;

import net.miginfocom.swing.MigLayout;

/**
 * EPMOpenPanel
 */
public class EPMOpenPanel extends JPanel {
    EPMFrame frame;

    private JLabel authenticationLabel;
    private JLabel filePathLabel;

    private JPasswordField passwordField;

    private JButton chooseFilePathButton;
    private JButton openDatabaseButton;

    private Path chosenFile = null;

    private Color[] success = new Color[]{new Color(0, 255, 0), new Color(200, 255, 200)};

    EPMOpenPanel(
        EPMFrame frame
    ) {
        this.frame = frame;

        initPanelComponents();

        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
    }

	private void initPanelComponents() {
        // Enabling closeMenuItem
        frame.closeMenuItem.setEnabled(true);

        JLabel passwordLabel = new JLabel();

        filePathLabel = new JLabel();
        authenticationLabel = new JLabel();

        passwordField = new JPasswordField();

        chooseFilePathButton = new JButton();
        openDatabaseButton = new JButton();

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
        EPMUtilities.addChangeListener(passwordField, e -> passwordFieldChangeListener());
        add(passwordField, "cell 1 2");

        openDatabaseButton.setText("Open Database");
        openDatabaseButton.setMnemonic('O');
        openDatabaseButton.setDisplayedMnemonicIndex(0);
        openDatabaseButton.setEnabled(false);
        openDatabaseButton.addActionListener(e -> openActionListener());
        add(openDatabaseButton, "cell 0 3 2 1,align center");

        add(authenticationLabel, "cell 0 4 2 1, align center");
	}

    private boolean passwordConfirmation() {
        if (passwordField.getPassword().length > 0) {
            return true;
        }

        return false;
    }

    private void passwordFieldChangeListener() {
        if (!passwordConfirmation()) {
            passwordField.putClientProperty("JComponent.outline", "error");
        } else {
            passwordField.putClientProperty("JComponent.outline", this.success);
        }

        checkOpenButtonStatus();
	}

	private void checkOpenButtonStatus() {
        if (
            passwordField.getPassword().length > 0 &&
            Objects.nonNull(chosenFile)
        ) {
            openDatabaseButton.setEnabled(true);
            return;
        }

        openDatabaseButton.setEnabled(false);
    }

	private void openActionListener() {
        if (
            PasswordManagerInterface.isFileADB(
                chosenFile,
                String.valueOf(passwordField.getPassword())
            )
        ) {

            frame.contentPane.removeAll();
            FlatLaf.revalidateAndRepaintAllFramesAndDialogs();

            return;
        }

        passwordField.putClientProperty("JComponent.outline", "error");

        authenticationLabel.setText("Wrong Credentials...");
        authenticationLabel.setForeground(Color.RED);

        // TODO: Change screen when Implemented
	}

	private void chooseActionListener() {
        JFileChooser fileChooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Encrypt Password Manager Files", "epm");

        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            chosenFile = Paths.get(fileChooser.getSelectedFile().getAbsolutePath());

            chosenFile = chosenFile.normalize();

            filePathLabel.setText(chosenFile.toString());
        }

        checkOpenButtonStatus();
	}
}
