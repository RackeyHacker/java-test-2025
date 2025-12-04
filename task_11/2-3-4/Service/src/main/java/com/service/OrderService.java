package com.service;

import com.entity.Book;
import com.entity.Customer;
import com.entity.Order;
import com.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    Order makeOrder(Customer customer, List<Book> cart);

    void cancelOrder(int id);

    void changeOrderStatus(int id, OrderStatus newStatus);

    List<Order> showOrdersSortedBy(String sortBy);

    Order getOrderDetails(int id);

    List<Order> processBookRequests(Book availableBook);
}
