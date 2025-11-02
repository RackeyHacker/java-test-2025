package controller;

import entity.Book;
import enums.BookStatus;
import model.BookStore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class BookController {

    private BookStore store;
    private final Scanner scanner = new Scanner(System.in);

    public BookController(BookStore store) {
        this.store = store;
    }

    public BookStore getStore() {
        return store;
    }

    public void setStore(BookStore store) {
        this.store = store;
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
        Book book = new Book(title, author, BookStatus.AVAILABLE, price, date, description);
        store.addBookToWarehouse(book);
    }

    public void writeOffBookFromWarehouse() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();
        Book book = store.findBookByTitle(title);
        if (book == null) {
            System.out.println("Book not found: " + title);
            return;
        }
        store.writeOffBookFromWarehouse(book);
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
        store.changeBookStatus(title, newStatus);
    }

    public void showBooksSortedBy() {
        System.out.print("Enter the sorting parameter (title, publication Date, price, status): ");
        String sortingParameter = scanner.nextLine().trim();;
        store.showBooksSortedBy(sortingParameter);
    }

    public void showBookDescription() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();
        store.showBookDescription(title);
    }

    public void showRequestedBooksSortedBy() {
        System.out.print("Enter the sorting parameter (count, title): ");
        String sortingParameter = scanner.nextLine();
        store.showRequestedBooksSortedBy(sortingParameter);
    }

    public void showOldBooks() {
        System.out.print("Enter the sorting parameter (receiptDate, price): ");
        String sortingParameter = scanner.nextLine().trim();;
        store.showOldBooks(sortingParameter);
    }
}
