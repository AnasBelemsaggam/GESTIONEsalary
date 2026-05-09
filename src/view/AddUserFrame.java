package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class AddUserFrame extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public AddUserFrame() {
        setTitle("Register / Add User");
        setSize(560, 470);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 25));
        mainPanel.setBackground(new Color(245, 247, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 45, 30, 45));

        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(24, 31, 45));

        JLabel subtitleLabel = new JLabel("Register as a new user", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
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
                BorderFactory.createEmptyBorder(28, 35, 28, 35)
        ));

        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();

        styleInput(usernameField);
        styleInput(emailField);
        styleInput(passwordField);

        formPanel.add(createFieldPanel("Username", usernameField));
        formPanel.add(Box.createVerticalStrut(14));

        formPanel.add(createFieldPanel("Email", emailField));
        formPanel.add(Box.createVerticalStrut(14));

        formPanel.add(createFieldPanel("Password", passwordField));
        formPanel.add(Box.createVerticalStrut(25));

        JButton saveButton = createPrimaryButton("Create Account");
        formPanel.add(saveButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        saveButton.addActionListener(e -> saveUser());
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(430, 75));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(24, 31, 45));

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private void styleInput(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 15));
        field.setPreferredSize(new Dimension(430, 40));
        field.setMaximumSize(new Dimension(430, 40));
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
        button.setMaximumSize(new Dimension(430, 45));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(38, 49, 68));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void saveUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setRole("USER");

        UserDAO userDAO = new UserDAO();
        userDAO.addUser(user);

        JOptionPane.showMessageDialog(this, "Account created successfully.");

        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }
}