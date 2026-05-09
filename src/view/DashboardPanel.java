package view;

import dao.ExpenseDAO;
import dao.MonthlyReportDAO;
import model.MonthlyReport;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class DashboardPanel extends JPanel {

    private User loggedUser;

    public DashboardPanel(User loggedUser) {
        this.loggedUser = loggedUser;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));

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

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(245, 247, 250));

        JLabel welcomeLabel = new JLabel(
                "Welcome back, " + loggedUser.getUsername() + "!"
        );
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(24, 31, 45));

        JLabel subtitleLabel = new JLabel(
                "Financial overview for " + month + "/" + year
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 17));
        subtitleLabel.setForeground(new Color(100, 110, 125));

        headerPanel.add(welcomeLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        if (remaining.compareTo(BigDecimal.ZERO) < 0) {

            JPanel warningPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            warningPanel.setBackground(new Color(255, 235, 235));
            warningPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(231, 76, 60), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));

            JLabel warningLabel = new JLabel(
                    "⚠ Warning: Your expenses are higher than your salary this month."
            );

            warningLabel.setFont(new Font("Arial", Font.BOLD, 14));
            warningLabel.setForeground(new Color(192, 57, 43));

            warningPanel.add(warningLabel);

            headerPanel.add(Box.createVerticalStrut(15));
            headerPanel.add(warningPanel);
        }

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 25, 25));
        cardsPanel.setBackground(new Color(245, 247, 250));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 20, 0));

        cardsPanel.add(createCard(
                "Salary",
                salary + " DH",
                new Color(52, 152, 219)
        ));

        cardsPanel.add(createCard(
                "Expenses",
                expenses + " DH",
                new Color(231, 76, 60)
        ));

        Color remainingColor;

        if (remaining.compareTo(BigDecimal.ZERO) < 0) {
            remainingColor = new Color(192, 57, 43);
        } else {
            remainingColor = new Color(46, 204, 113);
        }

        cardsPanel.add(createCard(
                "Remaining",
                remaining + " DH",
                remainingColor
        ));

        ExpenseDAO expenseDAO = new ExpenseDAO();

        Map<String, BigDecimal> stats =
                expenseDAO.getSpendingByCategory(
                        loggedUser.getUserId(),
                        month,
                        year
                );

        PieChartPanel pieChartPanel = new PieChartPanel(stats);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(245, 247, 250));

        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(pieChartPanel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String value, Color accentColor) {

        JPanel card = new JPanel(new BorderLayout(10, 10));

        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(90, 100, 115));

        JLabel valueLabel = new JLabel(value);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 30));
        valueLabel.setForeground(accentColor);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private static class PieChartPanel extends JPanel {

        private Map<String, BigDecimal> data;

        private final Color[] colors = {
                new Color(52, 152, 219),
                new Color(231, 76, 60),
                new Color(46, 204, 113),
                new Color(241, 196, 15),
                new Color(155, 89, 182),
                new Color(230, 126, 34),
                new Color(26, 188, 156)
        };

        public PieChartPanel(Map<String, BigDecimal> data) {

            this.data = data;

            setBackground(Color.WHITE);

            setPreferredSize(new Dimension(800, 330));

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(225, 230, 235), 1),
                    BorderFactory.createEmptyBorder(15, 25, 15, 25)
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();

            g2.setFont(new Font("Arial", Font.BOLD, 24));
            g2.setColor(new Color(24, 31, 45));

            g2.drawString("Spending by Category", 30, 40);

            if (data == null || data.isEmpty()) {

                g2.setFont(new Font("Arial", Font.PLAIN, 16));

                g2.drawString(
                        "No expenses for this month.",
                        30,
                        85
                );

                return;
            }

            BigDecimal total = BigDecimal.ZERO;

            for (BigDecimal value : data.values()) {
                total = total.add(value);
            }

            int pieSize = 230;

            int pieX = 90;
            int pieY = 75;

            int legendX = width / 2 + 40;
            int legendY = 105;

            int startAngle = 0;

            int index = 0;

            for (Map.Entry<String, BigDecimal> entry : data.entrySet()) {

                double value = entry.getValue().doubleValue();

                double totalValue = total.doubleValue();

                int angle =
                        (int) Math.round(value * 360 / totalValue);

                Color color =
                        colors[index % colors.length];

                g2.setColor(color);

                g2.fillArc(
                        pieX,
                        pieY,
                        pieSize,
                        pieSize,
                        startAngle,
                        angle
                );

                double percent =
                        value * 100 / totalValue;

                g2.fillRect(
                        legendX,
                        legendY + index * 35,
                        18,
                        18
                );

                g2.setColor(new Color(40, 40, 40));

                g2.setFont(new Font("Arial", Font.PLAIN, 15));

                g2.drawString(
                        entry.getKey()
                                + " - "
                                + entry.getValue()
                                + " DH ("
                                + String.format("%.1f", percent)
                                + "%)",

                        legendX + 30,
                        legendY + 15 + index * 35
                );

                startAngle += angle;

                index++;
            }
        }
    }
}