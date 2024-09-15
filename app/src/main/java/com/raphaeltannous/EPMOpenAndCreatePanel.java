package com.raphaeltannous;

import javax.swing.JButton;
import javax.swing.JPanel;

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
        frame.newPasswordMenuItem.setEnabled(false);

        JButton createButton = new JButton("Create");
        JButton openButton = new JButton("Open");

        this.setLayout(new MigLayout());

        createButton.setText("Create");
        createButton.setMnemonic('C');
        createButton.setDisplayedMnemonicIndex(0);
        createButton.addActionListener(e -> frame.createActionListener());
        add(createButton, "cell 0 0");

        openButton.setText("Open");
        openButton.setMnemonic('O');
        openButton.setDisplayedMnemonicIndex(0);
        openButton.addActionListener(e -> frame.openActionListener());
        add(openButton, "cell 1 0");
    }
}
