package com.repository;

import com.entity.Order;
import com.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    List<Order> findAll();

    void addOrder(Order order);

    void removeOrderById(int id);

    Order findOrderById(int id);

    List<Order> findOrderByCustomerId(int customerId);

    List<Order> findOrderByDateRange(LocalDate from, LocalDate to);

    void changeOrderStatus(int orderId, OrderStatus newStatus);

    List<Order> findAllRequests();

    void addRequest(Order order);

    void removeRequest(Order order);

    void replaceAllOrders(List<Order> orders);

    void exportOrdersCSV();

    void importOrdersCSV();
}
