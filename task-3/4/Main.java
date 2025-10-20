package bookstore;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        BookStore store = new BookStore();

        Book book1 = new Book("Clean Code", BookStatus.AVAILABLE, 45.0);
        Book book2 = new Book("Effective Java", BookStatus.UNAVAILABLE, 55.0);
        Book book3 = new Book("Design Patterns", BookStatus.AVAILABLE, 60.0);

        store.addBookToWarehouse(book1);
        store.addBookToWarehouse(book2);
        store.addBookToWarehouse(book3);

        store.changeBookStatus("Effective Java", BookStatus.UNAVAILABLE);

        List<Book> cart1 = new ArrayList<>(Arrays.asList(book1, book3));
        store.makeOrder(cart1);

        List<Book> cart2 = new ArrayList<>(Collections.singletonList(book2));
        store.makeOrder(cart2);

        store.writeOffBookFromWarehouse(book3);

        store.changeOrderStatus(1, OrderStatus.COMPLETED);

        store.changeBookStatus("Effective Java", BookStatus.AVAILABLE);

        store.cancelOrder(2);
    }
}
