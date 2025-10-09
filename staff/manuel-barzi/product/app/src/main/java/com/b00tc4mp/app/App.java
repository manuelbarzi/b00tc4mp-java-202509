
package com.b00tc4mp.app;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    // Simple in-memory user store
    private String registeredUser = null;
    private String registeredPass = null;

    public App() {
        setTitle("App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Panels
        JPanel registerPanel = createRegisterPanel();
        JPanel loginPanel = createLoginPanel();
        JPanel homePanel = createHomePanel();

        cards.add(registerPanel, "register");
        cards.add(loginPanel, "login");
        cards.add(homePanel, "home");

        add(cards);
        cardLayout.show(cards, "register");
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setBackground(Color.CYAN);
        title.setOpaque(true);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        JTextField userField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        JButton registerBtn = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        JButton toLoginBtn = new JButton("Go to Login");
        gbc.gridy = 4;
        panel.add(toLoginBtn, gbc);

        JLabel message = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 5;
        panel.add(message, gbc);

        registerBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());
            
            if (user.isEmpty() || pass.isEmpty()) {
                message.setText("Please fill all fields.");
            } else {
                registeredUser = user;
                registeredPass = pass;

                message.setText("Registered! Go to login.");
            }
        });

        toLoginBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
            message.setText("");

            cardLayout.show(cards, "login");
        });

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        JTextField userField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        JButton toRegisterBtn = new JButton("Go to Register");
        gbc.gridy = 4;
        panel.add(toRegisterBtn, gbc);

        JLabel message = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 5;
        panel.add(message, gbc);

        loginBtn.addActionListener(e -> {
            String user = userField.getText().trim();
            String pass = new String(passField.getPassword());

            if (user.equals(registeredUser) && pass.equals(registeredPass)) {
                message.setText("");

                // TODO call https://zenquotes.io/api/today and display quote on home panel
                cardLayout.show(cards, "home");
            } else {
                message.setText("Invalid credentials.");
            }
        });

        toRegisterBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
            message.setText("");

            cardLayout.show(cards, "register");
        });

        return panel;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel welcome = new JLabel("Welcome Home!", SwingConstants.CENTER);
        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 20f));
        panel.add(welcome, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Logout");
        panel.add(logoutBtn, BorderLayout.SOUTH);
        logoutBtn.addActionListener(e -> cardLayout.show(cards, "login"));

        return panel;
    }
}
