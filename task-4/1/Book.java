package bookstore;

import java.util.Date;

public class Book {

    private String title;
    private BookStatus status;
    private double price;
    private Date publicationDate;
    private String description;
    private int requestCount;
    private Date receiptDate;

    public Book(String title, BookStatus status, double price, Date publicationDate, String description) {
        this.title = title;
        this.status = status;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
