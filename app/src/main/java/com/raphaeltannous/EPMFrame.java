package com.raphaeltannous;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGUtils;

import net.miginfocom.swing.MigLayout;

/**
 * EPMFrame
 */
public class EPMFrame extends JFrame {
    private JMenuBar menuBar;

    private JMenuItem exitMenuItem;
    protected JMenuItem closeMenuItem;
    private JMenuItem aboutMenuItem;

    protected JMenuItem newPasswordMenuItem;
    protected JMenuItem editPasswordMenuItem;
    protected JMenuItem deletePasswordMenuItem;
    protected JMenuItem copyWebsiteMenuItem;
    protected JMenuItem copyUsernameMenuItem;
    protected JMenuItem copyPasswordMenuItem;
    protected JMenuItem showOrUpdateTOTPMenuItem;
    protected JMenuItem showOrUpdateBackupCodesMenuItem;

    protected Container contentPane;

    private boolean createActionListenerInProgress = false;
    private boolean openActionListenerInProgress = false;
    private boolean closeActionListenerInProgress = false;
    private boolean aboutActionListenerInProgress = false;
    private boolean generatePasswordActionListenerInProgress = false;
    private boolean documentationActionListenerInProrgress = false;

    EPMFrame() {
        setIconImages(FlatSVGUtils.createWindowIconImages("/com/raphaeltannous/epm.svg"));

        initFrameComponents();
    }

    private void initFrameComponents() {
        menuBar = new JMenuBar();

        JMenu databaseMenu = new JMenu();
        JMenuItem createMenuItem = new JMenuItem();
        JMenuItem openMenuItem = new JMenuItem();
        closeMenuItem = new JMenuItem();

        JMenu passwordsMenu = new JMenu();
        newPasswordMenuItem = new JMenuItem();
        editPasswordMenuItem = new JMenuItem();
        deletePasswordMenuItem = new JMenuItem();
        copyWebsiteMenuItem = new JMenuItem();
        copyUsernameMenuItem = new JMenuItem();
        copyPasswordMenuItem = new JMenuItem();
        showOrUpdateTOTPMenuItem = new JMenuItem();
        showOrUpdateBackupCodesMenuItem = new JMenuItem();

        JMenu toolsMenu = new JMenu();
        JMenuItem generatePasswordMenuItem = new JMenuItem();

        JMenu helpMenu = new JMenu();
        JMenuItem documentationMenuItem = new JMenuItem();
        aboutMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();

        EPMOpenAndCreatePanel openAndCreatePanel = new EPMOpenAndCreatePanel(this);

        this.setTitle("EPM");
        this.setSize(777, 333);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setMinimumSize(new Dimension(777, 333));
        contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("insets 0, fill"));

