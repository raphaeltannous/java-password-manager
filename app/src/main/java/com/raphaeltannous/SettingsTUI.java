package com.raphaeltannous;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * SettingsTUI
 *
 * Settings' API for the TUI application.
 */
public class SettingsTUI implements SettingsInterface {
    private static final String filePath = "settingsTUI.txt";
    private static final String defaultDatabase = "SQLite";

    public SettingsTUI() {
        File file = new File(filePath);

        // If the settings file doesn't exists create a new settings file.
        if (!(file.exists() && !file.isDirectory())) {
            try (
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            ) {
                writer.write(defaultDatabase);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getDatabaseOptions() {
        String[] databaseOptions = new String[2];

        databaseOptions[0] = "CSV";
        databaseOptions[1] = "SQLite";

        return databaseOptions;
    };

    public String getCurrentDatabase() {
        String databaseSetting = defaultDatabase; // Default storage.

        try (
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
        ) {
            databaseSetting = reader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return databaseSetting;
    };

    public void setDatabaseOption() {
        try (
            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        ) {
            file.seek(0);

            String[] options = getDatabaseOptions();
            String currentSetting = file.readLine().trim();

            // Choosing from the options.
            for (int i = 0; i < options.length; i++) {
                System.out.print("Index: " + (i + 1) + ", Options: " + options[i]);

                if (currentSetting.equals(options[i])) {
                    System.out.println(" (Current)");
                } else {
                    System.out.println();
                }
            }

            int choice = UserInput.readIntFromRange("Index to use: ", 1, options.length);

            // Setting new option.
            file.seek(0);
            file.setLength(options[choice - 1].length());
            file.writeBytes(options[choice - 1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    };
}
