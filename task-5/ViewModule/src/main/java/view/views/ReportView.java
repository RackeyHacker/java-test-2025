package view.views;

import entity.Order;
import controller.ReportController;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ReportView {

    private ReportController controller;
    private Scanner scanner = new Scanner(System.in);

    public ReportView(ReportController controller) {
        this.controller = controller;
    }

    public void showOrderForDateSortedBy() {
        LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

        System.out.print("Enter the sorting parameter (date, amount): ");
        String sortingParameter = scanner.nextLine();

        List<Order> orders = controller.showOrderForDateSortedBy(startDate, endDate, sortingParameter);
        if (orders == null) {
            System.err.println("Incorrect SORTING PARAMETER");
        } else if (orders.isEmpty()) {
            System.out.println("No order for this date");
        } else {
            printOrders(orders);
        }
    }

    public void findEarnedFundsOverPeriodTime() {
        LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

        controller.findEarnedFundsOverPeriodTime(startDate, endDate);
    }

    public void findNumberCompletedOrdersOverPeriodTime() {
        LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

        String result = controller.findNumberCompletedOrdersOverPeriodTime(startDate, endDate);
        System.out.println(result);
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
