package view;

import dao.MonthlyReportDAO;
import model.MonthlyReport;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardPanel extends JPanel {

    private User loggedUser;

    public DashboardPanel(User loggedUser) {
        this.loggedUser = loggedUser;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        MonthlyReportDAO reportDAO = new MonthlyReportDAO();
        MonthlyReport report = reportDAO.getMonthlySummary(
                loggedUser.getUserId(),
                month,
                year
        );

        BigDecimal salary = BigDecimal.ZERO;
        BigDecimal expenses = BigDecimal.ZERO;
        BigDecimal remaining = BigDecimal.ZERO;

        if (report != null) {
            salary = report.getSalary();
            expenses = report.getTotalExpenses();
            remaining = report.getRemaining();
        }

        JLabel title = new JLabel(
                "Dashboard - " + loggedUser.getUsername(),
                SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitle = new JLabel(
                "Current Month: " + month + "/" + year,
                SwingConstants.CENTER
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(title);
        headerPanel.add(subtitle);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        cardsPanel.setBackground(Color.WHITE);

        cardsPanel.add(createCard("Salary", salary + " DH"));
        cardsPanel.add(createCard("Expenses", expenses + " DH"));
        cardsPanel.add(createCard("Remaining", remaining + " DH"));

        add(headerPanel, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(240, 245, 255));
        card.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 26));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
}