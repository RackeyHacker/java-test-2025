package controller;

import model.BookStore;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Scanner;

public class ReportController {

    private final BookStore store;
    private final Scanner scanner = new Scanner(System.in);

    public ReportController(BookStore store) {
        this.store = store;
    }

    public void showOrderForDateSortedBy() {
        LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

        System.out.print("Enter the sorting parameter (date, amount): ");
        String sortingParameter = scanner.nextLine();

        store.showOrderForDateSortedBy(startDate, endDate, sortingParameter);
    }

    public void findEarnedFundsOverPeriodTime() {
        LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

        store.findEarnedFundsOverPeriodTime(startDate, endDate);
    }

    public void findNumberCompletedOrdersOverPeriodTime() {
        LocalDate startDate = readDate("Enter start date (yyyy-MM-dd): ");
        LocalDate endDate = readDate("Enter end date (yyyy-MM-dd): ");

        store.findNumberCompletedOrdersOverPeriodTime(startDate, endDate);
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
}
