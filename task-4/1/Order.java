package bookstore;

import java.math.BigDecimal;
import java.util.*;

public class Order {

    private static int counter = 1;
    private int id;
    private OrderStatus status;
    private boolean hasPendingRequests;
    private final List<Book> books;
    private Date executionDate;
    private BigDecimal amount = new BigDecimal("0");
    private Customer customer;

    public Order(List<Book> bookCart, Customer customer) {
        this.id = counter++;
        this.status = OrderStatus.NEW;
        this.hasPendingRequests = false;
        this.books = new ArrayList<>(bookCart);
        this.executionDate = new Date();
        for (Book book : bookCart) {
            this.amount = this.amount.add(book.getPrice());
        }
        this.customer = customer;
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

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id &&
                hasPendingRequests == order.hasPendingRequests &&
                status == order.status && Objects.equals(books, order.books) &&
                Objects.equals(executionDate, order.executionDate) &&
                Objects.equals(amount, order.amount) &&
                Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, hasPendingRequests, books, executionDate, amount, customer);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", status=" + status +
                ", hasPendingRequests=" + hasPendingRequests +
                ", books=" + books +
                ", executionDate=" + executionDate +
                ", amount=" + amount +
                ", customer=" + customer +
                '}';
    }
}
