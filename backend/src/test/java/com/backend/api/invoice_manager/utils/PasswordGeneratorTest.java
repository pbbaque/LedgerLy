package com.backend.api.invoice_manager.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordGeneratorTest {

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:,.<>?";

    private final PasswordGenerator passwordGenerator = new PasswordGenerator();

    @Test
    void generatesPasswordsWithExpectedLengthAndAllowedCharacters() {
        String password = passwordGenerator.generateRandomPassword();

        assertEquals(12, password.length());
        assertTrue(password.chars().allMatch(character -> ALLOWED_CHARACTERS.indexOf(character) >= 0));
    }

    @Test
    void generatesDifferentPasswordsAcrossCalls() {
        String firstPassword = passwordGenerator.generateRandomPassword();
        String secondPassword = passwordGenerator.generateRandomPassword();

        assertNotEquals(firstPassword, secondPassword);
    }
}
