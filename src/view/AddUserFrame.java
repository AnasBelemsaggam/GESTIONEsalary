package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class AddUserFrame extends JFrame {

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public AddUserFrame() {
        setTitle("Add User");
        setSize(450, 330);
        setLocationRelativeTo(null);

        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JPasswordField();
        roleComboBox = new JComboBox<>(new String[]{"USER", "ADMIN"});

        JButton saveButton = new JButton("Save User");

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleComboBox);

        panel.add(new JLabel(""));
        panel.add(saveButton);

        add(panel);

        saveButton.addActionListener(e -> saveUser());
    }

    private void saveUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = roleComboBox.getSelectedItem().toString();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(password);
        user.setRole(role);

        UserDAO userDAO = new UserDAO();
        userDAO.addUser(user);

        JOptionPane.showMessageDialog(this, "User added successfully.");

        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }
}