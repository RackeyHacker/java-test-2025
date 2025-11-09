package controller;

import entity.Order;
import model.BookStore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReportController {

    private final BookStore store;

    public ReportController(BookStore store) {
        this.store = store;
    }

    public List<Order> showOrderForDateSortedBy(LocalDate startDate, LocalDate endDate, String sortingParameter) {
        return store.showOrderForDateSortedBy(startDate, endDate, sortingParameter);
    }

    public BigDecimal findEarnedFundsOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        return store.findEarnedFundsOverPeriodTime(startDate, endDate);
    }

    public BigDecimal findNumberCompletedOrdersOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        return store.findNumberCompletedOrdersOverPeriodTime(startDate, endDate);
    }


}
