package app;

import javax.swing.SwingUtilities;

import controller.auth_controller;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppTheme.apply();
            auth_controller controller = new auth_controller();
            controller.showLogin();
        });
    }
}
