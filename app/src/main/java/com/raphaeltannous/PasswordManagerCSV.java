package com.raphaeltannous;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PasswordManagerCSV
 */
public class PasswordManagerCSV implements PasswordManagerInterface {
    private static final String CSV_FILE = "passwords.csv";
    private static final String CSV_SPLIT_BY = "؟"; // , is in the PUNCTUATION_CHARS.

    public void addPassword(String website, String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            writer.write(website + CSV_SPLIT_BY + username + CSV_SPLIT_BY + password);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> readPasswords() {
        List<String[]> passwords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            int passwordIndex = 0;

            // Reading passwords line by line from CSV
            while ((line = br.readLine()) != null) {
                String[] csvData = line.split(CSV_SPLIT_BY);
                int csvDataLength = csvData.length;

                String[] data = new String[csvDataLength + 1];

                data[0] = Integer.toString(++passwordIndex);

                for (int i = 0; i < csvDataLength; i++) {
                    data[i+1] = csvData[i];
                }

                passwords.add(data);
            }
        } catch (IOException e) {
            System.out.println("You don't have any file or passwords.");
        }
        return passwords;
    }

    public void modifyPassword(int index, String newPassword) {
        List<String[]> passwords = readPasswords();

        for (int i = 0; i < passwords.size(); i++) {
            if ((i + 1) == index) {
                passwords.get(i)[3] = newPassword;
            }
        }
        writeAllPasswords(passwords);
    }

    public void deletePassword(int index) {
        List<String[]> passwords = readPasswords();
        for (int i = 0; i < passwords.size(); i++) {
            if ((i + 1) == index) {
                passwords.remove(i);
            }
        }
        writeAllPasswords(passwords);
    }

    private void writeAllPasswords(List<String[]> passwords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (String[] csvLine : passwords) {
                writer.write(csvLine[1] + CSV_SPLIT_BY + csvLine[2] + CSV_SPLIT_BY + csvLine[3]);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
