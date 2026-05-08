package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {

    private int expenseId;
    private int userId;
    private int categoryId;

    private String categoryName;

    private BigDecimal amount;
    private String description;
    private LocalDate expenseDate;

    public Expense() {
    }

    public Expense(
            int expenseId,
            int userId,
            int categoryId,
            String categoryName,
            BigDecimal amount,
            String description,
            LocalDate expenseDate
    ) {
        this.expenseId = expenseId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.amount = amount;
        this.description = description;
        this.expenseDate = expenseDate;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public int getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }
}