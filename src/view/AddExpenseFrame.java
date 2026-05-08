package view;

import dao.CategoryDAO;
import dao.ExpenseDAO;
import model.Category;
import model.Expense;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AddExpenseFrame extends JFrame {

    private User loggedUser;
    private Runnable onDataChanged;

    private JComboBox<Category> categoryComboBox;
    private JTextField amountField;
    private JTextField descriptionField;

    public AddExpenseFrame(User loggedUser, Runnable onDataChanged) {
        this.loggedUser = loggedUser;
        this.onDataChanged = onDataChanged;

        setTitle("Add Expense - " + loggedUser.getUsername());
        setSize(500, 330);
        setLocationRelativeTo(null);

        categoryComboBox = new JComboBox<>();
        amountField = new JTextField();
        descriptionField = new JTextField();

        loadCategories();

        JButton saveButton = new JButton("Save Expense");

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        panel.add(new JLabel("User:"));
        panel.add(new JLabel(loggedUser.getUsername()));

        panel.add(new JLabel("Category:"));
        panel.add(categoryComboBox);

        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        panel.add(new JLabel(""));
        panel.add(saveButton);

        add(panel);

        saveButton.addActionListener(e -> saveExpense());
    }

    private void loadCategories() {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAllCategories();

        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }
    }

    private void saveExpense() {
        try {
            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();

            if (selectedCategory == null) {
                JOptionPane.showMessageDialog(this, "Please select a category.");
                return;
            }

            BigDecimal amount = new BigDecimal(amountField.getText());
            String description = descriptionField.getText();

            Expense expense = new Expense();

            expense.setUserId(loggedUser.getUserId());
            expense.setCategoryId(selectedCategory.getCategoryId());
            expense.setAmount(amount);
            expense.setDescription(description);
            expense.setExpenseDate(LocalDate.now());

            ExpenseDAO expenseDAO = new ExpenseDAO();
            expenseDAO.addExpense(expense);

            JOptionPane.showMessageDialog(this, "Expense saved successfully.");

            amountField.setText("");
            descriptionField.setText("");

            if (onDataChanged != null) {
                onDataChanged.run();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check values.");
        }
    }
}