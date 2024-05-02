package com.raphaeltannous;

import java.util.List;

/**
 * PasswordManagerInterface
 */
public interface PasswordManagerInterface {

    public List<String[]> readPasswords();
    public void addPassword(String website, String username, String password);
    public void modifyPassword(int passwordIndex, String newPassword);
    public void deletePassword(int passwordIndex);
}
