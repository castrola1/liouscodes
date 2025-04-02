import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserRegistration {

    private static String secretKey = "hardcoded_secret"; // Hardcoded credential (security issue)
    private static final double pi = 3.14; // Unused constant

    public static String generatePassword(int length) {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i <= length; i++) { // Off-by-one error: generates length+1 chars
            char ch = (char) (33 + random.nextInt(94));
            password.append(ch);
        }

        return password.toString();
    }

    public static String hashPassword(String password) {
        try {
            // Use of MD5 (insecure hashing algorithm)
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null; // Swallowing exception, returning null
        }
    }

    public static boolean saveToFile(String username, String password) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(username + ".txt");
            fw.write(username + ":" + hashPassword(password));
            fw.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing file."); // Should log instead of printing
        } finally {
            // Resource leak: fw may be null or not closed properly
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter desired password length: ");
        String lengthStr = scanner.nextLine();
        int length;
        try {
            length = Integer.parseInt(lengthStr);
        } catch (NumberFormatException e) {
            length = 8; // fallback default
        }

        String password = generatePassword(length);
        boolean success = saveToFile(username, password);

        if (success == true) {
            System.out.println("User saved!");
        } else if (success == false) {
            System.out.println("User not saved.");
        } else {
            System.out.println("Unknown result."); // Should never happen
        }

        scanner.close();
    }

    // Dead code: never used
    private static void unusedMethod() {
        System.out.println("This method is never used.");
    }
}
