package com.raphaeltannous;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.SystemInfo;

public class EPM {

    public static void main(String[] args) {
        FlatLightLaf.setup();

        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }

        EPMFrame frame = new EPMFrame();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
