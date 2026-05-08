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
        setSize(800, 550);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel(
                "Salary History for " + loggedUser.getUsername(),
                SwingConstants.CENTER
        );

        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Amount", "Month", "Year"},
                0
        );

        salaryTable = new JTable(tableModel);
        salaryTable.setRowHeight(25);

        idField = new JTextField();
        idField.setEditable(false);

        amountField = new JTextField();
        monthField = new JTextField();
        yearField = new JTextField();

        JButton refreshButton = new JButton("Refresh");
        JButton updateButton = new JButton("Update Salary");
        JButton deleteButton = new JButton("Delete Salary");

        JPanel formPanel = new JPanel(
                new GridLayout(5, 2, 10, 10)
        );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

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

        JPanel bottomPanel = new JPanel(new BorderLayout());

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

    private void loadSalaries() {

        tableModel.setRowCount(0);

        SalaryDAO salaryDAO = new SalaryDAO();

        List<Salary> salaries =
                salaryDAO.getSalariesByUser(loggedUser.getUserId());

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

            idField.setText(
                    tableModel.getValueAt(selectedRow, 0).toString()
            );

            amountField.setText(
                    tableModel.getValueAt(selectedRow, 1).toString()
            );

            monthField.setText(
                    tableModel.getValueAt(selectedRow, 2).toString()
            );

            yearField.setText(
                    tableModel.getValueAt(selectedRow, 3).toString()
            );
        }
    }

    private void updateSalary() {

        try {

            if (idField.getText().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a salary first."
                );

                return;
            }

            Salary salary = new Salary();

            salary.setSalaryId(
                    Integer.parseInt(idField.getText())
            );

            salary.setUserId(loggedUser.getUserId());

            salary.setAmount(
                    new BigDecimal(amountField.getText())
            );

            salary.setMonth(
                    Integer.parseInt(monthField.getText())
            );

            salary.setYear(
                    Integer.parseInt(yearField.getText())
            );

            SalaryDAO salaryDAO = new SalaryDAO();

            salaryDAO.updateSalary(salary);

            JOptionPane.showMessageDialog(
                    this,
                    "Salary updated successfully."
            );

            loadSalaries();
            clearFields();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error while updating salary."
            );
        }
    }

    private void deleteSalary() {

        try {

            if (idField.getText().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please select a salary first."
                );

                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this salary?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                int salaryId =
                        Integer.parseInt(idField.getText());

                SalaryDAO salaryDAO = new SalaryDAO();

                salaryDAO.deleteSalary(
                        salaryId,
                        loggedUser.getUserId()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Salary deleted successfully."
                );

                loadSalaries();
                clearFields();
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error while deleting salary."
            );
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