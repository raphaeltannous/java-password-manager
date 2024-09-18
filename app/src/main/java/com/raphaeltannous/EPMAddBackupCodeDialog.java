package com.raphaeltannous;

import java.awt.Color;
import java.awt.Container;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import net.miginfocom.swing.MigLayout;

/**
 * EPMAddBackupCodeDialog
 */
public class EPMAddBackupCodeDialog extends JDialog {
    private EPMPasswordsPanel passwordsPanel;
    private int passwordId;

    private JPanel dialogPane;
    private JPanel contentPanel;

    private JTextField backupCodeTextField;

    private JButton addButton;
    private JButton cancelButton;

    EPMAddBackupCodeDialog(
        Window owner,
        EPMPasswordsPanel passwordsPanel,
        int passwordId
    ) {
        super(owner);

        this.passwordsPanel = passwordsPanel;
        this.passwordId = passwordId;

        initDialogComponents();

        getRootPane().setDefaultButton(addButton);
    }

    private void initDialogComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();

        JLabel backupCodeTextFieldLabel = new JLabel();

        backupCodeTextField = new JTextField();

        addButton = new JButton();
        cancelButton = new JButton();

        // this
        setTitle("Add Backup Code");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(333, 333);
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

                // backupCodeTextFieldLabel
                backupCodeTextFieldLabel.setText("Backup Code");
                contentPanel.add(backupCodeTextFieldLabel, "cell 0 0");

                // backupCodeTextField
                EPMUtilities.addChangeListener(backupCodeTextField, e -> backupCodeTextFieldChangeListener());
                contentPanel.add(backupCodeTextField, "cell 0 1 2 1, growx");

                // addButton
                addButton.setText("Add");
                addButton.setMnemonic('A');
                addButton.setDisplayedMnemonicIndex(0);
                addButton.addActionListener(e -> addButtonActionLister());
                addButton.setEnabled(false);
                contentPanel.add(addButton, "cell 0 2, align center");

                // cancelButton
                cancelButton.setText("Cancel");
                cancelButton.setMnemonic('C');
                cancelButton.setDisplayedMnemonicIndex(0);
                cancelButton.addActionListener(e -> cancelButtonActionListener());
                contentPanel.add(cancelButton, "cell 1 2, align center");
            }
        }
        dialogPane.add(contentPanel, "align center");

        contentPane.add(dialogPane, "align center");
        setLocationRelativeTo(getOwner());
    }

    private void cancelButtonActionListener() {
        dispose();
    }

    private void backupCodeTextFieldChangeListener() {
        Color[] success = new Color[]{new Color(0, 255, 0), new Color(200, 255, 200)};

        if (backupCodeTextField.getText().isEmpty()) {
            backupCodeTextField.putClientProperty("JComponent.outline", "error");
            addButton.setEnabled(false);

            return;
        }

        backupCodeTextField.putClientProperty("JComponent.outline", success);
        addButton.setEnabled(true);
    }

    private void addButtonActionLister() {
        String backupCode = backupCodeTextField.getText();

        passwordsPanel.db.addBackupCode(
            this.passwordId,
            backupCode
        );

        passwordsPanel.db.updateHasBackupCodeStatus(
            this.passwordId
        );

        dispose();
    }
}
