package com.controller;

import com.annotations.Autowired;
import com.annotations.Controller;
import com.entity.Order;
import com.service.ReportService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    public ReportController() {
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
