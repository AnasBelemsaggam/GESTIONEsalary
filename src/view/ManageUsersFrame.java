package view;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageUsersFrame extends JFrame {

    private JTable userTable;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField passwordField;
    private JComboBox<String> roleComboBox;

    public ManageUsersFrame() {
        setTitle("Manage Users");
        setSize(900, 560);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Username", "Email", "Password", "Role"},
                0
        );

        userTable = new JTable(tableModel);
        userTable.setRowHeight(25);

        idField = new JTextField();
        idField.setEditable(false);

        usernameField = new JTextField();
        emailField = new JTextField();
        passwordField = new JTextField();
        roleComboBox = new JComboBox<>(new String[]{"USER", "ADMIN"});

        JButton refreshButton = new JButton("Refresh");
        JButton updateButton = new JButton("Update User");
        JButton deleteButton = new JButton("Delete User");

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("User ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleComboBox);

        formPanel.add(refreshButton);
        formPanel.add(updateButton);

        formPanel.add(new JLabel(""));
        formPanel.add(deleteButton);

        add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);

        loadUsers();

        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromTable();
            }
        });

        refreshButton.addActionListener(e -> loadUsers());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
    }

    private void loadUsers() {
        tableModel.setRowCount(0);

        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllUsers();

        for (User user : users) {
            tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPasswordHash(),
                    user.getRole()
            });
        }
    }

    private void fillFormFromTable() {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow >= 0) {
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            usernameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            passwordField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            roleComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
        }
    }

    private void updateUser() {
        try {
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a user first.");
                return;
            }

            User user = new User();

            user.setUserId(Integer.parseInt(idField.getText()));
            user.setUsername(usernameField.getText());
            user.setEmail(emailField.getText());
            user.setPasswordHash(passwordField.getText());
            user.setRole(roleComboBox.getSelectedItem().toString());

            UserDAO userDAO = new UserDAO();
            userDAO.updateUser(user);

            JOptionPane.showMessageDialog(this, "User updated successfully.");

            loadUsers();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while updating user.");
        }
    }

    private void deleteUser() {
        try {
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a user first.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this user?\nAll salaries, expenses and reports of this user will also be deleted.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int userId = Integer.parseInt(idField.getText());

                UserDAO userDAO = new UserDAO();
                userDAO.deleteUser(userId);

                JOptionPane.showMessageDialog(this, "User deleted successfully.");

                loadUsers();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while deleting user.");
        }
    }

    private void clearFields() {
        idField.setText("");
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        userTable.clearSelection();
    }
}