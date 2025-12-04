package com.entity;

import com.enums.BookStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;


public class Book implements Serializable {

    private int id;
    private String title;
    private String author;
    private BookStatus status;
    private BigDecimal price;
    private LocalDate publicationDate;
    private String description;
    private int requestCount;
    private LocalDate receiptDate;

    public Book(String title, String author, BookStatus status, BigDecimal price,
                LocalDate publicationDate, String description) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.publicationDate = publicationDate;
        this.description = description;
        this.receiptDate = LocalDate.now();
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public void incrementRequestCount() {
        requestCount++;
    }

    public LocalDate getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(LocalDate receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", status=" + status +
                ", price=" + price +
                ", publicationDate=" + publicationDate +
                ", description='" + description + '\'' +
                ", requestCount=" + requestCount +
                ", receiptDate=" + receiptDate +
                '}';
    }
}