        // menuBar
        {

            // fileMenu
            {
                databaseMenu.setText("Database");
                databaseMenu.setMnemonic('D');

                // createMenuItem
                createMenuItem.setText("Create");
                createMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
                createMenuItem.setMnemonic('N');
                createMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/plus.svg"));
                createMenuItem.addActionListener(e -> createActionListener());
                databaseMenu.add(createMenuItem);

                // openMenuItem
                openMenuItem.setText("Open");
                openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
                openMenuItem.setMnemonic('O');
                openMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/folder-plus.svg"));
                openMenuItem.addActionListener(e -> openActionListener());
                databaseMenu.add(openMenuItem);
                databaseMenu.addSeparator();

                // closeMenuItem
                closeMenuItem.setText("Close");
                closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
                closeMenuItem.setMnemonic('C');
                closeMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/x.svg"));
                closeMenuItem.setEnabled(false);
                closeMenuItem.addActionListener(e -> closeActionListener());
                databaseMenu.add(closeMenuItem);
                databaseMenu.addSeparator();

                // exitMenuItem
                exitMenuItem.setText("Exit");
                exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
                exitMenuItem.setMnemonic('X');
                exitMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/door-open.svg"));
                exitMenuItem.addActionListener(e -> exitActionListener());
                databaseMenu.add(exitMenuItem);
            }
            menuBar.add(databaseMenu);

            // passwordsMenu
            {
                passwordsMenu.setText("Passwords");
                passwordsMenu.setMnemonic('P');

                // addPasswordMenuItem
                newPasswordMenuItem.setText("New Password");
                newPasswordMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
                // newPasswordMenuItem.setMnemonic('A');
                // BUG: For some reasons when the letter a is written in the dialog,
                // it will also be taken as a keyboard shortcut and call the dialog
                // once again. (Should be fixed) ?!
                newPasswordMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/plus-square.svg"));
                newPasswordMenuItem.setEnabled(false);
                passwordsMenu.add(newPasswordMenuItem);

                // editPasswordMenuItem
                editPasswordMenuItem.setText("Edit Password");
                editPasswordMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
                editPasswordMenuItem.setMnemonic('E');
                editPasswordMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/pencil-square.svg"));
                editPasswordMenuItem.setEnabled(false);
                passwordsMenu.add(editPasswordMenuItem);
                passwordsMenu.addSeparator();

                // deletePasswordMenuItem
                deletePasswordMenuItem.setText("Delete Password");
                deletePasswordMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
                deletePasswordMenuItem.setMnemonic('D');
                deletePasswordMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/x-square.svg"));
                deletePasswordMenuItem.setEnabled(false);
                passwordsMenu.add(deletePasswordMenuItem);
                passwordsMenu.addSeparator();

                // copyWebsiteMenuItem
                copyWebsiteMenuItem.setText("Copy Website");
                copyWebsiteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK));
                copyWebsiteMenuItem.setMnemonic('W');
                copyWebsiteMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/website-copy.svg"));
                copyWebsiteMenuItem.setEnabled(false);
                passwordsMenu.add(copyWebsiteMenuItem);

