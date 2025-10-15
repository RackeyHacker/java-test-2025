import java.util.Scanner;

public class Task2 {
    public static void main(String[] args) {
        Converter converter = new Converter();
        converter.start();
    }
}

class Converter {

    private final double USD_RATE = 83.65;
    private final double EUR_RATE = 98.69;
    private final double KZT_RATE = 0.15;
    private final double CNY_RATE = 11.76;

    private double amountRUB;
    private double amountUSD;
    private double amountEUR;
    private double amountKZT;
    private double amountCNY;

    private final Scanner sc;

    Converter() {
        sc = new Scanner(System.in);
    }

    public void start() {
        int currencyChoice = getCurrency();
        double inputAmount = getAmount();
        convertToRUB(currencyChoice, inputAmount);
        convertFromRUB();
        printConverters();
    }

    private int getCurrency() {
        int choice = 0;
        while (true) {
            System.out.println("\nSelect the number of the currency you want to manage:" +
                    "\n1.RUB\n2.USD\n3.EUR\n4.KZT\n5.CNY");
            System.out.print("Enter number: ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                if (choice >= 1 && choice <= 5) {
                    break;
                } else {
                    System.out.println("Enter a number from 1 to 5");
                }
            } else {
                System.out.println("Enter a number please");
                sc.next();
            }
        }
        return choice;
    }

    private double getAmount() {
        double amount = -1;
        while (true) {
            System.out.print("Enter amount: ");
            if (sc.hasNextDouble()) {
                amount = sc.nextDouble();
                if (amount < 0) {
                    System.out.println("Amount can't be negative");
                    continue;
                }
                break;
            } else {
                System.out.println("Enter a number please");
                sc.nextDouble();
            }
        }
        return amount;
    }


    private void convertToRUB(int currencyChoice, double inputAmount) {
        switch (currencyChoice) {
            case 1:
                amountRUB = inputAmount;
                break;
            case 2:
                amountRUB = inputAmount * USD_RATE;
                break;
            case 3:
                amountRUB = inputAmount * EUR_RATE;
                break;
            case 4:
                amountRUB = inputAmount * KZT_RATE;
                break;
            case 5:
                amountRUB = inputAmount * CNY_RATE;
                break;
            default:
                System.out.println("Invalid selection");
                System.exit(0);
        }
    }

    private void convertFromRUB() {
        amountUSD = amountRUB / USD_RATE;
        amountEUR = amountRUB / EUR_RATE;
        amountKZT = amountRUB / KZT_RATE;
        amountCNY = amountRUB / CNY_RATE;
    }

    private void printConverters() {
        System.out.println("\n\nConverted amounts:");
        System.out.printf("RUB: %.2f\n", amountRUB);
        System.out.printf("USD: %.2f\n", amountUSD);
        System.out.printf("EUR: %.2f\n", amountEUR);
        System.out.printf("KZT: %.2f\n", amountKZT);
        System.out.printf("CNY: %.2f\n", amountCNY);
    }
}
