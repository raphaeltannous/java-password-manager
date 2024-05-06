package com.raphaeltannous;

import java.security.SecureRandom;

/**
 * SecurePasswordGenerator
 */
public class SecurePasswordGenerator {

    // Defining the characters to be used
    // when generating the password.
    private final String LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String DIGITS_CHARS = "0123456789";
    private final String PUNCTUATION_CHARS = "!@#$%&*()_+-=[]|,./?><";

    private Boolean useLowerChars;
    private Boolean useUpperChars;
    private Boolean useDigitsChars;
    private Boolean usePunctuationChars;

    // Constructor
    public SecurePasswordGenerator(
        Boolean useLowerChars,
        Boolean useUpperChars,
        Boolean useDigitsChars,
        Boolean usePunctuationChars
    ) {
        this.useLowerChars = useLowerChars;
        this.useUpperChars = useUpperChars;
        this.useDigitsChars = useDigitsChars;
        this.usePunctuationChars = usePunctuationChars;
    }

    public String generatePassword(int length) {
        if (length < 16) {
            return "Length should be greater than 16.";
        }

        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();

        String charsToUse = "";

        if (useLowerChars) {
            charsToUse += LOWER_CASE_CHARS;
        }
        if (useUpperChars) {
            charsToUse += UPPER_CASE_CHARS;
        }
        if (useDigitsChars) {
            charsToUse += DIGITS_CHARS;
        }
        if (usePunctuationChars) {
            charsToUse += PUNCTUATION_CHARS;
        }

        for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(charsToUse.length());
                password.append(charsToUse.charAt(randomIndex));
        }

        return password.toString();
    }
}
