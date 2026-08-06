package com.smartlibrary.util;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!";

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordGenerator() {
        // Prevent object creation
    }

    public static String generatePassword(int length) {

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {

            int index = RANDOM.nextInt(CHARACTERS.length());

            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }
}