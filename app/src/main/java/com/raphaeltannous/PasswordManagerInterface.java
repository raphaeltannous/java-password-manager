package com.raphaeltannous;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.mc.SQLiteMCSqlCipherConfig;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * PasswordManagerInterface
 */
public interface PasswordManagerInterface {

    // You should use isFileABD() before initializing an instance.
    // If you initialize without checking and the file is not a database,
    // it will throw an exception.
    public static boolean isFileADB(Path databasePath, String databasePassword) {
        boolean isTablesAvailable = false;

        if (Files.exists(databasePath)) {
            try (
                Connection connection = SQLiteMCSqlCipherConfig.getV4Defaults().withKey(databasePassword).build().createConnection("jdbc:sqlite:file:" + databasePath.normalize());
                Statement statement = connection.createStatement();
            ) {
                statement.setQueryTimeout(30);

                DatabaseMetaData dmd = connection.getMetaData();
                ResultSet rs = dmd.getTables(null, null, "%", null);

                // Checking if the tables `Passwords` and `backupCodes` are present.
                boolean isPasswordsTableAvailable = false;
                boolean isBackupCodesTableAvaible = false;
                while (rs.next()) {
                    if (rs.getString(4).equalsIgnoreCase("TABLE") && rs.getString(3).equals("passwords")) {
                        isPasswordsTableAvailable = true;
                    }

                    if (rs.getString(4).equalsIgnoreCase("TABLE") && rs.getString(3).equals("backupCodes")) {
                        isBackupCodesTableAvaible = true;
                    }
                }

                isTablesAvailable = isPasswordsTableAvailable && isBackupCodesTableAvaible;
            } catch (SQLException e) {
                return false;
            }
        } else {
            return false;
        }

        return isTablesAvailable;
    };

    public List<String[]> fetchPasswords();
    public int getPasswordsCount();

    public String fetchWebsite(int passwordId);
    public String fetchUsername(int passwordId);
    public String fetchPassword(int passwordId);
    public String[] fetchPasswordData(int passwordId);

    // Website/Username/Password are required to be in a password and they cannot be empty.
    public void addPassword(
        String website,
        String username,
        String password
    );

    public void modifyWebsite(int passwordId, String newWebsite); // newWebsite cannot be empty.
    public void modifyUsername(int passwordId, String newUsername); // newUsername cannot be empty.
    public void modifyPassword(int passwordId, String newPassword); // newPassword cannot be empty.

    public void deletePassword(int passwordId);

    public String fetchOTP(int passwordId); // otp doesn't exists if otp is "".
    public void modifyOTP(int passwordId, String newOTP); // if newOTP is empty, then remove OTP.

    public String fetchNote(int passwordId); // note doesn't exists if note is "".
    public void modifyNote(int passwordId, String newNote); // if newNote is empty, then remove note.

    public List<String[]> fetchBackupCodes(int passwordId);
    public void addBackupCode(int passwordId, String backupCode); // backupCode cannot be empty if we are adding a backup code.
    public void removeBackupCode(int backupCodeId);
    public void updateBackupCodeStatus(int backupCodeId, int status); // status is 0 if backup code is not used otherwise 1.
    public void updateHasBackupCodeStatus(int passwordId, int status);
}
