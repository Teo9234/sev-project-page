package com.clock_in.clock.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "yourpassword"; // Replace this with the password you want to set
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
