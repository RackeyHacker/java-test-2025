package com.controller;

import com.annotations.Autowired;
import com.annotations.Controller;
import com.entity.Book;
import com.entity.Order;
import com.enums.BookStatus;
import com.service.BookService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    public BookController() {
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


