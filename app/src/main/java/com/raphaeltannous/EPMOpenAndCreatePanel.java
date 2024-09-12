package com.raphaeltannous;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatLaf;

import net.miginfocom.swing.MigLayout;

/**
 * EPMOpenandCreatePanel
 */
public class EPMOpenAndCreatePanel extends JPanel {
    private EPMFrame frame;

    EPMOpenAndCreatePanel(
        EPMFrame frame
    ) {
        this.frame = frame;

        initPanelComponents();
    }

    private void initPanelComponents() {
        // Disabling closeMenuItem
        frame.closeMenuItem.setEnabled(false);

        JButton createButton = new JButton("Create");
        JButton openButton = new JButton("Open");

        this.setLayout(new MigLayout());

        createButton.setText("Create");
        createButton.setMnemonic('N');
        createButton.setDisplayedMnemonicIndex(0);
        createButton.addActionListener(e -> createActionListener());
        add(createButton, "cell 0 0");

        openButton.setText("Open");
        createButton.setMnemonic('O');
        openButton.setDisplayedMnemonicIndex(0);
        openButton.addActionListener(e -> openActionListener());
        add(openButton, "cell 1 0");
    }

	private void openActionListener() {
        EPMOpenPanel openPanel = new EPMOpenPanel(this.frame);
        frame.contentPane.removeAll();
        frame.contentPane.add(openPanel);
        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
	}

	private void createActionListener() {
        EPMCreatePanel createPanel = new EPMCreatePanel(this.frame);
        frame.contentPane.removeAll();
        frame.contentPane.add(createPanel);
        FlatLaf.revalidateAndRepaintAllFramesAndDialogs();
	}
}
