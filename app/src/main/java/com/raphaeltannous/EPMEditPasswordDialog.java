package com.raphaeltannous;

import java.awt.Color;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import net.miginfocom.swing.MigLayout;


/**
 * EPMEditPasswordDialog
 */
public class EPMEditPasswordDialog extends JDialog {
    private EPMPasswordsPanel passwordsPanel;
    private EPMFrame frame; private int passwordId;
    private String[] sqlitePasswordData;

    private JPanel dialogPane;
    private JPanel contentPanel;

    private JPasswordField passwordField;
    private JPasswordField totpPasswordField;

    private JTextField websiteTextField;
    private JTextField usernameTextField;

    private JTextArea noteTextArea;

    private JButton updateButton;
    private JButton cancelButton;

    private Color[] success = new Color[]{new Color(0, 255, 0), new Color(200, 255, 200)};

    EPMEditPasswordDialog(
        Window owner,
        EPMPasswordsPanel passwordsPanel,
        EPMFrame frame,
        int passwordId
    ) {
        super(owner);

        this.passwordsPanel = passwordsPanel;
        this.passwordId = passwordId;
        this.frame = frame;

        initDialogComponents();

        getRootPane().setDefaultButton(updateButton);

        // Show reveal password button for passwordField and totpPasswordField
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");
        totpPasswordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton: true");

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

        // Set data in field
        // 0 -> id
        // 1 -> website
        // 2 -> username
        // 3 -> password
        // 4 -> totp
        // 5 -> hasBackupCodes
        // 6 -> note
        sqlitePasswordData = this.passwordsPanel.db.fetchPasswordData(this.passwordId);

        websiteTextField.setText(sqlitePasswordData[1]);
        usernameTextField.setText(sqlitePasswordData[2]);
        passwordField.setText(sqlitePasswordData[3]);
        totpPasswordField.setText(sqlitePasswordData[4]);
        noteTextArea.setText(sqlitePasswordData[6]);
    }

    private void initDialogComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();

        JLabel websiteTextFieldLabel = new JLabel();
        JLabel usernameTextFieldLabel = new JLabel();
        JLabel passwordFieldLabel = new JLabel();
        JLabel totpPasswordFieldLabel = new JLabel();
        JLabel noteTextAreaLabel = new JLabel();

        websiteTextField = new JTextField();
        usernameTextField = new JTextField();

        passwordField = new JPasswordField();
        totpPasswordField = new JPasswordField();

        JScrollPane noteTextAreaScrollPane = new JScrollPane();

        noteTextArea = new JTextArea();

        updateButton = new JButton();
        cancelButton = new JButton();

        // this
        setTitle("Edit Password");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(555, 444);
        setResizable(false);
        setModal(true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("insets 0, fill"));

        // dialogPane
        {
            dialogPane.setLayout(new MigLayout("insets 0"));

            // contentPanel
            {
                contentPanel.setLayout(new MigLayout("insets 0"));

                // websiteTextFieldLabel
                websiteTextFieldLabel.setText("Website");
                contentPanel.add(websiteTextFieldLabel, "cell 0 0");

                // usernameTextFieldLabel
                usernameTextFieldLabel.setText("Username");
                contentPanel.add(usernameTextFieldLabel, "cell 1 0");

                // websiteTextField
                websiteTextField.setColumns(20);
                EPMUtilities.addChangeListener(websiteTextField, e -> websiteTextFieldActionListener());
                contentPanel.add(websiteTextField, "cell 0 1");

                // usernameTextField
                usernameTextField.setColumns(20);
                EPMUtilities.addChangeListener(usernameTextField, e -> usernameTextFieldActionListener());
                contentPanel.add(usernameTextField, "cell 1 1");

                // passwordFieldLabel
                passwordFieldLabel.setText("Password");
                contentPanel.add(passwordFieldLabel, "cell 0 2 2 1, align center");

                // passwordField
                EPMUtilities.addChangeListener(passwordField, e -> passwordFieldActionListener());
                contentPanel.add(passwordField, "cell 0 3 2 1, growx");

                // totpPasswordFieldLabel
                totpPasswordFieldLabel.setText("TOTP");
                contentPanel.add(totpPasswordFieldLabel, "cell 0 4 2 1, align center");

                // totpPasswordField
                EPMUtilities.addChangeListener(totpPasswordField, e -> totpPasswordField());
                contentPanel.add(totpPasswordField, "cell 0 5 2 1, growx");

                // noteTextAreaLabel
                noteTextAreaLabel.setText("Note");
                contentPanel.add(noteTextAreaLabel, "cell 0 6 2 1, align center");

                // noteTextAreaScrollPane
                {
                    // noteTextArea
                    noteTextArea.setRows(7);
                    EPMUtilities.addChangeListener(noteTextArea, e -> noteTextAreaChangeListener());
                    noteTextAreaScrollPane.setViewportView(noteTextArea);
                }
                contentPanel.add(noteTextAreaScrollPane, "cell 0 7 2 1, growx");

                // addButton
                updateButton.setText("Update");
                updateButton.setMnemonic('U');
                updateButton.setDisplayedMnemonicIndex(0);
                updateButton.addActionListener(e -> updateButtonActionListener());
                updateButton.setEnabled(false);
                contentPanel.add(updateButton, "cell 0 8, align center");

                // cancelButton
                cancelButton.setText("Cancel");
                cancelButton.setMnemonic('C');
                cancelButton.setDisplayedMnemonicIndex(0);
                cancelButton.addActionListener(e -> cancelButtonActionLister());
                contentPanel.add(cancelButton, "cell 1 8, align center");
            }
        }
        dialogPane.add(contentPanel, "align center");

