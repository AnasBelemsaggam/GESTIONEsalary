package view;

import dao.MonthlyReportDAO;
import model.MonthlyReport;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class MonthlyReportFrame extends JFrame {

    private User loggedUser;

    private JTextField monthField;
    private JTextField yearField;
    private JTextArea resultArea;

    public MonthlyReportFrame(User loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Monthly Report - " + loggedUser.getUsername());
        setSize(500, 400);
        setLocationRelativeTo(null);

        monthField = new JTextField(String.valueOf(LocalDate.now().getMonthValue()));
        yearField = new JTextField(String.valueOf(LocalDate.now().getYear()));
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        JButton searchButton = new JButton("Show Report");

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        inputPanel.add(new JLabel("User:"));
        inputPanel.add(new JLabel(loggedUser.getUsername()));

        inputPanel.add(new JLabel("Month:"));
        inputPanel.add(monthField);

        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);

        inputPanel.add(new JLabel(""));
        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        searchButton.addActionListener(e -> showReport());
    }

    private void showReport() {
        try {
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());

            MonthlyReportDAO reportDAO = new MonthlyReportDAO();
            MonthlyReport report = reportDAO.getMonthlySummary(
                    loggedUser.getUserId(),
                    month,
                    year
            );

            if (report != null) {
                resultArea.setText(
                        "===== MONTHLY REPORT =====\n\n" +
                                "User: " + report.getUsername() + "\n" +
                                "Month: " + report.getMonth() + "\n" +
                                "Year: " + report.getYear() + "\n\n" +
                                "Salary: " + report.getSalary() + " DH\n" +
                                "Total Expenses: " + report.getTotalExpenses() + " DH\n" +
                                "Remaining Money: " + report.getRemaining() + " DH"
                );
            } else {
                resultArea.setText("No report found for this month.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input.");
        }
    }
}