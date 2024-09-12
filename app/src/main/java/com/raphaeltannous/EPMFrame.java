package com.raphaeltannous;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

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
        // TODO: setIconImages FlatSVGUtils

        initFrameComponents();
    }

    private void initFrameComponents() {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu();
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
        contentPane.setLayout(new GridBagLayout());

        // menuBar
        {

            // fileMenu
            {
                fileMenu.setText("File");
                fileMenu.setMnemonic('F');

                // createMenuItem
                createMenuItem.setText("Create");
                createMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
                createMenuItem.setMnemonic('N');
                createMenuItem.addActionListener(e -> createActionListener());
                fileMenu.add(createMenuItem);

                // openMenuItem
                openMenuItem.setText("Open");
                openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
                openMenuItem.setMnemonic('O');
                openMenuItem.addActionListener(e -> openActionListener());
                fileMenu.add(openMenuItem);
                fileMenu.addSeparator();

                // closeMenuItem
                closeMenuItem.setText("Close");
                closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
                closeMenuItem.setMnemonic('C');
                closeMenuItem.setEnabled(false);
                closeMenuItem.addActionListener(e -> closeActionListener());
                fileMenu.add(closeMenuItem);
                fileMenu.addSeparator();

                // exitMenuItem
                exitMenuItem.setText("Exit");
                exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
                exitMenuItem.setMnemonic('X');
                exitMenuItem.addActionListener(e -> exitActionListener());
                fileMenu.add(exitMenuItem);
            }
            menuBar.add(fileMenu);


            // helpMenu
            {
                helpMenu.setText("Help");
                helpMenu.setMnemonic('H');

                // documentationMenuItem
                documentationMenuItem.setText("Documentation");
                documentationMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
                documentationMenuItem.setMnemonic('D');
                documentationMenuItem.addActionListener(e -> documentationActionListener());
                helpMenu.add(documentationMenuItem);

                // aboutMenuItem
                aboutMenuItem.setText("About");
                aboutMenuItem.setMnemonic('A');
                aboutMenuItem.addActionListener(e -> aboutActionListener());
                helpMenu.add(aboutMenuItem);
            }
            menuBar.add(helpMenu);
        }
        setJMenuBar(menuBar);

        contentPane.add(openAndCreatePanel);
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
        contentPane.add(openAndCreatePanel);
        contentPane.revalidate();
        contentPane.repaint();
	}

	private void openActionListener() {
        EPMOpenPanel openPanel = new EPMOpenPanel(this);
        contentPane.removeAll();
        contentPane.add(openPanel);
        contentPane.revalidate();
        contentPane.repaint();
	}

	private void createActionListener() {
        EPMCreatePanel createPanel = new EPMCreatePanel(this);
        contentPane.removeAll();
        contentPane.add(createPanel);
        contentPane.revalidate();
        contentPane.repaint();
	}
}
