package com.repository;

import com.entity.Book;
import com.enums.BookStatus;

import java.util.List;

public interface BookRepository {

    List<Book> findAll();

    void addBook(Book book);

    void removeBook(String title);

    Book findBookByTitle(String title);

    Book findBookById(int id);

    void changeBookStatus(String title, BookStatus status);

    void replaceAllBooks(List<Book> books);

    void exportBooksCSV();

    void importBooksCSV();
}
