package bookstore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Objects;


public class Book {

    private String title;
    private BookStatus status;
    private BigDecimal price;
    private Date publicationDate;
    private String description;
    private int requestCount;
    private Date receiptDate;

    public Book(String title, BookStatus status, BigDecimal price, Date publicationDate, String description) {
        this.title = title;
        this.status = status;
        this.price = price.setScale(2, RoundingMode.HALF_UP);
        this.publicationDate = publicationDate;
        this.description = description;
        this.receiptDate = new Date();
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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
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

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return requestCount == book.requestCount &&
                Objects.equals(title, book.title) &&
                status == book.status &&
                Objects.equals(price, book.price) &&
                Objects.equals(publicationDate, book.publicationDate) &&
                Objects.equals(description, book.description) &&
                Objects.equals(receiptDate, book.receiptDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, status, price, publicationDate, description, requestCount, receiptDate);
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", status=" + status +
                ", price=" + price +
                ", publicationDate=" + publicationDate +
                ", description='" + description + '\'' +
                ", requestCount=" + requestCount +
                ", receiptDate=" + receiptDate +
                '}';
    }
}
