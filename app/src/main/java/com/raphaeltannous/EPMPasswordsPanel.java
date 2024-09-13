package com.raphaeltannous;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;

import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import net.miginfocom.swing.MigLayout;

/**
 * EPMPasswordsPanel
 */
public class EPMPasswordsPanel extends JPanel {
    private EPMFrame frame;
    protected PasswordManagerInterface db;

    private JTable passwordsTable;
    private DefaultTableModel passwordsTableModel;

    EPMPasswordsPanel(
        EPMFrame frame,
        Path databasePath,
        String databasePassword
    ) {
        this.frame = frame;

        db = new PasswordManagerSQLite(
            databasePath,
            databasePassword
        );

        initPanelComponents();

        if (db.getPasswordsCount() > 0) {
            passwordsTable.addRowSelectionInterval(0, 0);
        }

        enableTools();
    }

    private void enableTools() {
        frame.newPasswordMenuItem.setEnabled(true);
        enableToolsOnSelection();

        frame.newPasswordMenuItem.addActionListener(e -> newPasswordMenuItemActionListener());
        frame.deletePasswordMenuItem.addActionListener(e -> deletePasswordMenuItemActionListener());
        frame.editPasswordMenuItem.addActionListener(e -> editPasswordMenuItemActionListener());
    }

    private void enableToolsOnSelection() {
        boolean status = true;

        if (passwordsTable.getSelectedRow() == -1) {
            status = false;
        }

        frame.editPasswordMenuItem.setEnabled(status);
        frame.deletePasswordMenuItem.setEnabled(status);
    }

    private Object[][] databaseDataToTableData() {
        List<String[]> passwords = db.fetchPasswords();
        int passwordsSize = passwords.size();

        Object[][] convertedPasswords = new Object[passwordsSize][];

        for (int i = 0; i < passwordsSize; i++) {
            Object[] row = passwords.get(i);

            convertedPasswords[i] = row;
        }

        return convertedPasswords;
    }

    private void initPanelComponents() {
        // Enabling closeMenuItem
        frame.closeMenuItem.setEnabled(true);

        JScrollPane scrollPane = new JScrollPane();
        passwordsTable = new JTable();

        JPopupMenu passwordPopupMenu = new JPopupMenu();
        JMenuItem newPasswordMenuItem = new JMenuItem();
        JMenuItem editPasswordMenuItem = new JMenuItem();
        JMenuItem deletePasswordMenuItem = new JMenuItem();

        setLayout(new MigLayout("insets 0, fill"));

        // scrollPane
        {
            // table
            passwordsTableModel = getTableModel();

            passwordsTable.setModel(passwordsTableModel);

            passwordsTable.setAutoCreateRowSorter(true);
            scrollPane.setViewportView(passwordsTable);
        }

        // Hiding the Id Column, but it will be still accessible by the code.
        //passwordsTable.removeColumn(
        //    passwordsTable.getColumnModel().getColumn(0)
        //);

        add(scrollPane, "grow");

        // passwordPopupMenu
        {
            // newPasswordMenuItem
            newPasswordMenuItem.setText("Add Password");
            newPasswordMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/plus-square.svg"));
            newPasswordMenuItem.addActionListener(e -> newPasswordMenuItemActionListener());
            passwordPopupMenu.add(newPasswordMenuItem);

            // editPasswordMenuItem
            editPasswordMenuItem.setText("Edit Password");
            editPasswordMenuItem.addActionListener(e -> editPasswordMenuItemActionListener());
            editPasswordMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/pencil-square.svg"));
            passwordPopupMenu.add(editPasswordMenuItem);
            passwordPopupMenu.addSeparator();

            // deletePasswordMenuItem
            deletePasswordMenuItem.setText("Delete Password");
            deletePasswordMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/x-square.svg"));
            deletePasswordMenuItem.addActionListener(e -> deletePasswordMenuItemActionListener());
            passwordPopupMenu.add(deletePasswordMenuItem);
        }

        passwordsTable.setComponentPopupMenu(passwordPopupMenu);

        // Mouse listener
        passwordsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                Point point = event.getPoint();
                int currentRow = passwordsTable.rowAtPoint(point);
                passwordsTable.setRowSelectionInterval(currentRow, currentRow);

                enableTools();
            }
        });
    }

    private DefaultTableModel getTableModel() {
        return new DefaultTableModel(
            databaseDataToTableData(),
            new String[] {
                "Id", "Website", "Username/Email", "Passwords", "OTP", "Has Backup Code", "Note"
            }
        ) {
            Class<?>[] columnTypes = {
                String.class, String.class, String.class, String.class, String.class, String.class, String.class
            };

            boolean[] columnEditable = {
                false, false, false, false, false, false, false
            };

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnEditable[columnIndex];
            }
        };
    }

    protected void updateTableModel() {
        DefaultTableModel newTableModel = getTableModel();

        passwordsTable.setModel(newTableModel);
    }

    private int getSelectedPasswordId() {
        int selectedRow = passwordsTable.getSelectedRow();
        return Integer.parseInt((String) passwordsTable.getValueAt(selectedRow, 0));
    }

    private void deletePasswordMenuItemActionListener() {
        int passwordId = getSelectedPasswordId();

        db.deletePassword(passwordId);

        updateTableModel();
        enableTools();
    }

    private void editPasswordMenuItemActionListener() {
        System.out.println(getSelectedPasswordId());

        enableTools();
    }

    private void newPasswordMenuItemActionListener() {
        EPMNewPasswordDialog addPasswordDialog = new EPMNewPasswordDialog(
            this.frame,
            this
        );

        addPasswordDialog.setVisible(true);
        enableTools();
    }
}
