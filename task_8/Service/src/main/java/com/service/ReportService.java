package com.service;

import com.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    List<Order> showOrderForDateSortedBy(LocalDate startDate, LocalDate endDate, String sortingParameter);

    BigDecimal findEarnedFundsOverPeriodTime(LocalDate startDate, LocalDate endDate);

    BigDecimal findNumberCompletedOrdersOverPeriodTime(LocalDate startDate, LocalDate endDate);
}
