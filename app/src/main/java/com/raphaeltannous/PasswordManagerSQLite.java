package com.raphaeltannous;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.mc.SQLiteMCSqlCipherConfig;

import java.util.ArrayList;
import java.util.List;

import java.nio.file.Path;
import java.nio.file.Files;

/**
 * PasswordManagerSQLite
 */
public class PasswordManagerSQLite implements PasswordManagerInterface {
    // Encryption Configuration
    SQLiteMCSqlCipherConfig databaseConfig = SQLiteMCSqlCipherConfig.getV4Defaults();
    private Path databasePath;
    private String databasePassword;
    private String databaseURL;

    // Prepared Statements.
    private final String addPasswordStatement = (
        "INSERT INTO passwords (website, username, password)"
        + " VALUES (?, ?, ?);"
    );

    private final String updateWebsiteStatement = (
        "UPDATE passwords SET website = ? "
        + "WHERE id = ?;"
    );

    private final String updateUsernameStatement = (
        "UPDATE passwords SET username = ? "
        + "WHERE id = ?;"
    );

    private final String updatePasswordStatement = (
        "UPDATE passwords SET password = ? "
        + "WHERE id = ?;"
    );

    private final String deletePasswordStatement = (
        "DELETE FROM passwords "
        + "WHERE id = ?;"
    );

    private final String fetchOTPStatement = (
        "SELECT otp FROM passwords "
        + "WHERE id = ?;"
    );

    private final String updateOTPStatement = (
        "UPDATE passwords SET otp = ? "
        + "WHERE id = ?;"
    );

    private final String fetchNoteStatement = (
        "SELECT note FROM passwords "
        + "WHERE id = ?;"
    );

    private final String updateNoteStatement = (
        "UPDATE passwords SET note = ? "
        + "WHERE id = ?;"
    );

    private final String fetchBackupCodesStatement = (
        "SELECT * FROM backupCodes "
        + "WHERE passwordId = ?;"
    );

    private final String addBackupCodeStatement = (
        "INSERT INTO backupCodes (passwordId, code) "
        + "VALUES (?, ?);"
    );

    private final String removeBackupCodeStatement = (
        "DELETE FROM backupCodes "
        + "WHERE id = ?;"
    );

    private final String updateBackupCodeStatusStatement =  (
        "UPDATE backupCodes SET isUsed = ? "
        + "WHERE id = ?;"
    );

    private final String updateHasBackupCodeStatusStatement =  (
        "UPDATE passwords SET hasBackupCodes = ? "
        + "WHERE id = ?;"
    );

