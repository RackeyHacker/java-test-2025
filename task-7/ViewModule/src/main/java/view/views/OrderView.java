package view.views;

import controller.OrderController;
import entity.Book;
import entity.Order;
import enums.OrderStatus;

import java.util.List;
import java.util.Scanner;

public class OrderView {

    private final OrderController controller;
    private final Scanner scanner = new Scanner(System.in);

    public OrderView(OrderController controller) {
        this.controller = controller;
    }

    public void makeOrder() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();

        try {
            Order order = controller.makeOrder(name, email);

            if (order.isHasPendingRequests()) {
                System.out.println("Order #" + order.getId() + " sent for revision. ");
                System.out.println("Unavailable books:");

                for (Book book : order.getBooks()) {
                    System.out.println(" - " + book.getTitle());
                }
            } else {
                System.out.println("Order #" + order.getId() + " was created successfully");
            }

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void cancelOrder() {
        System.out.print("Enter order ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            controller.cancelOrder(id);
            System.out.println("Order #" + id + " cancelled successfully");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void changeOrderStatus() {
        System.out.print("Enter order ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        OrderStatus newStatus = null;
        while (newStatus == null) {
            System.out.print("Enter new status (NEW, COMPLETED, CANCELED): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                newStatus = OrderStatus.valueOf(input);
            } catch (Exception e) {
                System.out.println("Invalid status, try again");
            }
        }

        try {
            controller.changeOrderStatus(id, newStatus);
            System.out.println("Status changed successfully");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void showOrdersSortedBy() {
        System.out.print("Enter sorting parameter (executionDate, amount, status): ");
        String sortingParameter = scanner.nextLine().trim();

        try {
            List<Order> orders = controller.showOrdersSortedBy(sortingParameter);
            if (orders.isEmpty()) {
                System.out.println("No orders yet.");
            } else {
                printOrders(orders);
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private void printOrders(List<Order> orders) {
        for (int i = 0; i < orders.size(); i++) {
            System.out.println((i + 1) + ". " + orders.get(i));
        }
    }

    public void showOrderDetails() {
        System.out.print("Enter order ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        try {
            Order order = controller.getOrderDetails(id);
            System.out.println("Order #" + order.getId());
            System.out.println("Customer: " + order.getCustomer().getName());
            System.out.println("Email: " + order.getCustomer().getEmail());
            System.out.println("Status: " + order.getStatus());

            System.out.println("Books:");
            for (Book b : order.getBooks()) {
                System.out.println(" - " + b.getTitle() + " (" + b.getPrice() + ")");
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
