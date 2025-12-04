package com.service.impl;

import com.annotations.Autowired;
import com.annotations.Service;
import com.entity.Book;
import com.entity.Customer;
import com.entity.Order;
import com.enums.BookStatus;
import com.enums.OrderStatus;
import com.repository.CustomerRepository;
import com.repository.OrderRepository;
import com.service.BookService;
import com.service.CartService;
import com.service.OrderService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BookService bookService;
    @Autowired
    private CartService cartService;

    public OrderServiceImpl() {
    }

    @Override
    public Order makeOrder(Customer customer, List<Book> cart) {
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("The cart is empty");
        }

        Customer existing = customerRepository.findCustomerByEmail(customer.getEmail());

        if (existing == null) {
            customerRepository.addCustomer(customer);
            existing = customer;
        }

        Order order = new Order(new ArrayList<>(cart), existing);

        List<Book> unavailableBooks = findUnavailableBooks(cart);

        if (!unavailableBooks.isEmpty()) {
            for (Book book : unavailableBooks) {
                book.incrementRequestCount();
            }
            return handleOrderWithUnavailableBooks(order);
        }
        return handleSuccessfulOrder(order);
    }

    private Order handleOrderWithUnavailableBooks(Order order) {
        order.setHasPendingRequests(true);
        orderRepository.addRequest(order);
        cartService.clearCart();
        return order;
    }

    private Order handleSuccessfulOrder(Order order) {
        orderRepository.addOrder(order);
        cartService.clearCart();
        return order;
    }

    private List<Book> findUnavailableBooks(List<Book> cart) {
        List<Book> unavailableBooks = new ArrayList<>();
        for (Book book : cart) {
            if (book == null) throw new IllegalArgumentException("The book is null");
            if (book.getStatus() == BookStatus.UNAVAILABLE) {
                unavailableBooks.add(book);
            }
        }
        return unavailableBooks;
    }

    @Override
    public void cancelOrder(int id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid order ID");

        Order order = orderRepository.findOrderById(id);
        if (order == null) throw new NoSuchElementException("Order with id " + id + " not found");

        order.setStatus(OrderStatus.CANCELED);
    }

    @Override
    public void changeOrderStatus(int id, OrderStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("New status is null");

        Order order = orderRepository.findOrderById(id);
        if (order == null) throw new NoSuchElementException("Order with id " + id + " not found");

        order.setStatus(newStatus);
    }

    @Override
    public List<Order> showOrdersSortedBy(String sortBy) {
        List<Order> orders = orderRepository.findAll();

        switch (sortBy) {
            case "executionDate":
                orders.sort(Comparator.comparing(Order::getExecutionDate));
                break;
            case "amount":
                orders.sort(Comparator.comparing(Order::getAmount));
                break;
            case "status":
                orders.sort(Comparator.comparing(Order::getStatus));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort parameter: " + sortBy);
        }

        return orders;
    }

    @Override
    public Order getOrderDetails(int id) {
        return orderRepository.findOrderById(id);
    }

    @Override
    public List<Order> processBookRequests(Book availableBook) {
        List<Order> readyOrders = new ArrayList<>();

        List<Order> requests = orderRepository.findAllRequests();

        for (Order order : requests) {
            boolean containsThisBook = order.getBooks().stream()
                    .anyMatch(b -> b.getTitle().equals(availableBook.getTitle()));
            if (!containsThisBook) continue;

            boolean allAvailable = order.getBooks().stream()
                    .allMatch(b -> b.getStatus() == BookStatus.AVAILABLE);

            if (allAvailable) {
                order.setHasPendingRequests(false);
                order.setStatus(OrderStatus.NEW);
                readyOrders.add(order);
            }
        }

        for (Order order : readyOrders) {
            orderRepository.removeRequest(order);
            orderRepository.addOrder(order);
        }

        return readyOrders;
    }
}
