package view;

import dao.CategoryDAO;
import dao.ExpenseDAO;
import model.Category;
import model.Expense;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseHistoryFrame extends JFrame {

    private User loggedUser;

    private JTable expenseTable;
    private DefaultTableModel tableModel;

    private List<Integer> expenseIds;

    private JComboBox<Category> categoryComboBox;
    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField dateField;

    public ExpenseHistoryFrame(User loggedUser) {
        this.loggedUser = loggedUser;
        this.expenseIds = new ArrayList<>();

        setTitle("Expense History - " + loggedUser.getUsername());
        setSize(950, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel(
                "Expense History for " + loggedUser.getUsername(),
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(24, 31, 45));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        tableModel = new DefaultTableModel(
                new Object[]{"Category", "Amount", "Description", "Date"},
                0
        );

        expenseTable = new JTable(tableModel);
        expenseTable.setRowHeight(30);
        expenseTable.setFont(new Font("Arial", Font.PLAIN, 14));
        expenseTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        expenseTable.getTableHeader().setBackground(new Color(38, 49, 68));
        expenseTable.getTableHeader().setForeground(Color.WHITE);
        expenseTable.setSelectionBackground(new Color(220, 235, 255));
        expenseTable.setSelectionForeground(Color.BLACK);
        expenseTable.setGridColor(new Color(230, 230, 230));

        categoryComboBox = new JComboBox<>();
        amountField = new JTextField();
        descriptionField = new JTextField();
        dateField = new JTextField();

        styleField(amountField);
        styleField(descriptionField);
        styleField(dateField);

        loadCategories();

        JButton refreshButton = createButton("Refresh");
        JButton updateButton = createButton("Update Expense");
        JButton deleteButton = createButton("Delete Expense");

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 12, 12));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryComboBox);

        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);

        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);

        formPanel.add(new JLabel("Date yyyy-mm-dd:"));
        formPanel.add(dateField);

        formPanel.add(refreshButton);
        formPanel.add(updateButton);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(245, 247, 250));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(deleteButton, BorderLayout.SOUTH);

        add(titleLabel, BorderLayout.NORTH);
        add(new JScrollPane(expenseTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        expenseTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromTable();
            }
        });

        refreshButton.addActionListener(e -> loadExpenses());
        updateButton.addActionListener(e -> updateExpense());
        deleteButton.addActionListener(e -> deleteExpense());

        loadExpenses();
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

    private void loadCategories() {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAllCategories();

        categoryComboBox.removeAllItems();

        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }
    }

    private void loadExpenses() {
        tableModel.setRowCount(0);
        expenseIds.clear();

        ExpenseDAO expenseDAO = new ExpenseDAO();
        List<Expense> expenses = expenseDAO.getExpensesByUser(loggedUser.getUserId());

        for (Expense e : expenses) {
            expenseIds.add(e.getExpenseId());

            tableModel.addRow(new Object[]{
                    e.getCategoryName(),
                    e.getAmount(),
                    e.getDescription(),
                    e.getExpenseDate()
            });
        }
    }

    private void fillFormFromTable() {
        int selectedRow = expenseTable.getSelectedRow();

        if (selectedRow >= 0) {
            String categoryName = tableModel.getValueAt(selectedRow, 0).toString();
            selectCategoryByName(categoryName);

            amountField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            descriptionField.setText(tableModel.getValueAt(selectedRow, 2) == null ? "" : tableModel.getValueAt(selectedRow, 2).toString());
            dateField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        }
    }

    private void selectCategoryByName(String categoryName) {
        for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
            Category category = categoryComboBox.getItemAt(i);

            if (category.getName().equalsIgnoreCase(categoryName)) {
                categoryComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private void updateExpense() {
        try {
            int selectedRow = expenseTable.getSelectedRow();

            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select an expense first.");
                return;
            }

            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();

            if (selectedCategory == null) {
                JOptionPane.showMessageDialog(this, "Please select a category.");
                return;
            }

            int expenseId = expenseIds.get(selectedRow);

            Expense expense = new Expense();

            expense.setExpenseId(expenseId);
            expense.setUserId(loggedUser.getUserId());
            expense.setCategoryId(selectedCategory.getCategoryId());
            expense.setAmount(new BigDecimal(amountField.getText()));
            expense.setDescription(descriptionField.getText());
            expense.setExpenseDate(LocalDate.parse(dateField.getText()));

            ExpenseDAO expenseDAO = new ExpenseDAO();
            expenseDAO.updateExpense(expense);

            JOptionPane.showMessageDialog(this, "Expense updated successfully.");

            loadExpenses();
            clearFields();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while updating expense. Check values.");
        }
    }

    private void deleteExpense() {
        try {
            int selectedRow = expenseTable.getSelectedRow();

            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(this, "Please select an expense first.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this expense?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int expenseId = expenseIds.get(selectedRow);

                ExpenseDAO expenseDAO = new ExpenseDAO();
                expenseDAO.deleteExpense(expenseId, loggedUser.getUserId());

                JOptionPane.showMessageDialog(this, "Expense deleted successfully.");

                loadExpenses();
                clearFields();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error while deleting expense.");
        }
    }

    private void clearFields() {
        amountField.setText("");
        descriptionField.setText("");
        dateField.setText("");

        if (categoryComboBox.getItemCount() > 0) {
            categoryComboBox.setSelectedIndex(0);
        }

        expenseTable.clearSelection();
    }
}