package com.userwebapp.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    /**
     * Verifies a plain‐text password against the stored BCrypt hash.
     * Returns false if the hash is invalid or the check fails.
     */
    public static boolean safeCheck(String plain, String storedHash) {
        if (storedHash == null || storedHash.length() < 29) {
            return false;
        }
        try {
            return BCrypt.checkpw(plain, storedHash);
        } catch (IllegalArgumentException ex) {
            // thrown if the hash format is invalid
            return false;
        }
    }

    /**
     * Hashes the plain‐text password using BCrypt with a work factor of 12.
     * Higher log rounds (e.g., 12–14) increase security at the cost of CPU time.
     */
    public static String hash(String plainPassword) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainPassword, salt);
    }
}