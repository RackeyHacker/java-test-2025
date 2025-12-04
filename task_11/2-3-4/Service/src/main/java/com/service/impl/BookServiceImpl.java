package com.service.impl;

import com.annotations.Autowired;
import com.annotations.Service;
import com.entity.Book;
import com.entity.Order;
import com.enums.BookStatus;
import com.repository.BookRepository;
import com.service.config.BookConfig;
import com.service.BookService;
import com.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BookConfig config;

    public BookServiceImpl() {
    }

    @Override
    public void addBook(String title, String author, BigDecimal price, LocalDate publicationDate, String description) {
        Book book = new Book(title, author, BookStatus.AVAILABLE, price, publicationDate, description);
        bookRepository.addBook(book);
    }

    @Override
    public void writeOffBook(String title) {
        Book searchBook = bookRepository.findBookByTitle(title);
        if (searchBook == null) throw new IllegalArgumentException("Book not found: " + title);
        bookRepository.changeBookStatus(title, BookStatus.UNAVAILABLE);
    }


    @Override
    public List<Order> changeStatus(String title, BookStatus newStatus) {
        Book searchBook = bookRepository.findBookByTitle(title);
        if (searchBook == null) throw new IllegalArgumentException("Book not found: " + title);
        bookRepository.changeBookStatus(title, newStatus);
        List<Order> readyOrders = new ArrayList<>();

        if (newStatus == BookStatus.AVAILABLE && config.isEnableCompleteRequests()) {
            readyOrders = orderService.processBookRequests(searchBook);
        }

        return readyOrders;
    }

    @Override
    public List<Book> showBooksSortedBy(String sortBy) {
        List<Book> books = bookRepository.findAll();

        switch (sortBy) {
            case "title":
                books.sort(Comparator.comparing(Book::getTitle));
                break;
            case "publicationDate":
                books.sort(Comparator.comparing(Book::getPublicationDate));
                break;
            case "price":
                books.sort(Comparator.comparing(Book::getPrice));
                break;
            case "status":
                books.sort(Comparator.comparing(Book::getStatus));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by: " + sortBy);
        }
        return books;
    }

    @Override
    public List<Book> showRequestedBooksSortedBy(String sortBy) {
        List<Book> requestedBooks = bookRepository.findAll().stream()
                .filter(book -> book.getRequestCount() > 0)
                .toList();

        switch (sortBy) {
            case "count":
                requestedBooks.sort(Comparator.comparingInt(Book::getRequestCount));
                break;
            case "title":
                requestedBooks.sort(Comparator.comparing(Book::getTitle));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by: " + sortBy);
        }

        return requestedBooks;
    }

    @Override
    public List<Book> showOldBooks(String sortBy) {

        LocalDate staleDate = LocalDate.now().minusMonths(config.getStaleBookMonths());
        List<Book> oldBooks = bookRepository.findAll().stream()
                .filter(book -> book.getPublicationDate().isBefore(staleDate))
                .toList();

        switch (sortBy) {
            case "receiptDate":
                oldBooks.sort(Comparator.comparing(Book::getReceiptDate));
                break;
            case "price":
                oldBooks.sort(Comparator.comparing(Book::getPrice));
                break;
            default:
                throw new IllegalArgumentException("Invalid sort by: " + sortBy);
        }

        return oldBooks;
    }

    @Override
    public Book showBookDescription(String title) {
        return bookRepository.findBookByTitle(title);
    }
}
