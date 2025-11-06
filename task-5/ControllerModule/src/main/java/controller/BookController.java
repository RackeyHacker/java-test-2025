package controller;

import entity.Book;
import enums.BookStatus;
import model.BookStore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookController {

    private BookStore store;

    public BookController(BookStore store) {
        this.store = store;
    }

    public boolean addBookToWarehouse(String title, String author, BigDecimal price, LocalDate publicationDate, String description) {
        Book book = new Book(title, author, BookStatus.AVAILABLE, price, publicationDate, description);
        return store.addBookToWarehouse(book);
    }

    public boolean writeOffBookFromWarehouse(String title) {
        Book book = store.findBookByTitle(title);
        if (book == null) return false;
        return store.writeOffBookFromWarehouse(book);
    }

    public boolean changeBookStatus(String title, BookStatus newStatus) {
        if (title == null || newStatus == null) return false;
        return store.changeBookStatus(title, newStatus);
    }

    public List<Book> showBooksSortedBy(String sortingParameter) {
        if (sortingParameter.isEmpty()) return null;
        return store.showBooksSortedBy(sortingParameter);
    }

    public String showBookDescription(String title) {
        if (title.isEmpty()) return "The title is EMPTY or INCORRECT";
        return store.showBookDescription(title);
    }

    public List<Book> showRequestedBooksSortedBy(String sortingParameter) {
        if (sortingParameter.isEmpty()) return null;
        return store.showRequestedBooksSortedBy(sortingParameter);
    }

    public List<Book> showOldBooks(String sortingParameter) {
        if (sortingParameter.isEmpty()) return null;
        return store.showOldBooks(sortingParameter);
    }
}
