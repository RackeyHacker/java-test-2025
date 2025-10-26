package bookstore;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        BookStore store = new BookStore();

        Calendar cal = Calendar.getInstance();
        cal.set(2018, Calendar.MAY, 12);
        Book b1 = new Book("Clean Code", BookStatus.AVAILABLE, 45.0, cal.getTime(), "Best practices for writing clean code");
        cal.set(2020, Calendar.AUGUST, 21);
        Book b2 = new Book("Effective Java", BookStatus.UNAVAILABLE, 55.0, cal.getTime(), "Comprehensive guide to Java programming");
        cal.set(2015, Calendar.DECEMBER, 10);
        Book b3 = new Book("Design Patterns", BookStatus.AVAILABLE, 60.0, cal.getTime(), "Classic software design solutions");

        store.addBookToWarehouse(b1);
        store.addBookToWarehouse(b2);
        store.addBookToWarehouse(b3);

        Customer c1 = new Customer("Alice", "alice@gmail.com");
        Customer c2 = new Customer("Bob", "bob@gmail.com");

        store.addBookToCart(b1);
        store.addBookToCart(b3);
        store.makeOrder(store.getCart(), c1);

        store.addBookToCart(b2);
        store.makeOrder(store.getCart(), c2);

        store.changeBookStatus("Effective Java", BookStatus.AVAILABLE);
        store.changeOrderStatus(1, OrderStatus.COMPLETED);
        store.cancelOrder(2);

        System.out.println("\n=== Список книг по названию ===");
        store.showBooksSortedBy("title");

        System.out.println("\n=== Список книг по цене ===");
        store.showBooksSortedBy("price");

        System.out.println("\n=== Список заказов по статусу ===");
        store.showOrdersSortedBy("status");

        System.out.println("\n=== Детали заказа №1 ===");
        store.showOrderDetails(1);

        System.out.println("\n=== Описание книги ===");
        store.showBookDescription("Design Patterns");

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        Calendar end = Calendar.getInstance();

        System.out.println("\n=== Выполненные заказы за период ===");
        store.showOrderForDateSortedBy(start.getTime(), end.getTime(), "amount");

        store.findEarnedFundsOverPeriodTime(start.getTime(), end.getTime());
        store.findNumberCompletedOrdersOverPeriodTime(start.getTime(), end.getTime());

        System.out.println("\n=== Запросы на книги ===");
        store.showRequestedBooksSortedBy("title");

        System.out.println("\n=== Старые книги ===");
        store.showOldBooks("receiptDate");
    }
}
