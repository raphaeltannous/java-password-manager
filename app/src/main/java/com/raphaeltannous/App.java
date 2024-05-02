package com.raphaeltannous;

import java.util.List;

public class App {
    public static void main(String[] args) {
        SettingsInterface settings = new SettingsTUI();

        while (true) {
            String command = UserInput.readCommand("Command (type 'help' for help): ");

            switch (command) {
                case "help":
                    System.out.println("Command List:");
                    System.out.println();
                    System.out.println("help\t\tShows this help page");
                    System.out.println("about\t\tAbout the program");
                    System.out.println("version\t\tShows the version of the program");
                    System.out.println("manager\t\tEnters the manager command line to manage passwords");
                    System.out.println("generator\tGenerates a custom password");
                    System.out.println("settings\tSet and get settings");
                    System.out.println("quit\t\tQuit out of the program");
                    System.out.println("exit\t\tSame as quit");
                    break;
                case "version":
                    System.out.println("Version 0.1.0");
                    break;
                case "about":
                    System.out.println("Author: Raphael Tannous (rofe33)");
                    System.out.println();
                    System.out.println("Source repository: https://github.com/rofe33/java-password-manager");
                    System.out.println();
                    System.out.println("License: GNU General Public License Version 3 (GPLv3)");
                    System.out.println();
                    System.out.println("All The Glory To Christ Jesus...");
                    break;
                case "manager":
                    runManager(settings);
                    break;
                case "generator":
                    String password = generatePassword();
                    System.out.println("Generated Password: " + password);
                    break;
                case "settings":
                    runSettings(settings);
                    break;
                case "quit":
                case "exit":
                    UserInput.scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command. Enter 'help' to know all the commands.");
            }
        }
    }

    private static String generatePassword() {
        System.out.println("Please enter your preferences: (true/false) (Default: true)");
        Boolean useLowerCaseChars = UserInput.readBooleanInput("Use lower case characters? ");
        Boolean useUpperCaseChars = UserInput.readBooleanInput("Use upper case characters? ");
        Boolean useDigitsChars = UserInput.readBooleanInput("Use digit characters? ");
        Boolean usePunctuationChars = UserInput.readBooleanInput("Use punctuation characters? ");
        int passwordLength = UserInput.readInt("Enter the length of the password (Default: 16): ", "Password length should be greater than or equal 16.", 16, 16); // Default length is 16

        SecurePasswordGenerator generator = new SecurePasswordGenerator(
            useLowerCaseChars,
            useUpperCaseChars,
            useDigitsChars,
            usePunctuationChars
        );

        String password = generator.generatePassword(passwordLength);
        return password;
    }

    private static void runSettings(SettingsInterface settings) {
        loop: while (true) {
            String settingsCommand = UserInput.readCommand("Settings' Command (type 'help' for help): ");

            switch (settingsCommand) {
                case "help":
                    System.out.println("Settings' Command List:");
                    System.out.println();
                    System.out.println("help\t\tShows this help page");
                    System.out.println("status\t\tList the available storage settings and shows the current setting");
                    System.out.println("set\t\tChange current setting");
                    System.out.println("quit\t\tQuit out of the settings");
                    System.out.println("exit\t\tExit out of the program");
                    break;
                case "status":
                    String[] options = settings.getDatabaseOptions();
                    String currentSetting = settings.getCurrentDatabase();

                    for (int i = 0; i < options.length; i++) {
                        System.out.print("Index: " + (i + 1) + ", Options: " + options[i]);

                        if (currentSetting.equals(options[i])) {
                            System.out.println(" (Current)");
                        } else {
                            System.out.println();
                        }
                    }
                    break;
                case "set":
                    settings.setDatabaseOption();
                    break;
                case "quit":
                    break loop;
                case "exit":
                    UserInput.scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command. Enter 'help' to know all the commands.");
                    break;
            }
        }
    }

    private static void runManager(SettingsInterface settings) {
        String whichPasswordManager = settings.getCurrentDatabase();
        PasswordManagerInterface passwordManager;

        switch (whichPasswordManager) {
            case "CSV":
                passwordManager = new PasswordManagerCSV();
                break;
            case "SQLite":
            default:
                passwordManager = new PasswordManagerSQLite();
                break;
        }

        loop: while (true) {
            String managerCommand = UserInput.readCommand("Manager's Command (type 'help' for help): ");

            switch (managerCommand) {
                case "help":
                    System.out.println("Manager's Command List:");
                    System.out.println();
                    System.out.println("help\t\tShows this help page");
                    System.out.println("list\t\tShows all the saved passwords");
                    System.out.println("add\t\tAdds a new password");
                    System.out.println("modify\t\tModifies an existent password");
                    System.out.println("delete\t\tDeletes a password");
                    System.out.println("quit\t\tQuit manager");
                    System.out.println("exit\t\tExit the whole program");
                    break;
                case "list":
                    List<String[]> passwords = passwordManager.readPasswords();

                    if (passwords.size() == 0) {
                        System.out.println("There's no passwords.");
                        break;
                    }

                    for (int i = 0; i < passwords.size(); i++) {
                        String[] passwordData = passwords.get(i);
                        System.out.println(
                            "Index: " + (i + 1) +
                            ", Website: " + passwordData[1] +
                            ", Username: " + passwordData[2] +
                            ", Password: " + passwordData[3]
                        );
                    }
                    break;
                case "add":
                    System.out.print("Enter website: ");
                    String website = UserInput.scanner.nextLine();

                    System.out.print("Enter username: ");
                    String username = UserInput.scanner.nextLine();

                    System.out.println("Generating a Password...");
                    String password = generatePassword();

                    passwordManager.addPassword(website, username, password);
                    break;
                case "modify":
                    passwords = passwordManager.readPasswords();

                    if (passwords.size() == 0) {
                        System.out.println("There's no passwords.");
                        break;
                    }

                    for (int i = 0; i < passwords.size(); i++) {
                        String[] passwordData = passwords.get(i);
                        System.out.println(
                            "Index: " + (i + 1) +
                            ", Website: " + passwordData[1] +
                            ", Username: " + passwordData[2] +
                            ", Password: " + passwordData[3]
                        );
                    }

                    int modifyIndexAt = UserInput.readIntFromRange("Index to update password: ", 1, passwords.size()) - 1;

                    int index = Integer.parseInt(passwords.get(modifyIndexAt)[0]);

                    System.out.println("Generating a new Password...");
                    String newPassword = generatePassword();


                    passwordManager.modifyPassword(index, newPassword);

                    break;
                case "delete":
                    passwords = passwordManager.readPasswords();

                    if (passwords.size() == 0) {
                        System.out.println("There's no passwords.");
                        break;
                    }

                    for (int i = 0; i < passwords.size(); i++) {
                        String[] passwordData = passwords.get(i);
                        System.out.println(
                            "Index: " + (i + 1) +
                            ", Website: " + passwordData[1] +
                            ", Username: " + passwordData[2] +
                            ", Password: " + passwordData[3]
                        );
                    }

                    int deleteIndexAt = UserInput.readIntFromRange("Index to delete: ", 1, passwords.size()) - 1;

                    int realIndex = Integer.parseInt(passwords.get(deleteIndexAt)[0]);

                    passwordManager.deletePassword(realIndex);
                    break;
                case "quit":
                    break loop;
                case "exit":
                    UserInput.scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command. Enter 'help' to know all the commands.");
            }
        }
    }
}
