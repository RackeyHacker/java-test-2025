package bookstore;

public class Book {

    private String title;
    private BookStatus status;
    private double price;

    public Book(String title, BookStatus status, double price) {
        this.title = title;
        this.status = status;
        this.price = price;
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

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                '}';
    }
}