        contentPane.add(dialogPane, "align center");
        setLocationRelativeTo(getOwner());
    }

    private void totpPasswordField() {
        checkUpdateButtonStatus();
    }

    private void noteTextAreaChangeListener() {
        checkUpdateButtonStatus();
    }

    private void checkUpdateButtonStatus() {
        boolean isPasswordFieldEmpty = !isFieldEmpty(passwordField);
        boolean isWebsiteTextFieldEmpty = !isFieldEmpty(websiteTextField);
        boolean isUsernameTextFieldEmpty = !isFieldEmpty(usernameTextField);
        boolean isPasswordDataChanged = isPasswordDataChanged();

        boolean status = (
            isPasswordFieldEmpty &&
            isWebsiteTextFieldEmpty &&
            isUsernameTextFieldEmpty &&
            isPasswordDataChanged
        );

        updateButton.setEnabled(status);
    }

    private void passwordFieldActionListener() {
        boolean isEmpty = isFieldEmpty(passwordField);

        if (isEmpty) {
            passwordField.putClientProperty("JComponent.outline", "error");
        } else {
            passwordField.putClientProperty("JComponent.outline", this.success);
        }

        checkUpdateButtonStatus();
    }

    private void usernameTextFieldActionListener() {
        boolean isEmpty = isFieldEmpty(usernameTextField);

        if (isEmpty) {
            usernameTextField.putClientProperty("JComponent.outline", "error");
        } else {
            usernameTextField.putClientProperty("JComponent.outline", this.success);
        }

        checkUpdateButtonStatus();
    }

    private void websiteTextFieldActionListener() {
        boolean isEmpty = isFieldEmpty(websiteTextField);

        if (isEmpty) {
            websiteTextField.putClientProperty("JComponent.outline", "error");
        } else {
            websiteTextField.putClientProperty("JComponent.outline", this.success);
        }

        checkUpdateButtonStatus();
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

    private boolean isPasswordDataChanged() {
        String websiteData = websiteTextField.getText();
        String usernameData = usernameTextField.getText();
        String passwordData = String.valueOf(passwordField.getPassword());
        String totpData = String.valueOf(totpPasswordField.getPassword());
        String noteData = noteTextArea.getText();

        if (!websiteData.equals(sqlitePasswordData[1])) {
            return true;
        }

        if (!usernameData.equals(sqlitePasswordData[2])) {
            return true;
        }

        if (!passwordData.equals(sqlitePasswordData[3])) {
            return true;
        }

        if (!totpData.equals(sqlitePasswordData[4])) {
            return true;
        }

        if (!noteData.equals(sqlitePasswordData[6])) {
            return true;
        }


        return false;
    }

    private void cancelButtonActionLister() {
        dispose();
    }

    private void updateButtonActionListener() {
        String websiteData = websiteTextField.getText();
        String usernameData = usernameTextField.getText();
        String passwordData = String.valueOf(passwordField.getPassword());
        String totpData = String.valueOf(totpPasswordField.getPassword());
        String noteData = noteTextArea.getText();

        if (!websiteData.equals(sqlitePasswordData[1])) {
            passwordsPanel.db.modifyWebsite(this.passwordId, websiteData);
        }

        if (!usernameData.equals(sqlitePasswordData[2])) {
            passwordsPanel.db.modifyUsername(this.passwordId, usernameData);
        }

        if (!passwordData.equals(sqlitePasswordData[3])) {
            passwordsPanel.db.modifyUsername(this.passwordId, passwordData);
        }

        if (!totpData.equals(sqlitePasswordData[4])) {
            passwordsPanel.db.modifyOTP(this.passwordId, totpData);
        }

        if (!noteData.equals(sqlitePasswordData[6])) {
            passwordsPanel.db.modifyNote(this.passwordId, noteData);
        }

        dispose();
    }
}
