package com.service.impl;

import com.annotations.Autowired;
import com.annotations.Service;
import com.entity.Order;
import com.repository.OrderRepository;
import com.service.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderRepository orderRepository;

    public ReportServiceImpl() {
    }

    @Override
    public List<Order> showOrderForDateSortedBy(LocalDate startDate, LocalDate endDate, String sortBy) {
        List<Order> sortedOrders = orderRepository.findOrderByDateRange(startDate, endDate);

        switch (sortBy) {
            case "date":
                sortedOrders.sort(Comparator.comparing(Order::getExecutionDate));
                break;
            case "amount":
                sortedOrders.sort(Comparator.comparing(Order::getAmount));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by: " + sortBy);
        }

        return sortedOrders;
    }

    @Override
    public BigDecimal findEarnedFundsOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = orderRepository.findOrderByDateRange(startDate, endDate);

        if (sortedOrders.isEmpty()) return BigDecimal.ZERO;

        BigDecimal totalAmount = new BigDecimal("0");
        for (Order order : sortedOrders) {
            totalAmount = totalAmount.add(order.getAmount());
        }
        return totalAmount;
    }

    @Override
    public BigDecimal findNumberCompletedOrdersOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = orderRepository.findOrderByDateRange(startDate, endDate);
        if (sortedOrders.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(sortedOrders.size());
    }
}
