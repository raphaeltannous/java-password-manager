package com.raphaeltannous;

import java.util.List;

/**
 * PasswordManagerInterface
 */
public interface PasswordManagerInterface {

    public List<String[]> fetchPasswords();

    // Website/Username/Password are required to be in a password.
    public void addPassword(
        String website,
        String username,
        String password
    );

    public void modifyWebsite(int passwordId, String newWebsite); // newWebsite cannot be empty
    public void modifyUsername(int passwordId, String newUsername); // newUsername cannot be empty
    public void modifyPassword(int passwordId, String newPassword); // newPassword cannot be empty

    public void deletePassword(int passwordId);

    public String fetchOTP(int passwordId);
    public void modifyOTP(int passwordId, String newOTP); // if newOTP is empty, then remove OTP

    public String fetchNote(int passwordId); // note doesn't exists if note is ""
    public void modifyNote(int passwordId, String newNote); // if newNote is empty, then remove note

    public List<String[]> fetchBackupCodes(int passwordId);
    public void addBackupCode(int passwordId, String backupCode); // backupCode cannot be empty if we are adding a backup code
    public void removeBackupCode(int backupCodeId);
    public void updateBackupCodeStatus(int backupCodeId, int status); // status is 0 if backup code is not used otherwise 1.
    public void updateHasBackupCodeStatus(int passwordId, int status);
}
