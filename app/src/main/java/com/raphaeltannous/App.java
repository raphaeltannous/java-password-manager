package com.raphaeltannous;

import com.formdev.flatlaf.FlatLightLaf;

public class App {

    public static void main(String[] args) {
        FlatLightLaf.setup();

        EPMFrame frame = new EPMFrame();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
