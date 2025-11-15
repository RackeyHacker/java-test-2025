package controller;

import entity.Book;
import entity.Order;
import enums.BookStatus;
import service.BookService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public void addBookToWarehouse(String title,
                                   String author,
                                   BigDecimal price,
                                   LocalDate publicationDate,
                                   String description) {
        bookService.addBook(title, author, price, publicationDate, description);
    }

    public void writeOffBookFromWarehouse(String title) {
        bookService.writeOffBook(title);
    }

    public List<Order> changeBookStatus(String title, BookStatus newStatus) {
        return bookService.changeStatus(title, newStatus);
    }

    public List<Book> showBooksSortedBy(String sortingParameter) {
        return bookService.showBooksSortedBy(sortingParameter);
    }

    public List<Book> showRequestedBooksSortedBy(String sortingParameter) {
        return bookService.showRequestedBooksSortedBy(sortingParameter);
    }

    public List<Book> showOldBooks(String sortingParameter) {
        return bookService.showOldBooks(sortingParameter);
    }

    public Book showBookDescription(String title) {
        return bookService.showBookDescription(title);
    }
}


