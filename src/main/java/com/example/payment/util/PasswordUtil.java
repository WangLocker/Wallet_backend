package com.example.payment.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private static String salt;

    @Value("${bcrypt.salt}")
    public void setSalt(String salt) {
        PasswordUtil.salt = salt;
    }

    public static String hashPassword(String password) {
        if (salt == null) {
            throw new IllegalStateException("Salt is not initialized");
        }
        return BCrypt.hashpw(password, salt);
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }
}
