package com.raphaeltannous;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class PasswordManagerSQLiteTest {

    private void printArray(String[]... arrays) {
        for (String[] array : arrays) {
            System.out.print("|");

            for (int i = 0; i < array.length; i++) {
                System.out.print(" " + array[i] + " |");
            }

            System.out.println();
        }
    }

    private boolean isListEqualList(List<String[]> firstList, List<String[]> secondList) {
        if (firstList.size() != secondList.size()) {
            System.out.println("Not same size.");
            return false;
        }

        for (int i = 0; i < firstList.size(); i++) {
            String[] firstArray = firstList.get(i);
            String[] secondArray = secondList.get(i);

            if (!Arrays.equals(firstArray, secondArray)) {
                printArray(firstArray, secondArray);

                return false;
            }
        }

        return true;
    }

    @Test
    void passwordsTest() {
        Path databasePath = Paths.get("passwords.db");
        String databasePassword = "123";

        PasswordManagerSQLite db = new PasswordManagerSQLite(
            databasePath,
            databasePassword
        );

        // Adding 2 Passwords
        db.addPassword(
            "https://youtube.com/",
            "tester",
            "a0e564984bca24c5"
        );

        db.addPassword(
            "https://music.apple.com/",
            "tester",
            "cf0fbe3142af64fc"
        );

        // Changing website for password 1
        db.modifyWebsite(1, "https://chat.openai.com/");

        // Changing username for password 2
        db.modifyUsername(2, "testerWithStyle");

        // Changing password for password 1
        db.modifyPassword(1, "61d17dc939607d18c0fe00e5de4d27e1");

        // Adding a password and deleting password 2
        db.addPassword("https://www.instagram.com/", "tester", "67157420020d");

        db.deletePassword(2);

        List<String[]> fetchedPasswords = db.fetchPasswords();

        List<String[]> expectedFetchedPasswords = new ArrayList<>();

        expectedFetchedPasswords.add(
            new String[]{
                "1",
                "https://chat.openai.com/",
                "tester",
                "61d17dc939607d18c0fe00e5de4d27e1",
                "",
                "0",
                ""
            }
        );

        expectedFetchedPasswords.add(
            new String[]{
                "3",
                "https://www.instagram.com/",
                "tester",
                "67157420020d",
                "",
                "0",
                ""
            }
        );

        // Changing OTP for password 1
        boolean preFetchOTP = db.fetchOTP(1).equals("");

        db.modifyOTP(1, "86a1809af1db");

        boolean postFetchOTP = db.fetchOTP(1).equals("86a1809af1db");

        boolean otpResult = preFetchOTP && postFetchOTP;

        if (!otpResult) {
            System.out.println("Something went wrong in OTP (preFetchOTP postFetchOTP): ");
            System.out.println(preFetchOTP + " " + postFetchOTP);
        }

        // Changing Note for password 3
        boolean preFetchNote = db.fetchNote(3).equals("");

        db.modifyNote(3, "instagram note");

        boolean postFetchNote = db.fetchNote(3).equals("instagram note");

        boolean noteResult = preFetchNote && postFetchNote;

        if (!noteResult) {
            System.out.println("Something went wrong in Note (preFetchNote postFetchNote): ");
            System.out.println(preFetchNote + " " + postFetchNote);
        }

        // Adding backup codes for password 1
        List<String[]> preFetchedBackupCodes = db.fetchBackupCodes(1);

        List<String[]> expectedPreFetchedBackupCodes = new ArrayList<>();

        boolean preFetchBackupCodesStatus = isListEqualList(preFetchedBackupCodes, expectedPreFetchedBackupCodes);

        db.addBackupCode(1, "5774d4761370");

        // Chaning Has Backup Code Status
        db.updateHasBackupCodeStatus(1, 1);

        db.addBackupCode(1, "6e56a0a66191");

        db.updateBackupCodeStatus(2, 1); // 6e56a0a66191

        // Removing backup code 1
        db.removeBackupCode(1);

        List<String[]> postFetchedBackupCodes = db.fetchBackupCodes(1);

        List<String[]> expectedPostFetchedBackupCodes = new ArrayList<>();

        expectedPostFetchedBackupCodes.add(
            new String[] {
                "2",
                "1",
                "6e56a0a66191",
                "1"
            }
        );

        boolean postFetchBackupCodesStatus = isListEqualList(postFetchedBackupCodes, expectedPostFetchedBackupCodes);

        boolean backupCodesResult = preFetchBackupCodesStatus && postFetchBackupCodesStatus;

        assertTrue(
            isListEqualList(expectedFetchedPasswords, fetchedPasswords) &&
            otpResult &&
            noteResult &&
            backupCodesResult
        );
    }
}
