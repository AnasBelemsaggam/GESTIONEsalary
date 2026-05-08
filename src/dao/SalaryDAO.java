package dao;

import database.ConnectionDB;
import model.Salary;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaryDAO {

    public void addOrUpdateSalary(Salary salary) {

        String checkSql =
                "SELECT salaryId, amount FROM Salaries " +
                        "WHERE userId = ? AND month = ? AND year = ?";

        try {
            Connection con = ConnectionDB.getConnection();

            PreparedStatement checkPs = con.prepareStatement(checkSql);

            checkPs.setInt(1, salary.getUserId());
            checkPs.setInt(2, salary.getMonth());
            checkPs.setInt(3, salary.getYear());

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {

                int salaryId = rs.getInt("salaryId");

                String updateSql =
                        "UPDATE Salaries " +
                                "SET amount = amount + ? " +
                                "WHERE salaryId = ?";

                PreparedStatement updatePs = con.prepareStatement(updateSql);

                updatePs.setBigDecimal(1, salary.getAmount());
                updatePs.setInt(2, salaryId);

                updatePs.executeUpdate();

                System.out.println("Salaire ajouté au salaire existant.");

            } else {

                String insertSql =
                        "INSERT INTO Salaries(userId, amount, month, year) " +
                                "VALUES (?, ?, ?, ?)";

                PreparedStatement insertPs = con.prepareStatement(insertSql);

                insertPs.setInt(1, salary.getUserId());
                insertPs.setBigDecimal(2, salary.getAmount());
                insertPs.setInt(3, salary.getMonth());
                insertPs.setInt(4, salary.getYear());

                insertPs.executeUpdate();

                System.out.println("Salaire ajouté.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Salary> getSalariesByUser(int userId) {

        List<Salary> salaries = new ArrayList<>();

        String sql =
                "SELECT salaryId, userId, amount, month, year " +
                        "FROM Salaries " +
                        "WHERE userId = ? " +
                        "ORDER BY year DESC, month DESC";

        try {
            Connection con = ConnectionDB.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Salary salary = new Salary();

                salary.setSalaryId(rs.getInt("salaryId"));
                salary.setUserId(rs.getInt("userId"));
                salary.setAmount(rs.getBigDecimal("amount"));
                salary.setMonth(rs.getInt("month"));
                salary.setYear(rs.getInt("year"));

                salaries.add(salary);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salaries;
    }

    public void updateSalary(Salary salary) {

        String sql =
                "UPDATE Salaries " +
                        "SET amount = ?, month = ?, year = ? " +
                        "WHERE salaryId = ? AND userId = ?";

        try {
            Connection con = ConnectionDB.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setBigDecimal(1, salary.getAmount());
            ps.setInt(2, salary.getMonth());
            ps.setInt(3, salary.getYear());
            ps.setInt(4, salary.getSalaryId());
            ps.setInt(5, salary.getUserId());

            ps.executeUpdate();

            System.out.println("Salaire modifié.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteSalary(int salaryId, int userId) {

        String sql =
                "DELETE FROM Salaries " +
                        "WHERE salaryId = ? AND userId = ?";

        try {
            Connection con = ConnectionDB.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, salaryId);
            ps.setInt(2, userId);

            ps.executeUpdate();

            System.out.println("Salaire supprimé.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}