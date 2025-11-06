package view.views;

import entity.Book;
import entity.Order;
import enums.OrderStatus;
import controller.OrderController;

import java.util.List;
import java.util.Scanner;

public class OrderView {

    private OrderController controller;
    private final Scanner scanner = new Scanner(System.in);

    public OrderView(OrderController controller) {
        this.controller = controller;
    }

    public void makeOrder() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();

        String success = controller.makeOrder(name, email);
        System.out.println(success);
    }

    public void cancelOrder() {
        System.out.print("Enter id of the order to cancel: ");
        int id = scanner.nextInt();
        String success = controller.cancelOrder(id);
        System.out.println(success);
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

        String changedStatus = controller.changeOrderStatus(id, newStatus);
        System.out.println(changedStatus);
    }

    public void showOrdersSortedBy() {
        System.out.print("Enter the sorting parameter (executionDate, amount, status): ");
        String sortingParameter = scanner.nextLine().trim();
        List<Order> sortedOrders = controller.showOrdersSortedBy(sortingParameter);
        if (sortedOrders == null) {
            System.err.println("Incorrect SORTING PARAMETER");
        } else if (sortedOrders.isEmpty()) {
            System.out.println("No orders yet");
        } else {
            printOrders(sortedOrders);
        }
    }

    private void printOrders(List<Order> orderList) {
        for (int i = 0; i < orderList.size(); i++) {
            System.out.println((i + 1) + ". " + orderList.get(i));
        }
    }

    public void showOrderDetails() {
        System.out.print("Enter order ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Order order = controller.getOrderDetails(id);
        if (order == null) {
            System.out.println("Order not found");
        } else {
            System.out.println("Order #" + order.getId());
            System.out.println("Customer: " + order.getCustomer().getName());
            System.out.println("Email: " + order.getCustomer().getEmail());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Books:");
            for (Book b : order.getBooks()) {
                System.out.println(" - " + b.getTitle() + " (" + b.getPrice() + ")");
            }
        }
    }


}
