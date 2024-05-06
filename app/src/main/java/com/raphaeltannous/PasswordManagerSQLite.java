package com.raphaeltannous;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * PasswordManagerSQLite
 */
public class PasswordManagerSQLite implements PasswordManagerInterface {
    private final String databaseFilename = "passwords.db";

    // Prepared Statements.
    private final String addPasswordStatement = "INSERT INTO Manager (website, username, password)"
                                              + " VALUES (?, ?, ?);";
    private final String updatePasswordStatement = "UPDATE Manager SET password = ? WHERE id = ?;";
    private final String deletePasswordStatement = "DELETE FROM Manager WHERE id = ?;";

    // Constructor and database initializer.
    public PasswordManagerSQLite() {
        try (
            // By default if there's no database file the sqlite-jdbc will create one.
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFilename);
            Statement statement = connection.createStatement();
        ) {
            statement.setQueryTimeout(30);

            DatabaseMetaData dmd = connection.getMetaData();
            ResultSet rs = dmd.getTables(null, null, "%", null);

            // Checking if the manager's table is present.
            boolean isManagerAvailable = false;
            while (rs.next()) {
                if (rs.getString(4).equalsIgnoreCase("TABLE") && rs.getString(3).equals("Manager")) {
                    isManagerAvailable = true;
                    break;
                }
            }

            // If there's no table, create a new one.
            if (!isManagerAvailable) {
                statement.executeUpdate(
                    "CREATE TABLE Manager ("
                    + "id INTEGER PRIMARY KEY, "
                    + "website VARCHAR, "
                    + "username VARCHAR, "
                    + "password VARCJAR"
                    + ");"
                );
            }

        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public List<String[]> readPasswords() {
        List<String[]> passwords = new ArrayList<>();

        try (
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFilename);
            Statement statement = connection.createStatement();
        ) {
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery("SELECT * FROM Manager;");

            while (rs.next()) {
                String[] data = {
                    rs.getString("id"),
                    rs.getString("website"),
                    rs.getString("username"),
                    rs.getString("password")
                };

                passwords.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return passwords;
    }

    public void addPassword(String website, String username, String password) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFilename);
            PreparedStatement statement = connection.prepareStatement(addPasswordStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setString(1, website);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void modifyPassword(int index, String newPassword) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFilename);
            PreparedStatement statement = connection.prepareStatement(updatePasswordStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setString(1, newPassword);
            statement.setInt(2, index);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void deletePassword(int passwordIndex) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFilename);
            PreparedStatement statement = connection.prepareStatement(deletePasswordStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, passwordIndex);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

    }
}
