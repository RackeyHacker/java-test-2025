package com.view.views;

import com.controller.BookController;
import com.entity.Book;
import com.entity.Order;
import com.enums.BookStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class BookView {

    private final BookController controller;
    private final Scanner scanner = new Scanner(System.in);

    public BookView(BookController controller) {
        this.controller = controller;
    }

    public void addBookToWarehouse() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();

        System.out.print("Enter the author: ");
        String author = scanner.nextLine();

        BigDecimal price = null;
        while (price == null) {
            System.out.print("Enter the price: ");
            try {
                price = new BigDecimal(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid number format, try again.");
            }
        }

        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter the publication date (yyyy-MM-dd): ");
            try {
                date = LocalDate.parse(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid date format, try again");
            }
        }

        System.out.print("Enter the description: ");
        String description = scanner.nextLine();

        try {
            controller.addBookToWarehouse(title, author, price, date, description);
            System.out.println("The book " + title + " was added to warehouse");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void writeOffBookFromWarehouse() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();

        try {
            controller.writeOffBookFromWarehouse(title);
            System.out.println("The book was written successful");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void changeBookStatus() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();

        BookStatus newStatus = null;
        while (newStatus == null) {
            System.out.print("Enter new status (AVAILABLE, UNAVAILABLE): ");
            String input = scanner.nextLine().toUpperCase();
            try {
                newStatus = BookStatus.valueOf(input);
            } catch (Exception e) {
                System.out.println("Invalid status, try again");
            }
        }

        try {
            List<Order> readyOrders = controller.changeBookStatus(title, newStatus);
            System.out.println("The status of the book was changed success");

            if (!readyOrders.isEmpty()) {
                System.out.println("Following orders are now ready:");
                for (Order order : readyOrders) {
                    System.out.println(order);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void showBooksSortedBy() {
        System.out.print("Enter the sorting parameter (title, publication Date, price, status): ");
        String sortingParameter = scanner.nextLine().trim();

        try {
            List<Book> books = controller.showBooksSortedBy(sortingParameter);
            if (books.isEmpty()) {
                System.out.println("No books in warehouse");
            } else {
                printBooks(books);
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private void printBooks(List<Book> bookList) {
        for (int i = 0; i < bookList.size(); i++) {
            System.out.println((i + 1) + ". " + bookList.get(i));
        }
    }

    public void showBookDescription() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();

        try {
            Book book = controller.showBookDescription(title);
            System.out.println("Title: " + book.getTitle() + " (" +
                    book.getPublicationDate() + ")" + "\nDescription: " +
                    book.getDescription());
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public void showRequestedBooksSortedBy() {
        System.out.print("Enter the sorting parameter (count, title): ");
        String sortingParameter = scanner.nextLine();

        try {
            List<Book> requestedBooks = controller.showRequestedBooksSortedBy(sortingParameter);
            if (requestedBooks.isEmpty()) {
                System.out.println("No requested books");
            } else {
                printRequestedBooks(requestedBooks);
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    private void printRequestedBooks(List<Book> requestedBooks) {
        for (Book book : requestedBooks) {
            System.out.println(book.getTitle() + " — Requests: " + book.getRequestCount());
        }
    }

    public void showOldBooks() {
        System.out.print("Enter the sorting parameter (receiptDate, price): ");
        String sortingParameter = scanner.nextLine().trim();

        try {
            List<Book> oldBooks = controller.showOldBooks(sortingParameter);
            if (oldBooks.isEmpty()) {
                System.out.println("No old books");
            } else {
                printBooks(oldBooks);
            }
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
