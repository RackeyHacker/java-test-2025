package view.views;

import controller.ReportController;
import entity.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReportView {

    private final ReportController controller;
    private final Scanner scanner = new Scanner(System.in);

    public ReportView(ReportController controller) {
        this.controller = controller;
    }

    public void showOrderForDateSortedBy() {
        LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

        System.out.print("Enter the sorting parameter (date, amount): ");
        String sortingParameter = scanner.nextLine();

        try {
            List<Order> orders = controller.showOrderForDateSortedBy(startDate, endDate, sortingParameter);
            if (orders.isEmpty()) {
                System.out.println("No order for this date");
            } else {
                printOrders(orders);
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void findEarnedFundsOverPeriodTime() {
        LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

        try {
            BigDecimal result = controller.findEarnedFundsOverPeriodTime(startDate, endDate);
            System.out.println("Earned funds over period time: " + result);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void findNumberCompletedOrdersOverPeriodTime() {
        LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

        try {
            BigDecimal result = controller.findNumberCompletedOrdersOverPeriodTime(startDate, endDate);
            System.out.println("Total completed orders: " + result);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private LocalDate readDate(String currentDate) {
        LocalDate date = null;
        while (date == null) {
            System.out.print(currentDate);
            try {
                date = LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid date format. Use format 'yyyy-MM-dd'");
            }
        }
        return date;
    }

    private void printOrders(List<Order> orders) {
        for (int i = 0; i < orders.size(); i++) {
            System.out.println((i + 1) + ". " + orders.get(i));
        }
    }
}
