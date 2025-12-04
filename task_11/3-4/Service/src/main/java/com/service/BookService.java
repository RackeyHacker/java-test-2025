package com.service;

import com.entity.Book;
import com.entity.Order;
import com.enums.BookStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookService {

    void addBook(String title, String author, BigDecimal price, LocalDate publicationDate, String description);

    void writeOffBook(String title);

    List<Order> changeStatus(String title, BookStatus newStatus);

    List<Book> showBooksSortedBy(String parameter);

    List<Book> showRequestedBooksSortedBy(String parameter);

    List<Book> showOldBooks(String sortBy);

    Book showBookDescription(String title);
}