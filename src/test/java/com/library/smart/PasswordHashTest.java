package com.library.smart;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordHashTest {

    @Test
    void generateHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("admin123");
        System.out.println("=== BCrypt Hash for 'admin123' ===");
        System.out.println(hash);
        System.out.println("=== Verification ===");
        System.out.println("Matches: " + encoder.matches("admin123", hash));
    }
}
