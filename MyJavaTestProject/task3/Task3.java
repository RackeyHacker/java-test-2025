import java.util.Random;
import java.util.Scanner;

public class Task3 {
    public static void main(String[] args) {
        SecurePassword securePassword = new SecurePassword();
        securePassword.start();
    }
}

class SecurePassword {

    private final String upper;
    private final String lower;
    private final String digits;
    private final String symbols;
    private final String[] pools;

    private final Scanner sc;

    public SecurePassword() {
        this.upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        this.lower = "abcdefghijklmnopqrstuvwxyz";
        this.digits = "0123456789";
        this.symbols = "!@#$%^&*()-_=+[]{};:,.<>/?";
        this.sc = new Scanner(System.in);
        this.pools = new String[]{upper, lower, digits, symbols};
    }

    public void start() {
        int length = getLengthPassword();
        String password = generatePassword(length);
        System.out.println("Your secure password: " + password);
    }

    private int getLengthPassword() {
        int length = 0;
        while (true) {
            System.out.print("Enter length of your future password (8-12): ");
            if (sc.hasNextInt()) {
                length = sc.nextInt();
                if (length < 8 || length > 12) {
                    System.out.println("Password length must be between 8 and 12");
                    continue;
                }
                break;
            } else {
                System.out.println("Enter a number please");
                sc.next();
            }
        }
        return length;
    }

    private String generatePassword(int length) {
        Random rand = new Random();
        char[] password = new char[length];

        password[0] = upper.charAt(rand.nextInt(upper.length()));
        password[1] = lower.charAt(rand.nextInt(lower.length()));
        password[2] = digits.charAt(rand.nextInt(digits.length()));
        password[3] = symbols.charAt(rand.nextInt(symbols.length()));

        for (int i = 4; i < length; i++) {
            String selectedPool = pools[rand.nextInt(pools.length)];
            password[i] = selectedPool.charAt(rand.nextInt(selectedPool.length()));
        }

        for (int i = 0; i < password.length; i++) {
            int j = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[j];
            password[j] = temp;
        }

        return new String(password);
    }

}
