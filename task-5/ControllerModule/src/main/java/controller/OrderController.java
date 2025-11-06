package controller;

import entity.Customer;
import entity.Order;
import enums.OrderStatus;
import model.BookStore;

import java.util.List;
import java.util.Scanner;

public class OrderController {

    private final BookStore store;
    private final Scanner scanner = new Scanner(System.in);

    public OrderController(BookStore store) {
        this.store = store;
    }

    public String makeOrder(String name, String email) {
        Customer customer = new Customer(name, email);
        return store.makeOrder(store.getCart(), customer);
    }

    public String cancelOrder(int id) {
        return store.cancelOrder(id);
    }

    public String changeOrderStatus(int id, OrderStatus newStatus) {
        return store.changeOrderStatus(id, newStatus);
    }

    public List<Order> showOrdersSortedBy(String sortingParameter) {
        return store.showOrdersSortedBy(sortingParameter);
    }

    public Order getOrderDetails(int id) {
        return store.findOrderById(id);
    }
}
