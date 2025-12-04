package com.repository.impl;

import com.annotations.Repository;
import com.config.dbconfig.DBConnectionManager;
import com.entity.Book;
import com.enums.BookStatus;
import com.repository.BookRepository;
import com.repository.exceptions.DataProcessingException;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.repository.util.CsvUtils;

import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final String bookDataPath = "data/csv/books.csv";

    private final DBConnectionManager connectionManager = new DBConnectionManager();

    public BookRepositoryImpl() {
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String query = "select * from books";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                books.add(setBookParameters(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book setBookParameters(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt("book_id"));
        book.setTitle(resultSet.getString("title"));
        book.setAuthor(resultSet.getString("author"));
        book.setPrice(resultSet.getBigDecimal("price"));

        Date pubDate = resultSet.getDate("publication_date");
        book.setPublicationDate(pubDate != null ? pubDate.toLocalDate() : null);

        book.setRequestCount(resultSet.getInt("request_count"));

        Date receiptDate = resultSet.getDate("receipt_date");
        book.setReceiptDate(receiptDate != null ? receiptDate.toLocalDate() : null);

        book.setStatus(BookStatus.valueOf(resultSet.getString("status")));
        book.setDescription(resultSet.getString("description"));
        return book;
    }

    @Override
    public void addBook(Book book) {
        String queryInsert = "insert into books (title, author, status, price, publication_date, description, " +
                "request_count, receipt_date) values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryInsert);
        ) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getStatus().name());
            preparedStatement.setBigDecimal(4, book.getPrice());
            preparedStatement.setDate(5, Date.valueOf(book.getPublicationDate()));
            preparedStatement.setString(6, book.getDescription());
            preparedStatement.setInt(7, book.getRequestCount());
            preparedStatement.setDate(8, Date.valueOf(book.getReceiptDate()));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Book findBookByTitle(String title) {
        String query = "select * from books where title = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, title);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return setBookParameters(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book findBookById(int id) {
        String query = "select * from books where book_id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return setBookParameters(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void removeBook(String title) {
        String query = "delete from books where title = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, title);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeBookStatus(String title, BookStatus status) {
        String query = "update books set status = ? where title = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setString(2, title);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void replaceAllBooks(List<Book> books) {
        String truncateQuery = "truncate table books restart identity cascade";
        String insertQuery = "insert into books (title, author, status, price, publication_date, description, " +
                "request_count, receipt_date) values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement truncateStatement = connection.prepareStatement(truncateQuery)) {
                truncateStatement.executeUpdate();
            }
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                for (Book book : books) {
                    insertStatement.setString(1, book.getTitle());
                    insertStatement.setString(2, book.getAuthor());
                    insertStatement.setString(3, book.getStatus().name());
                    insertStatement.setBigDecimal(4, book.getPrice());
                    insertStatement.setDate(5, book.getPublicationDate() != null ? Date.valueOf(book.getPublicationDate()) : null);
                    insertStatement.setString(6, book.getDescription());
                    insertStatement.setInt(7, book.getRequestCount());
                    insertStatement.setDate(8, book.getReceiptDate() != null ? Date.valueOf(book.getReceiptDate()) : null);
                    insertStatement.addBatch();
                }
                insertStatement.executeBatch();
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportBooksCSV() {
        String query = "select book_id, title, author, description, price, publication_date, status from books";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery();
             CSVWriter writer = new CSVWriter(new FileWriter(bookDataPath))) {

            writer.writeNext(new String[]{"book_id", "title", "author", "description", "price", "publicationDate", "status"});

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("book_id"));
                String title = rs.getString("title");
                String author = rs.getString("author");
                String description = rs.getString("description");
                String price = rs.getBigDecimal("price").toString();
                String publicationDate = rs.getDate("publication_date") != null
                        ? rs.getDate("publication_date").toLocalDate().toString()
                        : "";
                String status = rs.getString("status");

                writer.writeNext(new String[]{id, title, author, description, price, publicationDate, status});
            }

        } catch (Exception e) {
            throw new DataProcessingException("Error exporting books to CSV", e);
        }
    }

    @Override
    public void importBooksCSV() {
        String queryInsert = "insert into books(title, author, status, price, publication_date, description) " +
                "values (?, ?, ?, ?, ?, ?) " +
                "on conflict (book_id) do update set title = EXCLUDED.title, author = EXCLUDED.author, " +
                "status = EXCLUDED.status, price = EXCLUDED.price, publication_date = EXCLUDED.publication_date, " +
                "description = EXCLUDED.description";

        try (Connection connection = connectionManager.getConnection();
             CSVReader reader = new CSVReader(new FileReader(bookDataPath))) {

            CsvUtils.skipHeader(reader);
            connection.setAutoCommit(false);

            try (PreparedStatement ps = connection.prepareStatement(queryInsert)) {
                String[] row;
                while ((row = reader.readNext()) != null) {
                    ps.setString(1, row[1]);
                    ps.setString(2, row[2]);
                    ps.setString(3, row[6]);
                    ps.setBigDecimal(4, new BigDecimal(row[4]));
                    ps.setDate(5, Date.valueOf(row[5]));
                    ps.setString(6, row[3]);
                    ps.addBatch();
                }

                ps.executeBatch();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw e;
            }

        } catch (Exception e) {
            throw new DataProcessingException("Error importing books from CSV", e);
        }
    }
}
