package view;

import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private User loggedUser;
    private JPanel contentPanel;

    public MainFrame(User loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Personal Finance Tracker - " + loggedUser.getRole());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

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
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(11, 1, 10, 10));
        sidebar.setPreferredSize(new Dimension(220, 600));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        sidebar.setBackground(new Color(35, 45, 65));

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

        sidebar.add(dashboardButton);

        if (loggedUser.getRole().equalsIgnoreCase("ADMIN")) {
            JButton addUserButton = createMenuButton("Add User");
            JButton manageUsersButton = createMenuButton("Manage Users");

            addUserButton.addActionListener(e -> new AddUserFrame().setVisible(true));
            manageUsersButton.addActionListener(e -> new ManageUsersFrame().setVisible(true));

            sidebar.add(addUserButton);
            sidebar.add(manageUsersButton);
        }

        sidebar.add(salaryButton);
        sidebar.add(salaryHistoryButton);
        sidebar.add(expenseButton);
        sidebar.add(reportButton);
        sidebar.add(historyButton);
        sidebar.add(changePasswordButton);
        sidebar.add(logoutButton);

        return sidebar;
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setBackground(new Color(55, 70, 100));
        button.setForeground(Color.WHITE);
        return button;
    }
}