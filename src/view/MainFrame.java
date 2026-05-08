package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private User loggedUser;
    private JPanel contentPanel;

    private final Color SIDEBAR_COLOR = new Color(24, 31, 45);
    private final Color BUTTON_COLOR = new Color(38, 49, 68);
    private final Color BUTTON_HOVER_COLOR = new Color(52, 73, 94);
    private final Color TEXT_COLOR = Color.WHITE;

    public MainFrame(User loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Personal Finance Tracker - " + loggedUser.getRole());
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));

        refreshDashboard();

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void refreshDashboard() {
        contentPanel.removeAll();
        contentPanel.add(new DashboardPanel(loggedUser), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(240, 650));
        sidebar.setBackground(SIDEBAR_COLOR);

        JLabel appTitle = new JLabel(
                "<html><center>Personal<br>Finance Tracker</center></html>",
                SwingConstants.CENTER
        );
        appTitle.setFont(new Font("Arial", Font.BOLD, 22));
        appTitle.setForeground(Color.WHITE);
        appTitle.setBorder(BorderFactory.createEmptyBorder(25, 10, 25, 10));

        JPanel menuPanel = new JPanel(new GridLayout(12, 1, 0, 10));
        menuPanel.setBackground(SIDEBAR_COLOR);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton dashboardButton = createMenuButton("Dashboard");
        JButton salaryButton = createMenuButton("Add Salary");
        JButton salaryHistoryButton = createMenuButton("Salary History");
        JButton expenseButton = createMenuButton("Add Expense");
        JButton reportButton = createMenuButton("Monthly Report");
        JButton historyButton = createMenuButton("Expense History");
        JButton changePasswordButton = createMenuButton("Change Password");
        JButton logoutButton = createMenuButton("Logout");

        dashboardButton.addActionListener(e -> refreshDashboard());

        salaryButton.addActionListener(e ->
                new AddSalaryFrame(loggedUser, this::refreshDashboard).setVisible(true)
        );

        salaryHistoryButton.addActionListener(e ->
                new SalaryHistoryFrame(loggedUser).setVisible(true)
        );

        expenseButton.addActionListener(e ->
                new AddExpenseFrame(loggedUser, this::refreshDashboard).setVisible(true)
        );

        reportButton.addActionListener(e ->
                new MonthlyReportFrame(loggedUser).setVisible(true)
        );

        historyButton.addActionListener(e ->
                new ExpenseHistoryFrame(loggedUser).setVisible(true)
        );

        changePasswordButton.addActionListener(e ->
                new ChangePasswordFrame(loggedUser).setVisible(true)
        );

        logoutButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        menuPanel.add(dashboardButton);

        if (loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
            JButton addUserButton = createMenuButton("Add User");
            JButton manageUsersButton = createMenuButton("Manage Users");

            addUserButton.addActionListener(e -> new AddUserFrame().setVisible(true));
            manageUsersButton.addActionListener(e -> new ManageUsersFrame().setVisible(true));

            menuPanel.add(addUserButton);
            menuPanel.add(manageUsersButton);
        }

        menuPanel.add(salaryButton);
        menuPanel.add(salaryHistoryButton);
        menuPanel.add(expenseButton);
        menuPanel.add(reportButton);
        menuPanel.add(historyButton);
        menuPanel.add(changePasswordButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(SIDEBAR_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 20, 15));

        JLabel userLabel = new JLabel(
                "<html>" +
                        "<b>" + loggedUser.getUsername() + "</b><br>" +
                        loggedUser.getRole() +
                        "</html>"
        );
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        bottomPanel.add(userLabel, BorderLayout.CENTER);
        bottomPanel.add(logoutButton, BorderLayout.SOUTH);

        sidebar.add(appTitle, BorderLayout.NORTH);
        sidebar.add(menuPanel, BorderLayout.CENTER);
        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 42));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }
}