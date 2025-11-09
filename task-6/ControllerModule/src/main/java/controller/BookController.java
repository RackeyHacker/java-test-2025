package controller;

import entity.Book;
import entity.Order;
import enums.BookStatus;
import model.BookStore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookController {

    private final BookStore store;

    public BookController(BookStore store) {
        this.store = store;
    }

    public void addBookToWarehouse(String title,
                                   String author,
                                   BigDecimal price,
                                   LocalDate publicationDate,
                                   String description) {
        Book book = new Book(title, author, BookStatus.AVAILABLE, price, publicationDate, description);
        store.addBookToWarehouse(book);
    }

    public void writeOffBookFromWarehouse(String title) {
        store.writeOffBookFromWarehouse(title);
    }

    public List<Order> changeBookStatus(String title, BookStatus newStatus) {
        return store.changeBookStatus(title, newStatus);
    }

    public List<Book> showBooksSortedBy(String sortingParameter) {
        return store.showBooksSortedBy(sortingParameter);
    }

    public Book showBookDescription(String title) {
        return store.showBookDescription(title);
    }

    public List<Book> showRequestedBooksSortedBy(String sortingParameter) {
        return store.showRequestedBooksSortedBy(sortingParameter);
    }

    public List<Book> showOldBooks(String sortingParameter) {
        return store.showOldBooks(sortingParameter);
    }
}


