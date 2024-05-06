package com.raphaeltannous;

import java.util.Scanner;

/**
 * UserInput
 *
 * Provides the functions necessary to get
 * user input.
 *
 */
public class UserInput {
    public static Scanner scanner = new Scanner(System.in);

    public static int readIntFromRange(String message, int minValue, int maxValue) {
        int result = 0;
        boolean isValid = false;

        do {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                result = Integer.parseInt(input);

                if (result >= minValue && result <= maxValue) {
                    isValid = true;
                } else {
                    System.out.println("Please enter an integer within the range [" + minValue + ", " + maxValue + "].");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        } while (!isValid);

        return result;
    }

    public static int readInt(String message, String minValueMessage, int defaultValue, int minValue) {
        int result = 0;
        boolean isValid = false;

        do {
            System.out.print(message);
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                return defaultValue;
            }

            try {
                result = Integer.parseInt(input);

                if (result >= minValue) {
                    isValid = true;
                } else {
                    System.out.println(minValueMessage);
                    isValid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        } while (!isValid);

        return result;
    }

    public static boolean readBooleanInput(String message) {
        boolean isValid = false;
        boolean result = false;

        do {
            System.out.print(message);
            String input = scanner.nextLine().toLowerCase();

            switch (input) {
                case "":
                case "true":
                    result = true;
                    isValid = true;
                    break;
                case "false":
                    result = false;
                    isValid = true;
                    break;
                default:
                    System.out.println("Please enter 'true' or 'false' (Default: true).");
                    break;
            }
        } while (!isValid);

        return result;
    }

    public static String readCommand(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }
}
