package com.raphaeltannous;

import java.awt.Color;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatLaf;

import net.miginfocom.swing.MigLayout;

/**
 * EPMBackupCodesDialog
 */
public class EPMBackupCodesDialog extends JDialog {
    private EPMPasswordsPanel passwordsPanel;
    private EPMFrame frame;
    private int passwordId;

    private JPanel dialogPane;
    private JPanel contentPanel;

    private JButton addButton;
    private JButton okButton;

    private boolean addButtonActionListerInProgress = false;

    EPMBackupCodesDialog(
        Window owner,
        EPMPasswordsPanel passwordsPanel,
        EPMFrame frame,
        int passwordId
    ) {
        super(owner);

        this.passwordsPanel = passwordsPanel;
        this.frame = frame;
        this.passwordId = passwordId;

        initDialogComponents();

        getRootPane().setDefaultButton(okButton);
    }

    private void initDialogComponents() {
        dialogPane = new JPanel();
        contentPanel = new JPanel();

        okButton = new JButton();
        addButton = new JButton();

        JScrollPane backupCodeScrollPane = new JScrollPane();

        // this
        setTitle("Backup Codes");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(333, 333);
        setResizable(false);
        setModal(true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("insets 0"));

        // dialogPane
        {
            dialogPane.setLayout(new MigLayout("insets 0"));

            // contentPanel
            {
                contentPanel.setLayout(new MigLayout(
                    "insets 0, fill",
                    "[333!]",
                    ""
                ));

                backupCodeScrollPane = getBackupCodesScrollPane();

                contentPanel.add(backupCodeScrollPane, "grow");
            }
        }
        dialogPane.add(contentPanel, "grow");

        contentPane.add(dialogPane, "align center");
        setLocationRelativeTo(getOwner());
    }

    private JScrollPane getBackupCodesScrollPane() {
        JPanel backupCodeScrollPanePanel = new JPanel();
        JScrollPane backupCodeScrollPane = new JScrollPane();

        // backupCodeScrollPane
        {
            backupCodeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            backupCodeScrollPane.setBorder(null);
            backupCodeScrollPane.getHorizontalScrollBar().setBorder(null);
            backupCodeScrollPane.getVerticalScrollBar().setBorder(null);

            // backupCodes
            {
                List<String[]> backupCodes =  passwordsPanel.db.fetchBackupCodes(this.passwordId);
                int backupCodesLength = backupCodes.size();

                backupCodeScrollPanePanel.setLayout(
                    new MigLayout(
                        "insets 10 0 10 0",
                        "[333!]",
                        ""
                    )
                );

                for (int i = 0; i < backupCodesLength; i++) {
                    String[] backupCodeData = backupCodes.get(i);


                    backupCodeScrollPanePanel.add(
                        new BackupCodeItem(
                            this,
                            this.passwordsPanel,
                            backupCodeData[2],
                            this.passwordId,
                            Integer.valueOf(backupCodeData[0])
                        ),
                        "align center, wrap"
                    );
                }

                // addButton
                addButton.setText("+");
                addButton.setMnemonic('A');
                addButton.setDisplayedMnemonicIndex(0);
                addButton.addActionListener(e -> addButtonActionLister());
                backupCodeScrollPanePanel.add(addButton, "span, align center");
            }
        }
        backupCodeScrollPane.setViewportView(backupCodeScrollPanePanel);

        return backupCodeScrollPane;
    }

    protected void updateBackupCodesScrollPane() {
        JScrollPane backupCodeScrollPane = getBackupCodesScrollPane();

        contentPanel.removeAll();
        contentPanel.add(backupCodeScrollPane, "grow");
        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
    }

    private void addButtonActionLister() {
        if (addButtonActionListerInProgress) {
            return;
        }

        addButtonActionListerInProgress = true;

        EPMAddBackupCodeDialog addBackupCodeDialog = new EPMAddBackupCodeDialog(
            this.frame,
            this.passwordsPanel,
            this.passwordId
        );

        addBackupCodeDialog.setVisible(true);

        updateBackupCodesScrollPane();

        SwingUtilities.invokeLater(() -> addButtonActionListerInProgress = false);
    }
}

