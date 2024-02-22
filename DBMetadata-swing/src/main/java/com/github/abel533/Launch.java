package com.github.abel533;

import com.formdev.flatlaf.FlatDarkLaf;
import com.github.abel533.controller.LoginController;
import com.github.abel533.utils.I18n;
import com.github.abel533.view.LoginFrame;

import javax.swing.*;

public class Launch {

    private static void initClass() {
        try {
            Class.forName(I18n.class.getCanonicalName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        initClass();
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            // 绑定控制层并初始化
            new LoginController(loginFrame).initView().initAction();
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
        });
    }
}