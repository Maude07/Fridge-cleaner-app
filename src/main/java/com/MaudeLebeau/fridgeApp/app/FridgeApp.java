package com.MaudeLebeau.fridgeApp.app;

import com.MaudeLebeau.fridgeApp.repository.SchemaInitializer;
import com.MaudeLebeau.fridgeApp.ui.MainFrame;

import javax.swing.*;
import javax.xml.validation.Schema;

public class FridgeApp {
    public static void main(String[] args) {
        SchemaInitializer.init();

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
