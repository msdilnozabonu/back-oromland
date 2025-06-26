package uz.oromland;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
    public static String encryptPassword(String rawPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(rawPassword);
    }

    public static void main(String[] args) {
        String password = "123";
        String encryptedPassword = encryptPassword(password);
        System.out.println("Original password: " + password);
        System.out.println("Encrypted password: " + encryptedPassword);
    }
}
