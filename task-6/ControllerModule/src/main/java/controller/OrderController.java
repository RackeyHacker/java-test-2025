package controller;

import entity.Customer;
import entity.Order;
import enums.OrderStatus;
import model.BookStore;

import java.util.List;

public class OrderController {

    private final BookStore store;

    public OrderController(BookStore store) {
        this.store = store;
    }

    public Order makeOrder(String name, String email) {
        Customer customer = new Customer(name, email);
        return store.makeOrder(store.getCart(), customer);
    }

    public void cancelOrder(int id) {
        store.cancelOrder(id);
    }

    public void changeOrderStatus(int id, OrderStatus newStatus) {
        store.changeOrderStatus(id, newStatus);
    }

    public List<Order> showOrdersSortedBy(String sortingParameter) {
        return store.showOrdersSortedBy(sortingParameter);
    }

    public Order getOrderDetails(int id) {
        return store.findOrderById(id);
    }
}
