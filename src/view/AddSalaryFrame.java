package view;

import dao.SalaryDAO;
import model.Salary;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AddSalaryFrame extends JFrame {

    private User loggedUser;
    private Runnable onDataChanged;

    private JTextField amountField;
    private JTextField monthField;
    private JTextField yearField;

    public AddSalaryFrame(User loggedUser, Runnable onDataChanged) {
        this.loggedUser = loggedUser;
        this.onDataChanged = onDataChanged;

        setTitle("Add Salary - " + loggedUser.getUsername());
        setSize(400, 300);
        setLocationRelativeTo(null);

        amountField = new JTextField();
        monthField = new JTextField(String.valueOf(LocalDate.now().getMonthValue()));
        yearField = new JTextField(String.valueOf(LocalDate.now().getYear()));

        JButton saveButton = new JButton("Save Salary");

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("User:"));
        panel.add(new JLabel(loggedUser.getUsername()));

        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        panel.add(new JLabel("Month:"));
        panel.add(monthField);

        panel.add(new JLabel("Year:"));
        panel.add(yearField);

        panel.add(new JLabel(""));
        panel.add(saveButton);

        add(panel);

        saveButton.addActionListener(e -> saveSalary());
    }

    private void saveSalary() {
        try {
            BigDecimal amount = new BigDecimal(amountField.getText());
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());

            Salary salary = new Salary();
            salary.setUserId(loggedUser.getUserId());
            salary.setAmount(amount);
            salary.setMonth(month);
            salary.setYear(year);

            SalaryDAO salaryDAO = new SalaryDAO();
            salaryDAO.addOrUpdateSalary(salary);

            JOptionPane.showMessageDialog(this, "Salary saved successfully.");

            amountField.setText("");

            if (onDataChanged != null) {
                onDataChanged.run();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check values.");
        }
    }
}