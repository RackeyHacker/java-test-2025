package controller;

import entity.Customer;
import entity.Order;
import enums.OrderStatus;
import service.CartService;
import service.OrderService;

import java.util.List;

public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
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
