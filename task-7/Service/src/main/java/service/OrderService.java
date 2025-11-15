package service;

import entity.Book;
import entity.Customer;
import entity.Order;
import enums.OrderStatus;

import java.util.List;

public interface OrderService {

    Order makeOrder(Customer customer, List<Book> cart);

    void cancelOrder(int id);

    void changeOrderStatus(int id, OrderStatus newStatus);

    List<Order> showOrdersSortedBy(String sortBy);

    Order getOrderDetails(int id);

    List<Order> processBookRequests(Book availableBook);
}
