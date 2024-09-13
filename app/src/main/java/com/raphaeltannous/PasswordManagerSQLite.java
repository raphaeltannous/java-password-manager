package com.raphaeltannous;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.mc.SQLiteMCSqlCipherConfig;

import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;

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

    private final String isPasswordInDBStatement = (
        "SELECT id FROM passwords "
        + "WHERE id = ?;"
    );

    private final String isBackupCodeInDBStatement = (
        "SELECT id FROM backupCodes "
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

        if (
            !PasswordManagerInterface.isFileADB(this.databasePath, this.databasePassword) &&
            Files.exists(this.databasePath)
        ) {
            throw new IllegalArgumentException(
                "Failed establishing a connection to the database.\n"
                + "The PasswordManagerInterface provides isFileADB() function to check before initializing."
            );
        }

        try (
            // By default, if there's no database file the sqlite-jdbc will create one.
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            Statement statement = connection.createStatement();
        ) {
            statement.setQueryTimeout(30);

            DatabaseMetaData dmd = connection.getMetaData();
            ResultSet rs = dmd.getTables(null, null, "%", null);

            // Checking if the tables `Passwords` and `backupCodes` are present.
            boolean isPasswordsTableAvailable = false;
            boolean isBackupCodesTableAvaible = false;
            boolean areTablesAvailable = false;
            while (rs.next()) {
                if (rs.getString(4).equalsIgnoreCase("TABLE") && rs.getString(3).equals("passwords")) {
                    isPasswordsTableAvailable = true;
                }

                if (rs.getString(4).equalsIgnoreCase("TABLE") && rs.getString(3).equals("backupCodes")) {
                    isBackupCodesTableAvaible = true;
                }
            }

            areTablesAvailable = isPasswordsTableAvailable && isBackupCodesTableAvaible;

            if (isPasswordsTableAvailable != isBackupCodesTableAvaible) { // XOR
                throw new SQLDataException("database's tables are messed up.");
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

            if (!areTablesAvailable) {
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

    public int getPasswordsCount() {
        int count = 0;

        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            Statement statement = connection.createStatement();
        ) {
            statement.setQueryTimeout(30);

            ResultSet rs = statement.executeQuery(
                "SELECT COUNT(*) FROM passwords;"
            );

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return count;
    }

    public void addPassword(String website, String username, String password) {
        if (website == "") {
            throw new IllegalArgumentException("website cannot be empty.");
        }

        if (username == "") {
            throw new IllegalArgumentException("username cannot be empty.");
        }

        if (password == "") {
            throw new IllegalArgumentException("password cannot be empty.");
        }

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

    private boolean isPasswordInDB(int passwordId) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(isPasswordInDBStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, passwordId);

            ResultSet rs = statement.executeQuery();

            int fetchedPasswordId = rs.getInt("id");

            if (passwordId == fetchedPasswordId) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return false;
    }

    public void modifyWebsite(int passwordId, String newWebsite) {
        if (newWebsite == "") {
            throw new IllegalArgumentException("newWebsite cannot be empty.");
        }

        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
        if (newUsername ==  "") {
            throw new IllegalArgumentException("newUsername cannot be empty.");
        }

        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
        if (newPassword == "") {
            throw new IllegalArgumentException("newPassword cannot be empty.");
        }

        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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

    private boolean isBackupCodeInDB(int backupCodeId) {
        try (
            Connection connection = databaseConfig.withKey(this.databasePassword).build().createConnection(this.databaseURL);
            PreparedStatement statement = connection.prepareStatement(isBackupCodeInDBStatement);
        ) {
            statement.setQueryTimeout(30);

            statement.setInt(1, backupCodeId);

            ResultSet rs = statement.executeQuery();

            int fetchedPasswordId = rs.getInt("id");

            if (backupCodeId == fetchedPasswordId) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }

        return false;
    }

    public void removeBackupCode(int backupCodeId) {
        if (!isBackupCodeInDB(backupCodeId)) {
            throw new IllegalArgumentException("backup code is not in the database.");
        }

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
        if (!isBackupCodeInDB(backupCodeId)) {
            throw new IllegalArgumentException("backup code is not in the database.");
        }

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
        if (!isPasswordInDB(passwordId)) {
            throw new IllegalArgumentException("password is not in the database.");
        }

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
