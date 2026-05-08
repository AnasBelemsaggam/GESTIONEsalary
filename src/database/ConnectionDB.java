package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    private static final String URL =
            "jdbc:sqlserver://localhost\\SQLEXPRESS;" +
                    "databaseName=FinanceTracker;" +
                    "encrypt=true;" +
                    "trustServerCertificate=true;";

    private static final String USER = "finance_user";
    private static final String PASSWORD = "Finance2026!Strong";

    private static Connection connection = null;

    private ConnectionDB() {
    }

    public static Connection getConnection() {

        try {

            if (connection == null || connection.isClosed()) {

                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                connection = DriverManager.getConnection(
                        URL,
                        USER,
                        PASSWORD
                );

                System.out.println("Connexion SQL Server réussie !");
            }

        } catch (ClassNotFoundException e) {

            System.out.println("Driver SQL Server introuvable !");
            e.printStackTrace();

        } catch (SQLException e) {

            System.out.println("Erreur de connexion SQL Server !");
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection() {

        try {

            if (connection != null && !connection.isClosed()) {

                connection.close();

                System.out.println("Connexion fermée !");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}