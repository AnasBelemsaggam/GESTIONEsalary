package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("Personal Finance Tracker - Login");
        setSize(560, 470);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 25));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 45, 30, 45));

        JLabel titleLabel = new JLabel("Welcome Back", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(new Color(24, 31, 45));

        JLabel subtitleLabel = new JLabel("Login to manage your finances", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        subtitleLabel.setForeground(new Color(100, 110, 125));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        headerPanel.setBackground(new Color(245, 247, 250));
        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1),
                BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));

        emailField = new JTextField();
        passwordField = new JPasswordField();

        styleInput(emailField);
        styleInput(passwordField);

        formPanel.add(createFieldPanel("Email", emailField));
        formPanel.add(Box.createVerticalStrut(18));
        formPanel.add(createFieldPanel("Password", passwordField));
        formPanel.add(Box.createVerticalStrut(30));

        JButton loginButton = createPrimaryButton("Login");
        JButton registerButton = createSecondaryButton("Register");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 18, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(430, 48));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        formPanel.add(buttonPanel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        loginButton.addActionListener(e -> login());

        registerButton.addActionListener(e -> {
            AddUserFrame addUserFrame = new AddUserFrame();
            addUserFrame.setVisible(true);
        });
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(430, 80));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(new Color(24, 31, 45));

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private void styleInput(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(430, 42));
        field.setMaximumSize(new Dimension(430, 42));
        field.setMinimumSize(new Dimension(430, 42));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 198, 210), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(38, 49, 68));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(new Color(38, 49, 68));
        button.setBackground(new Color(230, 235, 245));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByEmail(email);

        if (user != null && user.getPasswordHash().equals(password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");

            MainFrame mainFrame = new MainFrame(user);
            mainFrame.setVisible(true);

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.");
        }
    }
}