    // Constructor and database initializer.
    public PasswordManagerSQLite(
        Path databasePath,
        String databasePassword
    ) {
        this.databasePath =  databasePath.normalize();
        this.databasePassword = databasePassword;
        this.databaseURL = "jdbc:sqlite:file:" + this.databasePath;

        if (Files.exists(this.databasePath)) {
            // TODO: Check if the file is a database.
            // TODO: Check if the password is correct.
        }

        try (
            // By default, if there's no database file the sqlite-jdbc will create one.
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            Statement statement = connection.createStatement();
        ) {
            statement.setQueryTimeout(30);

            DatabaseMetaData dmd = connection.getMetaData();
            ResultSet rs = dmd.getTables(null, null, "%", null);

            // Checking if the `Passwords`'s table is present.
            boolean isPasswordsTableAvailable = false;
            while (rs.next()) {
                if (rs.getString(4).equalsIgnoreCase("TABLE") && rs.getString(3).equals("passwords")) {
                    isPasswordsTableAvailable = true;
                    break;
                }
            }

            // If there's no table, create a new one.
            //
            // CREATE TABLE passwords (
            //   id INTEGER NOT NULL PRIMARY KEY,
            //   website VARCHAR NOT NULL,
            //   username VARCHAR NOT NULL,
            //   password VARCHAR NOT NULL,
            //   otp VARCHAR DEFAULT '',
            //   hasBackupCodes INTEGER DEFAULT 0 CHECK (hasBackupCodes IN (0, 1)),
            //   note VARCHAR DEFAULT ''
            // );
            //
            // CREATE TABLE backupCodes (
            //   id INTEGER NOT NULL PRIMARY KEY,
            //   passwordId INTEGER NOT NULL,
            //   code VARCHAR NOT NULL,
            //   isUsed INTEGER DEFAULT 0 CHECK (isUsed IN (0, 1)),
            //   FOREIGN KEY (passwordId) REFERENCES passwords(id)
            // );

            if (!isPasswordsTableAvailable) {
                statement.executeUpdate(
                    "CREATE TABLE passwords ("
                    + "id INTEGER NOT NULL PRIMARY KEY, "
                    + "website VARCHAR NOT NULL, "
                    + "username VARCHAR NOT NULL, "
                    + "password VARCHAR NOT NULL, "
                    + "otp VARCHAR DEFAULT '', "
                    + "hasBackupCodes INTEGER DEFAULT 0 CHECK (hasBackupCodes IN (0, 1)), "
                    + "note VARCHAR DEFAULT ''"
                    + ");"
                );

                statement.executeUpdate(
                    "CREATE TABLE backupCodes ("
                    + "id INTEGER NOT NULL PRIMARY KEY, "
                    + "passwordId INTEGER NOT NULL, "
                    + "code VARCHAR NOT NULL, "
                    + "isUsed INTEGER DEFAULT 0 CHECK (isUsed IN (0, 1)), "
                    + "FOREIGN KEY (passwordId) REFERENCES passwords(id)"
                    + ");"
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public List<String[]> fetchPasswords() {
        List<String[]> passwords = new ArrayList<>();

        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            Statement statement = connection.createStatement();
        ) {
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery(
                "SELECT * FROM passwords;"
            );

            while (rs.next()) {
                String[] data = {
                    rs.getString("id"),
                    rs.getString("website"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("otp"),
                    Integer.toString(rs.getInt("hasBackupCodes")),
                    rs.getString("note")
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
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
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

    public void modifyWebsite(int passwordId, String newWebsite) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(updateWebsiteStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setString(1, newWebsite);
            statement.setInt(2, passwordId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void modifyUsername(int passwordId, String newUsername) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(updateUsernameStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setString(1, newUsername);
            statement.setInt(2, passwordId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void modifyPassword(int passwordId, String newPassword) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(updatePasswordStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setString(1, newPassword);
            statement.setInt(2, passwordId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void deletePassword(int passwordId) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(deletePasswordStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, passwordId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public String fetchOTP(int passwordId) {
        String otp = "";

        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(fetchOTPStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, passwordId);

            ResultSet rs = statement.executeQuery();

            otp = rs.getString("otp");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return otp;
    }

    public void modifyOTP(int passwordId, String newOTP) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(updateOTPStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setString(1, newOTP);
            statement.setInt(2, passwordId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public String fetchNote(int passwordId) {
        String note = "";

        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(fetchNoteStatement);
        ) {
            statement.setQueryTimeout(30);


            statement.setInt(1, passwordId);

            ResultSet rs = statement.executeQuery();

            note = rs.getString("note");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return note;
    }

    public void modifyNote(int passwordId, String newNote) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(updateNoteStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setString(1, newNote);
            statement.setInt(2, passwordId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public List<String[]> fetchBackupCodes(int passwordId) {
        List<String[]> backupCodes = new ArrayList<>();

        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(fetchBackupCodesStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, passwordId);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String[] data = {
                    rs.getString("id"),
                    rs.getString("passwordId"),
                    rs.getString("code"),
                    rs.getString("IsUsed")
                };

                backupCodes.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return backupCodes;
    }

    public void addBackupCode(int passwordId, String backupCode) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(addBackupCodeStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, passwordId);
            statement.setString(2, backupCode);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void removeBackupCode(int backupCodeId) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(removeBackupCodeStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, backupCodeId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void updateBackupCodeStatus(int backupCodeId, int status) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(updateBackupCodeStatusStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, status);
            statement.setInt(2, backupCodeId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public void updateHasBackupCodeStatus(int passwordId, int status) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(updateHasBackupCodeStatusStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, status);
            statement.setInt(2, passwordId);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
}
