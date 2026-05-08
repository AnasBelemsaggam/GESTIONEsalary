package dao;

import database.ConnectionDB;
import model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        String sql = "SELECT categoryId, name, icon FROM Categories";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Category category = new Category();

                category.setCategoryId(rs.getInt("categoryId"));
                category.setName(rs.getString("name"));
                category.setIcon(rs.getString("icon"));

                categories.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public Category getCategoryById(int categoryId) {
        String sql = "SELECT categoryId, name, icon FROM Categories WHERE categoryId = ?";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, categoryId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Category category = new Category();

                category.setCategoryId(rs.getInt("categoryId"));
                category.setName(rs.getString("name"));
                category.setIcon(rs.getString("icon"));

                return category;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}