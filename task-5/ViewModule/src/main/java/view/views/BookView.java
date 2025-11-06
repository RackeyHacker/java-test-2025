package view.views;

import entity.Book;
import enums.BookStatus;
import controller.BookController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class BookView {

    private BookController controller;
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
        boolean success = controller.addBookToWarehouse(title, author, price, date, description);
        if (success) {
            System.out.println("The book " + title + " was added to warehouse");
        } else {
            System.out.println("Failed to add book " + title);
        }
    }

    public void writeOffBookFromWarehouse() {
        System.out.print("Enter the title: ");
        String title = scanner.nextLine();
        boolean success = controller.writeOffBookFromWarehouse(title);
        if (success) {
            System.out.println("The book was written successful");
        } else {
            System.out.println("The book not found: " + title);
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
        boolean success = controller.changeBookStatus(title, newStatus);
        if (success) {
            System.out.println("The status of the book was changed success");
        } else {
            System.err.println("Check TITLE or STATUS of the book");
        }
    }

    public void showBooksSortedBy() {
        System.out.print("Enter the sorting parameter (title, publication Date, price, status): ");
        String sortingParameter = scanner.nextLine().trim();
        List<Book> books = controller.showBooksSortedBy(sortingParameter);
        if (books == null) {
            System.err.println("Incorrect SORTING PARAMETER");
        } else if (books.isEmpty()) {
            System.out.println("No books in warehouse");
        } else {
            printBooks(books);
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
        String description = controller.showBookDescription(title);
        System.out.println(description);
    }

    public void showRequestedBooksSortedBy() {
        System.out.print("Enter the sorting parameter (count, title): ");
        String sortingParameter = scanner.nextLine();
        List<Book> requestedBooks = controller.showRequestedBooksSortedBy(sortingParameter);
        if (requestedBooks == null) {
            System.err.println("Incorrect SORTING PARAMETER");
        } else if (requestedBooks.isEmpty()) {
            System.out.println("No requested books");
        } else {
            printRequestedBooks(requestedBooks);
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
        List<Book> oldBooks = controller.showOldBooks(sortingParameter);
        if (oldBooks == null) {
            System.err.println("Incorrect SORTING PARAMETER");
        } else if (oldBooks.isEmpty()) {
            System.out.println("No old books");
        } else {
            printBooks(oldBooks);
        }
    }
}
