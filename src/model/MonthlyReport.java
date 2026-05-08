package model;

import java.math.BigDecimal;

public class MonthlyReport {

    private int userId;
    private String username;
    private int month;
    private int year;
    private BigDecimal salary;
    private BigDecimal totalExpenses;
    private BigDecimal remaining;

    public MonthlyReport() {
    }

    public MonthlyReport(int userId, String username, int month, int year,
                         BigDecimal salary, BigDecimal totalExpenses, BigDecimal remaining) {
        this.userId = userId;
        this.username = username;
        this.month = month;
        this.year = year;
        this.salary = salary;
        this.totalExpenses = totalExpenses;
        this.remaining = remaining;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getRemaining() {
        return remaining;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public void setRemaining(BigDecimal remaining) {
        this.remaining = remaining;
    }
}