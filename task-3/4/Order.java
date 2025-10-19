package bookstore;

import java.util.*;

public class Order {

    private static int counter = 1;
    private int id;
    private OrderStatus status;
    private boolean hasPendingRequests;
    private final List<Book> books;

    public Order(List<Book> bookCart) {
        this.id = counter++;
        this.status = OrderStatus.NEW;
        this.hasPendingRequests = false;
        this.books = new ArrayList<>(bookCart);
    }

    public static int getCounter() {
        return counter;
    }

    public static void setCounter(int counter) {
        Order.counter = counter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isHasPendingRequests() {
        return hasPendingRequests;
    }

    public void setHasPendingRequests(boolean hasPendingRequests) {
        this.hasPendingRequests = hasPendingRequests;
    }

    public List<Book> getBooks() {
        return books;
    }
}
