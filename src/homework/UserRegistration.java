package homework;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class UserRegistration {
    private final static Logger log = Logger.getLogger(UserRegistration.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        log.log(Level.INFO, "User Registration");
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (!validateFullName(fullName)) {
            log.log(Level.SEVERE, "Invalid Full Name");
            return;
        }

        if (!validateEmail(email)) {
            log.log(Level.SEVERE, "Invalid Email");
            return;
        }

        if (!validatePassword(password)) {
            log.log(Level.SEVERE, "Invalid Password");
            return;
        }

        log.log(Level.INFO, "Registration Successful!");
        log.log(Level.INFO, "Full Name: " + fullName);
        log.log(Level.INFO, "Email: " + email);
        log.log(Level.INFO, "Phone Number: " + phoneNumber);
    }

    private static boolean validateFullName(String fullName) {
        String[] nameParts = fullName.split(" ");
        if (nameParts.length < 2) {
            return false;
        }

        for (String namePart : nameParts) {
            if (!Pattern.matches("[A-Z][a-zA-Z]*", namePart)) {
                return false;
            }
        }

        return true;
    }

    private static boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private static boolean validatePassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&*()?/|\\\\%_\\-+,.<>~]).{8,}$";
        return Pattern.matches(passwordRegex, password);
    }
}