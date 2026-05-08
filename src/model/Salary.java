package model;

import java.math.BigDecimal;

public class Salary {

    private int salaryId;
    private int userId;
    private BigDecimal amount;
    private int month;
    private int year;

    public Salary() {
    }

    public Salary(int salaryId, int userId, BigDecimal amount, int month, int year) {
        this.salaryId = salaryId;
        this.userId = userId;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    public int getSalaryId() {
        return salaryId;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }
}