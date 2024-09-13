package com.raphaeltannous;

import java.awt.Container;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatClientProperties;

import net.miginfocom.swing.MigLayout;

/**
 * EPMAddPasswordDialog
 */
public class EPMNewPasswordDialog extends JDialog {
    private EPMPasswordsPanel passwordsPanel;

    private JPanel dialogPane;
    private JPanel contentPanel;

    // TODO: Check that each on of these TextFields cannot be empty.
    private JTextField websiteTextField;
    private JTextField usernameTextField;
    private JPasswordField passwordField;

    private JButton addButton;
    private JButton cancelButton;

    EPMNewPasswordDialog(
        Window owner,
        EPMPasswordsPanel passwordsPanel
    ) {
        super(owner);

        this.passwordsPanel = passwordsPanel;

        initDialogComponents();

        getRootPane().setDefaultButton(addButton);

        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
    }

    private void initDialogComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();

        JLabel websiteLabel = new JLabel();
        JLabel usernameLabel = new JLabel();
        JLabel passwordLabel = new JLabel();

        websiteTextField = new JTextField();
        usernameTextField = new JTextField();
        passwordField = new JPasswordField();

        addButton = new JButton();
        cancelButton = new JButton();

        // this
        setTitle("New Password");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("insets 0, fill"));

        // dialogPane
        {
            dialogPane.setLayout(new MigLayout("insets 0"));

            // contentPanel
            {
                contentPanel.setLayout(new MigLayout("insets 0"));

                // websiteLabel
                websiteLabel.setText("Website:");
                contentPanel.add(websiteLabel, "cell 0 0");

                // websiteTextField
                websiteTextField.setColumns(20);
                contentPanel.add(websiteTextField, "cell 1 0");

                // usernameLabel
                usernameLabel.setText("Username:");
                contentPanel.add(usernameLabel, "cell 0 1");

                // usernameTextField
                usernameTextField.setColumns(20);
                contentPanel.add(usernameTextField, "cell 1 1");

                // passwordLabel
                passwordLabel.setText("Password:");
                contentPanel.add(passwordLabel, "cell 0 2");

                // passwordField
                passwordField.setColumns(20);
                contentPanel.add(passwordField, "cell 1 2");

                // addButton
                addButton.setText("Add");
                addButton.setMnemonic('A');
                addButton.setDisplayedMnemonicIndex(0);
                addButton.addActionListener(e -> addButtonActionListener());
                contentPanel.add(addButton, "cell 0 3");

                // cancelButton
                cancelButton.setText("Cancel");
                cancelButton.setMnemonic('C');
                cancelButton.setDisplayedMnemonicIndex(0);
                cancelButton.addActionListener(e -> cancelButtonActionLister());
                contentPanel.add(cancelButton, "cell 1 3");
            }
        }
        dialogPane.add(contentPanel, "align center");

        contentPane.add(dialogPane, "align center");
        pack();
        setLocationRelativeTo(getOwner());
    }

    private void cancelButtonActionLister() {
        dispose();
    }

    private void addButtonActionListener() {
        String website = websiteTextField.getText();
        String username = usernameTextField.getText();
        String password = String.valueOf(passwordField.getPassword());

        passwordsPanel.db.addPassword(website, username, password);

        passwordsPanel.updateTableModel();

        dispose();
    }
}
