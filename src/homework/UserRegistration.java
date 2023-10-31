package homework;

import java.util.Scanner;
import java.util.regex.Pattern;

public class UserRegistration {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("User Registration");
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (!validateFullName(fullName)) {
            System.out.println("Invalid Full Name");
            return;
        }

        if (!validateEmail(email)) {
            System.out.println("Invalid Email");
            return;
        }

        if (!validatePassword(password)) {
            System.out.println("Invalid Password");
            return;
        }

        System.out.println("Registration Successful!");
        System.out.println("Full Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phoneNumber);
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
