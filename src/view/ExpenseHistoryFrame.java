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
import java.util.List;

public class ExpenseHistoryFrame extends JFrame {

    private User loggedUser;

    private JTable expenseTable;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JComboBox<Category> categoryComboBox;
    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField dateField;

    public ExpenseHistoryFrame(User loggedUser) {
        this.loggedUser = loggedUser;

        setTitle("Expense History - " + loggedUser.getUsername());
        setSize(900, 600);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel(
                "Expense History for " + loggedUser.getUsername(),
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Category", "Amount", "Description", "Date"},
                0
        );

        expenseTable = new JTable(tableModel);
        expenseTable.setRowHeight(25);

        idField = new JTextField();
        idField.setEditable(false);

        categoryComboBox = new JComboBox<>();
        amountField = new JTextField();
        descriptionField = new JTextField();
        dateField = new JTextField();

        loadCategories();

        JButton refreshButton = new JButton("Refresh");
        JButton updateButton = new JButton("Update Expense");
        JButton deleteButton = new JButton("Delete Expense");

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        formPanel.add(new JLabel("Expense ID:"));
        formPanel.add(idField);

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

        JPanel bottomPanel = new JPanel(new BorderLayout());
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

        ExpenseDAO expenseDAO = new ExpenseDAO();
        List<Expense> expenses = expenseDAO.getExpensesByUser(loggedUser.getUserId());

        for (Expense e : expenses) {
            tableModel.addRow(new Object[]{
                    e.getExpenseId(),
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
            idField.setText(tableModel.getValueAt(selectedRow, 0).toString());

            String categoryName = tableModel.getValueAt(selectedRow, 1).toString();
            selectCategoryByName(categoryName);

            amountField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            descriptionField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            dateField.setText(tableModel.getValueAt(selectedRow, 4).toString());
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
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an expense first.");
                return;
            }

            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();

            if (selectedCategory == null) {
                JOptionPane.showMessageDialog(this, "Please select a category.");
                return;
            }

            Expense expense = new Expense();

            expense.setExpenseId(Integer.parseInt(idField.getText()));
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
            if (idField.getText().isEmpty()) {
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
                int expenseId = Integer.parseInt(idField.getText());

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
        idField.setText("");
        amountField.setText("");
        descriptionField.setText("");
        dateField.setText("");
        categoryComboBox.setSelectedIndex(0);
        expenseTable.clearSelection();
    }
}