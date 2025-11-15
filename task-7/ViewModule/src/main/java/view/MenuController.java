package view;

import java.util.Scanner;

public class MenuController {

    private Builder builder;
    private Navigator navigator;
    private Scanner scanner = new Scanner(System.in);

    public MenuController(Navigator navigator) {
        this.navigator = navigator;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public void run() {
        while (true) {
            navigator.printMenu();
            System.out.print("Enter operation code (0 to exit): ");

            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                System.out.println("Exiting...");
                break;
            }

            try {
                int operationCode = Integer.parseInt(input);
                navigator.navigate(operationCode);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number");
            }
        }
    }
}
