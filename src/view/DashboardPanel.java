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
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));

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

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(new Color(245, 247, 250));

        JLabel welcomeLabel = new JLabel(
                "Welcome back, " + loggedUser.getUsername() + "!",
                SwingConstants.LEFT
        );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(24, 31, 45));

        JLabel subtitleLabel = new JLabel(
                "Financial overview for " + month + "/" + year,
                SwingConstants.LEFT
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        subtitleLabel.setForeground(new Color(100, 110, 125));

        headerPanel.add(welcomeLabel);
        headerPanel.add(subtitleLabel);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 25, 25));
        cardsPanel.setBackground(new Color(245, 247, 250));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(45, 0, 45, 0));

        cardsPanel.add(createCard("Salary", salary + " DH", new Color(52, 152, 219)));
        cardsPanel.add(createCard("Expenses", expenses + " DH", new Color(231, 76, 60)));
        cardsPanel.add(createCard("Remaining", remaining + " DH", new Color(46, 204, 113)));

        JLabel footer = new JLabel(
                "Manage your salary, expenses and reports from the sidebar.",
                SwingConstants.CENTER
        );
        footer.setFont(new Font("Arial", Font.PLAIN, 16));
        footer.setForeground(new Color(120, 120, 120));

        add(headerPanel, BorderLayout.NORTH);
        add(cardsPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1),
                BorderFactory.createEmptyBorder(30, 25, 30, 25)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(90, 100, 115));

        JLabel valueLabel = new JLabel(value, SwingConstants.LEFT);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 30));
        valueLabel.setForeground(accentColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }
}