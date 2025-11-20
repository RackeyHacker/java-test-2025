package com.repository.impl;

import com.annotations.Repository;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.entity.Book;
import com.enums.BookStatus;
import com.repository.BookRepository;
import com.repository.exceptions.DataProcessingException;
import com.repository.util.CsvUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final String bookDataPath = "data/csv/books.csv";
    private final List<Book> books = new ArrayList<>();

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(books);
    }

    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public Book findBookByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Book findBookById(int id) {
        return books.stream()
                .filter(book -> book.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeBook(String title) {
        Book book = findBookByTitle(title);
        if (book == null) throw new IllegalArgumentException("Book not found: " + title);
        books.remove(book);
    }

    @Override
    public void changeBookStatus(String title, BookStatus status) {
        Book book = findBookByTitle(title);
        if (book == null) throw new IllegalArgumentException("Book not found: " + title);
        book.setStatus(status);
    }

    @Override
    public void replaceAllBooks(List<Book> books) {
        this.books.clear();
        this.books.addAll(books);
    }

    @Override
    public void exportBooksCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(bookDataPath))) {
            writer.writeNext(new String[]{"id", "title", "author", "description", "price", "publicationDate", "status"});

            for (Book book : books) {
                writer.writeNext(new String[]{
                        String.valueOf(book.getId()),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getDescription(),
                        String.valueOf(book.getPrice()),
                        book.getPublicationDate().toString(),
                        book.getStatus().toString()
                });
            }

        } catch (Exception e) {
            throw new DataProcessingException("Error exporting books", e);
        }
    }

    @Override
    public void importBooksCSV() {
        try (CSVReader reader = new CSVReader(new FileReader(bookDataPath))) {
            CsvUtils.skipHeader(reader);

            String[] row;
            while ((row = reader.readNext()) != null) {
                int id = Integer.parseInt(row[0]);
                String title = row[1];
                String author = row[2];
                String description = row[3];
                BigDecimal price = new BigDecimal(row[4]);
                LocalDate publicationDate = LocalDate.parse(row[5]);
                BookStatus status = BookStatus.valueOf(row[6]);

                Book existing = findBookById(id);

                if (existing == null) {
                    Book newBook = new Book(title, author, status, price, publicationDate, description);
                    newBook.setId(id);
                    books.add(newBook);
                } else {
                    existing.setTitle(title);
                    existing.setAuthor(author);
                    existing.setPrice(price);
                    existing.setPublicationDate(publicationDate);
                    existing.setDescription(description);
                    existing.setStatus(status);
                }
            }

        } catch (Exception e) {
            throw new DataProcessingException("Error importing books", e);
        }
    }
}