class BackupCodeItem extends JPanel {
    private EPMBackupCodesDialog backupCodesDialog;
    private EPMPasswordsPanel passwordsPanel;
    private String backupCode;
    private int passwordId;
    private int backupCodeId;

    private JTextField backupCodeTextField;

    private JButton editButton;
    private JButton deleteButton;

    private boolean editButtonActionListenerInProgress = false;
    private boolean deleteButtonActionListenerInProgress = false;
    private boolean confirmButtonActionListenerInProgress = false;

    BackupCodeItem(
        EPMBackupCodesDialog backupCodesDialog,
        EPMPasswordsPanel passwordsPanel,
        String backupCode,
        int passwordId,
        int backupCodeId
    ) {
        this.backupCodesDialog = backupCodesDialog;
        this.passwordsPanel = passwordsPanel;
        this.backupCode = backupCode;
        this.passwordId = passwordId;
        this.backupCodeId = backupCodeId;

        initPanelComponents();
    }

    private void initPanelComponents() {
        this.setLayout(new MigLayout("insets 0"));

        backupCodeTextField = new JTextField();

        deleteButton = new JButton();
        editButton = new JButton();

        // this
        {
            // backupCodeTextField
            backupCodeTextField.setColumns(7);
            backupCodeTextField.setText(backupCode);
            backupCodeTextField.setEditable(false);
            EPMUtilities.addChangeListener(backupCodeTextField, e -> backupCodeTextFieldChangeListener());
            this.add(backupCodeTextField);

            // deleteButton
            deleteButton.setText("Delete");
            deleteButton.addActionListener(e -> deleteButtonActionListener());
            this.add(deleteButton);

            // editButton
            editButton.setText("Edit");
            editButton.addActionListener(e -> editButtonActionListener());
            this.add(editButton);
        }
    }

    private void backupCodeTextFieldChangeListener() {
        Color[] success = new Color[]{new Color(0, 255, 0), new Color(200, 255, 200)};

        if (backupCodeTextField.getText().isEmpty()) {
            backupCodeTextField.putClientProperty("JComponent.outline", "error");
            editButton.setEnabled(false);

            return;
        }

        backupCodeTextField.putClientProperty("JComponent.outline", success);
        editButton.setEnabled(true);
    }

    private void removeEditButtonActionListener() {
        ActionListener actionListener = editButton.getActionListeners()[0];

        editButton.removeActionListener(actionListener);
    }

    private void editButtonActionListener() {
        if (editButtonActionListenerInProgress) {
            return;
        }

        editButtonActionListenerInProgress = true;

        removeEditButtonActionListener();

        backupCodeTextField.setEditable(true);
        editButton.setText("Confirm");
        editButton.addActionListener(e -> confirmButtonActionListener());

        SwingUtilities.invokeLater(() -> editButtonActionListenerInProgress = false);
    }

    private void confirmButtonActionListener() {
        if (confirmButtonActionListenerInProgress) {
            return;
        }

        confirmButtonActionListenerInProgress = true;

        removeEditButtonActionListener();

        backupCodeTextField.setEditable(false);

        editButton.setText("Edit");
        editButton.addActionListener(e -> editButtonActionListener());

        String newBackupCode = backupCodeTextField.getText();

        passwordsPanel.db.modifyBackupCode(
            this.backupCodeId,
            newBackupCode
        );

        SwingUtilities.invokeLater(() -> confirmButtonActionListenerInProgress = false);
    }

    private void deleteButtonActionListener() {
        if (deleteButtonActionListenerInProgress) {
            return;
        }

        deleteButtonActionListenerInProgress = true;

        passwordsPanel.db.removeBackupCode(this.backupCodeId);

        passwordsPanel.db.updateHasBackupCodeStatus(
            this.passwordId
        );

        backupCodesDialog.updateBackupCodesScrollPane();

        SwingUtilities.invokeLater(() -> deleteButtonActionListenerInProgress = false);
    }
}
