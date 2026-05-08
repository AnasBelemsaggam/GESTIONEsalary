package dao;

import database.ConnectionDB;
import model.MonthlyReport;

import java.sql.*;

public class MonthlyReportDAO {

    public MonthlyReport getMonthlySummary(int userId, int month, int year) {
        String sql = "{CALL sp_GetMonthlySummary(?, ?, ?)}";

        try {
            Connection con = ConnectionDB.getConnection();
            CallableStatement cs = con.prepareCall(sql);

            cs.setInt(1, userId);
            cs.setInt(2, month);
            cs.setInt(3, year);

            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                MonthlyReport report = new MonthlyReport();

                report.setUserId(rs.getInt("userId"));
                report.setUsername(rs.getString("username"));
                report.setMonth(rs.getInt("month"));
                report.setYear(rs.getInt("year"));
                report.setSalary(rs.getBigDecimal("salary"));
                report.setTotalExpenses(rs.getBigDecimal("totalExpenses"));
                report.setRemaining(rs.getBigDecimal("remaining"));

                return report;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}