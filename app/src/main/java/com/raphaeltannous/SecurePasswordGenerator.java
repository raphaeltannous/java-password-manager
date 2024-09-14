package com.raphaeltannous;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * SecurePasswordGenerator
 */
public class SecurePasswordGenerator {
    // Defining the characters to be used
    // when generating the password.
    public static final String LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS_CHARS = "0123456789";
    public static final String PUNCTUATION_CHARS = "!@#$%&*()_+-=[]|,./?><";

    public static int maxNumberofWhitesSpaces(int length) {
        return (int) Math.ceil(length * 0.15);
    }

    public static String generatePassword(
        int length,
        int numberOfWhiteSpaces,
        boolean useLower,
        boolean useUpper,
        boolean useDigits,
        boolean usePunctuation
    ) {
        if (length < 16) {
            numberOfWhiteSpaces = 0;
        }

        // 15% of the code can be spaces
        if (numberOfWhiteSpaces > maxNumberofWhitesSpaces(length)) {
           throw new IllegalArgumentException("number of white spaces cannot exeed 15% of the length.");
        }

        if (
            (useLower || useUpper || useDigits || usePunctuation) == false
        ) {
           throw new IllegalArgumentException("At least enable useLower, useUpper, useDigits or usePunctuation");
        }

        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();

        String charsToUse = "";

        if (useLower) {
            charsToUse += LOWER_CASE_CHARS;
        }
        if (useUpper) {
            charsToUse += UPPER_CASE_CHARS;
        }
        if (useDigits) {
            charsToUse += DIGITS_CHARS;
        }
        if (usePunctuation) {
            charsToUse += PUNCTUATION_CHARS;
        }

        for (int i = 0; i < length - numberOfWhiteSpaces; i++) {
            int randomIndex = random.nextInt(charsToUse.length());
            password.append(charsToUse.charAt(randomIndex));
        }

        List<Integer> usedNumbers = new ArrayList<>();

        int i = 0;
        while (i < numberOfWhiteSpaces) {
            int randomIndex = random.nextInt(1, length - 1);

            if (usedNumbers.indexOf(randomIndex) != -1) {
                continue;
            }

            usedNumbers.add(randomIndex);
            password.insert(randomIndex, " ");
            i++;
        }

        return password.toString();
    }
}
