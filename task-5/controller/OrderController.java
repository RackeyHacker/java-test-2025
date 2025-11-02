package controller;

import entity.Customer;
import enums.OrderStatus;
import model.BookStore;

import java.util.ArrayList;
import java.util.Scanner;

public class OrderController {

    private final BookStore store;
    private final Scanner scanner = new Scanner(System.in);

    public OrderController(BookStore store) {
        this.store = store;
    }

    public void makeOrder() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();

        Customer customer = new Customer(name, email);

        store.makeOrder(store.getCart(), customer);
    }

    public void cancelOrder() {
        System.out.print("Enter id of the order to cancel: ");
        int id = scanner.nextInt();
        store.cancelOrder(id);
    }

    public void changeOrderStatus() {
        System.out.print("Enter id of the order: ");
        int id = scanner.nextInt();

        OrderStatus newStatus = null;
        while (newStatus == null) {
            System.out.print("Ender new status (NEW, COMPLETED, CANCELED): ");
            String input = scanner.nextLine().toUpperCase();
            try {
                newStatus = OrderStatus.valueOf(input);
            } catch (Exception e) {
                System.out.println("Invalid status, try again");
            }
        }
        store.changeOrderStatus(id, newStatus);
    }

    public void showOrdersSortedBy() {
        System.out.print("Enter the sorting parameter (title, publication Date, price, status): ");
        String sortingParameter = scanner.nextLine().trim();
        store.showBooksSortedBy(sortingParameter);
    }

    public void showOrderDetails() {
        System.out.print("Enter id of the order: ");
        int id = scanner.nextInt();
        store.showOrderDetails(id);
    }
}
