package view;

import dao.SalaryDAO;
import model.Salary;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class SalaryHistoryFrame extends JFrame {

    private User loggedUser;

    private JTable salaryTable;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JTextField amountField;
    private JTextField monthField;
    private JTextField yearField;

    public SalaryHistoryFrame(User loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Salary History - " + loggedUser.getUsername());
        setSize(850, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel(
                "Salary History for " + loggedUser.getUsername(),
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(24, 31, 45));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Amount", "Month", "Year"},
                0
        );

        salaryTable = new JTable(tableModel);
        salaryTable.setRowHeight(30);
        salaryTable.setFont(new Font("Arial", Font.PLAIN, 14));
        salaryTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        salaryTable.getTableHeader().setBackground(new Color(38, 49, 68));
        salaryTable.getTableHeader().setForeground(Color.WHITE);
        salaryTable.setSelectionBackground(new Color(220, 235, 255));
        salaryTable.setSelectionForeground(Color.BLACK);
        salaryTable.setGridColor(new Color(230, 230, 230));

        idField = new JTextField();
        idField.setEditable(false);

        amountField = new JTextField();
        monthField = new JTextField();
        yearField = new JTextField();

        styleField(idField);
        styleField(amountField);
        styleField(monthField);
        styleField(yearField);

        JButton refreshButton = createButton("Refresh");
        JButton updateButton = createButton("Update Salary");
        JButton deleteButton = createButton("Delete Salary");

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 12, 12));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        formPanel.add(new JLabel("Salary ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);

        formPanel.add(new JLabel("Month:"));
        formPanel.add(monthField);

        formPanel.add(new JLabel("Year:"));
        formPanel.add(yearField);

        formPanel.add(refreshButton);
        formPanel.add(updateButton);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(245, 247, 250));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(deleteButton, BorderLayout.SOUTH);

        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(salaryTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        salaryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromTable();
            }
        });

        refreshButton.addActionListener(e -> loadSalaries());
        updateButton.addActionListener(e -> updateSalary());
        deleteButton.addActionListener(e -> deleteSalary());

        loadSalaries();
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(38, 49, 68));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 215, 220)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void loadSalaries() {
        tableModel.setRowCount(0);

        SalaryDAO salaryDAO = new SalaryDAO();
        List<Salary> salaries = salaryDAO.getSalariesByUser(loggedUser.getUserId());

        for (Salary s : salaries) {
            tableModel.addRow(new Object[]{
                    s.getSalaryId(),
                    s.getAmount(),
                    s.getMonth(),
                    s.getYear()
            });
        }
    }

    private void fillFormFromTable() {
        int selectedRow = salaryTable.getSelectedRow();

        if (selectedRow >= 0) {
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            amountField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            monthField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            yearField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        }
    }

    private void updateSalary() {
        try {
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a salary first.");
                return;
            }

            Salary salary = new Salary();

            salary.setSalaryId(Integer.parseInt(idField.getText()));
            salary.setUserId(loggedUser.getUserId());
            salary.setAmount(new BigDecimal(amountField.getText()));
            salary.setMonth(Integer.parseInt(monthField.getText()));
            salary.setYear(Integer.parseInt(yearField.getText()));

            SalaryDAO salaryDAO = new SalaryDAO();
            salaryDAO.updateSalary(salary);

            JOptionPane.showMessageDialog(this, "Salary updated successfully.");

            loadSalaries();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while updating salary.");
        }
    }

    private void deleteSalary() {
        try {
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a salary first.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this salary?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int salaryId = Integer.parseInt(idField.getText());

                SalaryDAO salaryDAO = new SalaryDAO();
                salaryDAO.deleteSalary(
                        salaryId,
                        loggedUser.getUserId()
                );

                JOptionPane.showMessageDialog(this, "Salary deleted successfully.");

                loadSalaries();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while deleting salary.");
        }
    }

    private void clearFields() {
        idField.setText("");
        amountField.setText("");
        monthField.setText("");
        yearField.setText("");
        salaryTable.clearSelection();
    }
}