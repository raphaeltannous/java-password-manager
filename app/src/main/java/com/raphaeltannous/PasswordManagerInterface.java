package com.raphaeltannous;

import java.util.List;

/**
 * PasswordManagerInterface
 */
public interface PasswordManagerInterface {

    public List<String[]> readPasswords();

    // Website/Username/Password are required to be in a password.
    public void addPassword(
        String website,
        String username,
        String password
    );

    public void modifyWebsite(int passwordId, String newWebsite); // newWebsite cannot be empty
    public void modifyUsername(int passwordId, String newUsername); // newUsername cannot be empty
    public void modifyPassword(int passwordId, String newPassword); // newPassword cannot be empty

    public void addOTP(int passwordId, String otp); // otp cannot be empty if we are adding OTP
    public void modifyOTP(int passwordId, String newOTP); // if newOTP is empty, then remove OTP

    public void addNote(int passwordId, String note); // note cannot be empty if we are adding a note
    public void modifyNote(int passwordId, String newNote); // if newNote is empty, then remove note

    public void deletePassword(int passwordId);

    public void addBackupCode(int passwordId, String backupCode); // backupCode cannot be empty if we are adding a backup code
    public void removeBackupCode(int backupCodeId);
    public void setBackupCodeUsed(int backupCodeId);

    public void removePassword(int passwordId);
}
