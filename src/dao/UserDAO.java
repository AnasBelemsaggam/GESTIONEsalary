package dao;

import database.ConnectionDB;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void addUser(User user) {
        String sql = "INSERT INTO Users(username, email, passwordHash, role) VALUES (?, ?, ?, ?)";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole() == null ? "USER" : user.getRole());

            ps.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT userId, username, email, passwordHash, role FROM Users WHERE email = ?";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User();

                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setRole(rs.getString("role"));

                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String sql = "SELECT userId, username, email, passwordHash, role FROM Users";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();

                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPasswordHash(rs.getString("passwordHash"));
                user.setRole(rs.getString("role"));

                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void updateUser(User user) {
        String sql = "UPDATE Users SET username = ?, email = ?, passwordHash = ?, role = ? WHERE userId = ?";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());
            ps.setInt(5, user.getUserId());

            ps.executeUpdate();
            System.out.println("Utilisateur modifié avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM Users WHERE userId = ?";

        try {
            Connection con = ConnectionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);

            ps.executeUpdate();
            System.out.println("Utilisateur supprimé avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updatePassword(int userId, String oldPassword, String newPassword) {
        String checkSql = "SELECT passwordHash FROM Users WHERE userId = ?";
        String updateSql = "UPDATE Users SET passwordHash = ? WHERE userId = ?";

        try {
            Connection con = ConnectionDB.getConnection();

            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, userId);

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {
                String currentPassword = rs.getString("passwordHash");


                if (!currentPassword.equals(oldPassword)) {
                    return false;
                }

                PreparedStatement updatePs = con.prepareStatement(updateSql);
                updatePs.setString(1, newPassword);
                updatePs.setInt(2, userId);

                updatePs.executeUpdate();

                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}