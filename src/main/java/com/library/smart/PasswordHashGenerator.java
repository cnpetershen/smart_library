package com.library.smart;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.CommandLineRunner;

public class PasswordHashGenerator implements CommandLineRunner {
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void run(String... args) {
        String password = "admin123";
        String hash = passwordEncoder.encode(password);
        System.out.println("BCrypt Hash for 'admin123': " + hash);
        System.out.println("Matches: " + passwordEncoder.matches(password, hash));
        System.out.println("Hash length: " + hash.length());
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("admin123");
        System.out.println(hash);
    }
}
