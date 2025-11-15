package controller;

import entity.Order;
import service.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    public List<Order> showOrderForDateSortedBy(LocalDate startDate, LocalDate endDate, String sortingParameter) {
        return reportService.showOrderForDateSortedBy(startDate, endDate, sortingParameter);
    }

    public BigDecimal findEarnedFundsOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        return reportService.findEarnedFundsOverPeriodTime(startDate, endDate);
    }

    public BigDecimal findNumberCompletedOrdersOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        return reportService.findNumberCompletedOrdersOverPeriodTime(startDate, endDate);
    }


}
