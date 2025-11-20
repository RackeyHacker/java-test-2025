package com.controller;

import com.annotations.Autowired;
import com.annotations.Controller;
import com.entity.Customer;
import com.entity.Order;
import com.enums.OrderStatus;
import com.service.CartService;
import com.service.OrderService;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;

    public OrderController() {
    }

    public Order makeOrder(String name, String email) {
        Customer customer = new Customer(name, email);
        return orderService.makeOrder(customer, cartService.getCart());
    }

    public void cancelOrder(int id) {
        orderService.cancelOrder(id);
    }

    public void changeOrderStatus(int id, OrderStatus newStatus) {
        orderService.changeOrderStatus(id, newStatus);
    }

    public List<Order> showOrdersSortedBy(String sortingParameter) {
        return orderService.showOrdersSortedBy(sortingParameter);
    }

    public Order getOrderDetails(int id) {
        return orderService.getOrderDetails(id);
    }
}
