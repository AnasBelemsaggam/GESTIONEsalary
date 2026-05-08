package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordFrame extends JFrame {

    private User loggedUser;

    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ChangePasswordFrame(User loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Change Password - " + loggedUser.getUsername());
        setSize(450, 300);
        setLocationRelativeTo(null);

        oldPasswordField = new JPasswordField();
        newPasswordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        JButton changeButton = new JButton("Change Password");

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("User:"));
        panel.add(new JLabel(loggedUser.getUsername()));

        panel.add(new JLabel("Old Password:"));
        panel.add(oldPasswordField);

        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);

        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);

        panel.add(new JLabel(""));
        panel.add(changeButton);

        add(panel);

        changeButton.addActionListener(e -> changePassword());
    }

    private void changePassword() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New password and confirmation do not match.");
            return;
        }

        UserDAO userDAO = new UserDAO();
        boolean updated = userDAO.updatePassword(
                loggedUser.getUserId(),
                oldPassword,
                newPassword
        );

        if (updated) {
            loggedUser.setPasswordHash(newPassword);

            JOptionPane.showMessageDialog(this, "Password changed successfully.");

            oldPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Old password is incorrect.");
        }
    }
}