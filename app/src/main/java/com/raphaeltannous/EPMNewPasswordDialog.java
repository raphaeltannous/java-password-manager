package com.raphaeltannous;

import java.awt.Color;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import net.miginfocom.swing.MigLayout;

/**
 * EPMAddPasswordDialog
 */
public class EPMNewPasswordDialog extends JDialog {
    private EPMPasswordsPanel passwordsPanel;
    private EPMFrame frame;

    private JPanel dialogPane;
    private JPanel contentPanel;

    private JTextField websiteTextField;
    private JTextField usernameTextField;
    private JPasswordField passwordField;

    private JButton addButton;
    private JButton cancelButton;

    private Color[] success = new Color[]{new Color(0, 255, 0), new Color(200, 255, 200)};

    EPMNewPasswordDialog(
        Window owner,
        EPMPasswordsPanel passwordsPanel,
        EPMFrame frame
    ) {
        super(owner);

        this.passwordsPanel = passwordsPanel;
        this.frame = frame;

        initDialogComponents();

        getRootPane().setDefaultButton(addButton);


        // Show clear button (if passwordField is not empty)
        passwordField.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        // Show reveal password button
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");

        // Password Generator Button
        JButton passwordGeneratorButton = new JButton();
        passwordGeneratorButton.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/dice-5-gray.svg"));
        passwordGeneratorButton.addActionListener(e -> this.frame.generatePasswordMenuItemActionListener(passwordField));
        passwordGeneratorButton.setToolTipText("Password Generator");

        // Password toolBar
        JToolBar passwordToolBar = new JToolBar();
        passwordToolBar.addSeparator();
        passwordToolBar.add(passwordGeneratorButton);
        passwordField.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_COMPONENT, passwordToolBar);
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
        setSize(777, 333);
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
                websiteLabel.setText("Website");
                contentPanel.add(websiteLabel, "cell 0 0");

                // usernameLabel
                usernameLabel.setText("Username");
                contentPanel.add(usernameLabel, "cell 1 0");

                // websiteTextField
                websiteTextField.setColumns(20);
                EPMUtilities.addChangeListener(websiteTextField, e -> websiteTextFieldActionListener());
                contentPanel.add(websiteTextField, "cell 0 1");

                // usernameTextField
                usernameTextField.setColumns(20);
                EPMUtilities.addChangeListener(usernameTextField, e -> usernameTextFieldActionListener());
                contentPanel.add(usernameTextField, "cell 1 1");

                // passwordLabel
                passwordLabel.setText("Password");
                contentPanel.add(passwordLabel, "cell 0 2");

                // passwordField
                passwordField.setColumns(40);
                EPMUtilities.addChangeListener(passwordField, e -> passwordFieldActionListener());
                contentPanel.add(passwordField, "cell 0 3 2 1, growx");

                // addButton
                addButton.setText("Add");
                addButton.setMnemonic('A');
                addButton.setDisplayedMnemonicIndex(0);
                addButton.addActionListener(e -> addButtonActionListener());
                addButton.setEnabled(false);
                contentPanel.add(addButton, "cell 0 4, align center");

                // cancelButton
                cancelButton.setText("Cancel");
                cancelButton.setMnemonic('C');
                cancelButton.setDisplayedMnemonicIndex(0);
                cancelButton.addActionListener(e -> cancelButtonActionLister());
                contentPanel.add(cancelButton, "cell 1 4, align center");
            }
        }
        dialogPane.add(contentPanel, "align center");

        contentPane.add(dialogPane, "align center");
        setLocationRelativeTo(getOwner());
    }

    private boolean isFieldEmpty(Object obj) {
        if (obj instanceof JTextField) {
            JTextField textField = (JTextField) obj;

            return textField.getText().isEmpty();
        } else if (obj instanceof JPasswordField) {
            JPasswordField passwordField = (JPasswordField) obj;

            return String.valueOf(passwordField.getPassword()).isEmpty();
        } else {

            throw new IllegalArgumentException("Cannot determine the type of the object.");
        }
    }

    private void checkAddButtonStatus() {
        boolean isWebsiteTextFieldEmpty = isFieldEmpty(websiteTextField);
        boolean isUsernameTextFieldEmpty = isFieldEmpty(usernameTextField);
        boolean isPasswordFieldEmpty = isFieldEmpty(passwordField);

        boolean result = !isWebsiteTextFieldEmpty && !isUsernameTextFieldEmpty && !isPasswordFieldEmpty;

        if (result) {
            addButton.setEnabled(true);
            return;
        }

        addButton.setEnabled(false);
    }

    private void passwordFieldActionListener() {
        boolean isEmpty = isFieldEmpty(passwordField);

        if (isEmpty) {
            passwordField.putClientProperty("JComponent.outline", "error");
        } else {
            passwordField.putClientProperty("JComponent.outline", this.success);
        }

        checkAddButtonStatus();
    }

    private void usernameTextFieldActionListener() {
        boolean isEmpty = isFieldEmpty(usernameTextField);

        if (isEmpty) {
            usernameTextField.putClientProperty("JComponent.outline", "error");
        } else {
            usernameTextField.putClientProperty("JComponent.outline", this.success);
        }

        checkAddButtonStatus();
    }

    private void websiteTextFieldActionListener() {
        boolean isEmpty = isFieldEmpty(websiteTextField);

        if (isEmpty) {
            websiteTextField.putClientProperty("JComponent.outline", "error");
        } else {
            websiteTextField.putClientProperty("JComponent.outline", this.success);
        }

        checkAddButtonStatus();
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
