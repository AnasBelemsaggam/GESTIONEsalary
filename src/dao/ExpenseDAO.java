package dao;

import database.ConnectionDB;
import model.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {

    public void addExpense(Expense expense) {
        String sql = "{CALL sp_AddExpense(?, ?, ?, ?, ?)}";

        try {
            Connection con = ConnectionDB.getConnection();
            CallableStatement cs = con.prepareCall(sql);

            cs.setInt(1, expense.getUserId());
            cs.setInt(2, expense.getCategoryId());
            cs.setBigDecimal(3, expense.getAmount());
            cs.setString(4, expense.getDescription());

            if (expense.getExpenseDate() != null) {
                cs.setDate(5, Date.valueOf(expense.getExpenseDate()));
            } else {
                cs.setNull(5, Types.DATE);
            }

            cs.execute();
            System.out.println("Dépense ajoutée avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Expense> getExpensesByUser(int userId) {
        List<Expense> expenses = new ArrayList<>();

        String sql =
                "SELECT " +
                        "e.expenseId, " +
                        "e.userId, " +
                        "e.categoryId, " +
                        "c.name AS categoryName, " +
                        "e.amount, " +
                        "e.description, " +
                        "e.expenseDate " +
                        "FROM Expenses e " +
                        "JOIN Categories c ON e.categoryId = c.categoryId " +
                        "WHERE e.userId = ? " +
                        "ORDER BY e.expenseDate DESC";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Expense expense = new Expense();

                expense.setExpenseId(rs.getInt("expenseId"));
                expense.setUserId(rs.getInt("userId"));
                expense.setCategoryId(rs.getInt("categoryId"));
                expense.setCategoryName(rs.getString("categoryName"));
                expense.setAmount(rs.getBigDecimal("amount"));
                expense.setDescription(rs.getString("description"));
                expense.setExpenseDate(rs.getDate("expenseDate").toLocalDate());

                expenses.add(expense);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    public void updateExpense(Expense expense) {
        String sql =
                "UPDATE Expenses " +
                        "SET categoryId = ?, amount = ?, description = ?, expenseDate = ? " +
                        "WHERE expenseId = ? AND userId = ?";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, expense.getCategoryId());
            ps.setBigDecimal(2, expense.getAmount());
            ps.setString(3, expense.getDescription());
            ps.setDate(4, Date.valueOf(expense.getExpenseDate()));
            ps.setInt(5, expense.getExpenseId());
            ps.setInt(6, expense.getUserId());

            ps.executeUpdate();
            System.out.println("Dépense modifiée avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExpense(int expenseId, int userId) {
        String sql = "DELETE FROM Expenses WHERE expenseId = ? AND userId = ?";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, expenseId);
            ps.setInt(2, userId);

            ps.executeUpdate();
            System.out.println("Dépense supprimée avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}