                // copyUsernameMenuItem
                copyUsernameMenuItem.setText("Copy Username");
                copyUsernameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_DOWN_MASK));
                copyUsernameMenuItem.setMnemonic('U');
                copyUsernameMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/user-copy.svg"));
                copyUsernameMenuItem.setEnabled(false);
                passwordsMenu.add(copyUsernameMenuItem);

                // copyPasswordMenuItem
                copyPasswordMenuItem.setText("Copy Password");
                copyPasswordMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
                copyPasswordMenuItem.setMnemonic('P');
                copyPasswordMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/key-copy.svg"));
                copyPasswordMenuItem.setEnabled(false);
                passwordsMenu.add(copyPasswordMenuItem);
                passwordsMenu.addSeparator();

                // showOrUpdateTOTPMenuItem
                showOrUpdateTOTPMenuItem.setText("Show Or Update TOTP");
                showOrUpdateTOTPMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
                showOrUpdateTOTPMenuItem.setMnemonic('T');
                showOrUpdateTOTPMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/clock-history.svg"));
                showOrUpdateTOTPMenuItem.setEnabled(false);
                passwordsMenu.add(showOrUpdateTOTPMenuItem);
                passwordsMenu.addSeparator();

                // showOrUpdateBackupCodesMenuItem
                showOrUpdateBackupCodesMenuItem.setText("Show Or Update Backup Codes");
                showOrUpdateBackupCodesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
                showOrUpdateBackupCodesMenuItem.setMnemonic('B');
                showOrUpdateBackupCodesMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/code-square.svg"));
                showOrUpdateBackupCodesMenuItem.setEnabled(false);
                passwordsMenu.add(showOrUpdateBackupCodesMenuItem);
            }
            menuBar.add(passwordsMenu);

            // toolsMenu
            {
                toolsMenu.setText("Tools");
                toolsMenu.setMnemonic('T');

                // generatePasswordMenuItem
                generatePasswordMenuItem.setText("Password Generator");
                generatePasswordMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
                generatePasswordMenuItem.setMnemonic('G');
                generatePasswordMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/dice-5.svg"));
                generatePasswordMenuItem.addActionListener(e -> generatePasswordMenuItemActionListener(null));
                toolsMenu.add(generatePasswordMenuItem);
            }
            menuBar.add(toolsMenu);

            // helpMenu
            {
                helpMenu.setText("Help");
                helpMenu.setMnemonic('H');

                // documentationMenuItem
                documentationMenuItem.setText("Documentation");
                documentationMenuItem.setMnemonic('D');
                documentationMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/question.svg"));
                documentationMenuItem.addActionListener(e -> documentationActionListener());
                helpMenu.add(documentationMenuItem);

                // aboutMenuItem
                aboutMenuItem.setText("About");
                aboutMenuItem.setMnemonic('A');
                aboutMenuItem.setIcon(new FlatSVGIcon("com/raphaeltannous/icons/info-circle.svg"));
                aboutMenuItem.addActionListener(e -> aboutActionListener());
                helpMenu.add(aboutMenuItem);
            }
            menuBar.add(helpMenu);
        }
        setJMenuBar(menuBar);

        contentPane.add(openAndCreatePanel, "align center");
    }

    private void documentationActionListener() {
        if (documentationActionListenerInProrgress) {
            return;
        }

        documentationActionListenerInProrgress = true;

        openInBrowser("https://github.com/rofe33/java-password-manager");

        SwingUtilities.invokeLater(() -> documentationActionListenerInProrgress = false);
    }

    protected void generatePasswordMenuItemActionListener(JPasswordField passwordField) {
        if (generatePasswordActionListenerInProgress) {
            return;
        }

        generatePasswordActionListenerInProgress = true;

        EPMPasswordGeneratorDialog passwordGeneratorDialog = new EPMPasswordGeneratorDialog(
            this,
            passwordField
        );

        passwordGeneratorDialog.setVisible(true);

        SwingUtilities.invokeLater(() -> generatePasswordActionListenerInProgress = false);
    }

    private void openInBrowser(String urlString) {
        Desktop desktop = Desktop.getDesktop();
        String urlLocation = urlString;

        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
				URI url = new URI(urlLocation);

                desktop.browse(url);
			} catch (URISyntaxException e) {
				e.printStackTrace(System.err);
			} catch (IOException e) {
				e.printStackTrace(System.err);
            }
        }
    }

    private void aboutActionListener() {
        if (aboutActionListenerInProgress) {
            return;
        }

        aboutActionListenerInProgress = true;

        JLabel titleLabel = new JLabel("Encrypted Password Manager");
        titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h1");

        String link = "https://github.com/rofe33/java-password-manager";
        JLabel linkLabel = new JLabel("<html><a href=\"#\">" + link + "</a></html>");
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openInBrowser("https://github.com/rofe33/java-password-manager");
            }
        });

        JOptionPane.showMessageDialog(
            this,
            new Object[] {
                titleLabel,
                "To manage your passwords securely.",
                " ",
                "Licensed under The GPL-3.0 License.",
                "Source code available at: ",
                linkLabel,
                " ",
                "Created with ❤️ By Dina Houmani and Raphael Tannous."
            },
            "About",
            JOptionPane.PLAIN_MESSAGE
        );

        SwingUtilities.invokeLater(() -> aboutActionListenerInProgress = false);
    }

    private void exitActionListener() {
        dispose();
    }

    private void closeActionListener() {
        if (closeActionListenerInProgress) {
            return;
        }

        closeActionListenerInProgress = true;

        EPMOpenAndCreatePanel openAndCreatePanel = new EPMOpenAndCreatePanel(this);
        contentPane.removeAll();
        contentPane.add(openAndCreatePanel, "align center");
        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();

        SwingUtilities.invokeLater(() -> closeActionListenerInProgress = false);
    }

    protected void openActionListener() {
        if (openActionListenerInProgress) {
            return;
        }

        openActionListenerInProgress = true;

        EPMOpenPanel openPanel = new EPMOpenPanel(this);
        contentPane.removeAll();
        contentPane.add(openPanel, "align center");
        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();

        SwingUtilities.invokeLater(() -> openActionListenerInProgress = false);
    }

    protected void createActionListener() {
        if (createActionListenerInProgress) {
            return;
        }

        createActionListenerInProgress = true;

        EPMCreatePanel createPanel = new EPMCreatePanel(this);
        contentPane.removeAll();
        contentPane.add(createPanel, "align center");
        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();

        SwingUtilities.invokeLater(() -> createActionListenerInProgress = false);
    }
}
