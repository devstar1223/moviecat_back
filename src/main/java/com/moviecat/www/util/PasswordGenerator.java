package com.moviecat.www.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {

    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%&*?";

    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + NUMBER + OTHER_CHAR;

    private static SecureRandom random = new SecureRandom();

    public static String generatePassword(int length) {
        if (length < 6) {
            throw new IllegalArgumentException("Password length must be at least 6 characters.");
        }
        StringBuilder password = new StringBuilder(length);
        // ensure at least one lower-case character
        password.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        // ensure at least one digit
        password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));

        // generate rest of the password
        for (int i = 2; i < length; i++) {
            // pick a random character from the set of PASSWORD_ALLOW_BASE characters
            password.append(PASSWORD_ALLOW_BASE.charAt(random.nextInt(PASSWORD_ALLOW_BASE.length())));
        }

        // insert a random special character at the end
        password.insert(random.nextInt(length), OTHER_CHAR.charAt(random.nextInt(OTHER_CHAR.length())));

        // shuffle characters in password
        String shuffledPassword = shuffleString(password.toString());
        return shuffledPassword;
    }

    private static String shuffleString(String string) {
        char[] passwordArray = string.toCharArray();
        for (int i = 0; i < passwordArray.length; i++) {
            int randomIndex = random.nextInt(passwordArray.length);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[randomIndex];
            passwordArray[randomIndex] = temp;
        }
        return new String(passwordArray);
    }

    public static void main(String[] args) {
        // Example of generating a password with length 10
        String password = generatePassword(10);
        System.out.println("Generated Password: " + password);
    }
}

