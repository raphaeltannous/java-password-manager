package com.raphaeltannous;

import java.awt.Container;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

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
    protected Container contentPane;

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

        JMenu helpMenu = new JMenu();
        JMenuItem documentationMenuItem = new JMenuItem();
        aboutMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();

        EPMOpenAndCreatePanel openAndCreatePanel = new EPMOpenAndCreatePanel(this);

        this.setTitle("EPM");
        this.setSize(777, 333);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
                createMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
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


            // helpMenu
            {
                helpMenu.setText("Help");
                helpMenu.setMnemonic('H');

                // documentationMenuItem
                documentationMenuItem.setText("Documentation");
                documentationMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
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

	private Object aboutActionListener() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'aboutActionListener'");
	}

	private Object documentationActionListener() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'documentationActionListener'");
	}

	private void exitActionListener() {
        dispose();
	}

	private void closeActionListener() {
        EPMOpenAndCreatePanel openAndCreatePanel = new EPMOpenAndCreatePanel(this);
        contentPane.removeAll();
        contentPane.add(openAndCreatePanel, "align center");
        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
	}

	private void openActionListener() {
        EPMOpenPanel openPanel = new EPMOpenPanel(this);
        contentPane.removeAll();
        contentPane.add(openPanel, "align center");
        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
	}

	private void createActionListener() {
        EPMCreatePanel createPanel = new EPMCreatePanel(this);
        contentPane.removeAll();
        contentPane.add(createPanel, "align center");
        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
	}
}
