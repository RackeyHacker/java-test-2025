package bookstore;

import java.util.*;

public class BookStore {

    private final List<Book> warehouse = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final List<Book> cart = new ArrayList<>();
    private final List<Order> requests = new ArrayList<>();

    public void makeOrder(List<Book> cart) {
        if (cart == null) {
            System.out.println("Error: cart is null!");
            return;
        }

        if (cart.isEmpty()) {
            System.out.println("The cart is empty!");
            return;
        }

        Order order = new Order(new ArrayList<>(cart));

        if (containsUnavailableBooks(cart, order)) {
            handleOrderWithUnavailableBooks(order);
        } else {
            handleSuccessfulOrder(order);
        }
    }

    private boolean containsUnavailableBooks(List<Book> cart, Order order) {
        boolean hasUnavailable = false;
        for (Book book : cart) {
            if (book == null) continue;
            if (!checkStatus(book)) {
                hasUnavailable = true;
                makeRequestForBook(book);
            }
        }
        return hasUnavailable;
    }

    private void handleOrderWithUnavailableBooks(Order order) {
        order.setHasPendingRequests(true);
        requests.add(order);
        System.out.println("Order #" + order.getId() + " was sent for revision");
        clearCart();
    }

    private void handleSuccessfulOrder(Order order) {
        orders.add(order);
        System.out.println("Order #" + order.getId() + " was made successfully");
        clearCart();
    }

    public void cancelOrder(int id) {
        if (id <= 0) {
            System.out.println("Invalid order ID");
            return;
        }

        Order order = findOrderById(id);
        if (order == null) {
            System.out.println("Order #" + id + " not found");
            return;
        }

        order.setStatus(OrderStatus.CANCELED);
        System.out.println("Order #" + id + " was cancelled");
    }

    public void changeOrderStatus(int id, OrderStatus newStatus) {
        if (newStatus == null) {
            System.out.println("Error: newStatus is null!");
            return;
        }

        Order order = findOrderById(id);
        if (order != null) {
            order.setStatus(newStatus);
            System.out.println("Order #" + id + " status changed to " + newStatus);
        } else {
            System.out.println("Order #" + id + " not found");
        }
    }

    public void changeBookStatus(String title, BookStatus newStatus) {
        if (title == null || newStatus == null) {
            System.out.println("Error: title or status is null!");
            return;
        }

        Book searchingBook = findBookByTitle(title);
        if (searchingBook == null) {
            System.out.println("Book not found: " + title);
            return;
        }

        searchingBook.setStatus(newStatus);
        System.out.println("Status of the book " + title + " was changed to " + newStatus);

        if (newStatus == BookStatus.AVAILABLE) {
            processBookRequests(searchingBook);
        }
    }

    public void addBookToWarehouse(Book book) {
        if (book == null) {
            System.out.println("Error: book is null!");
            return;
        }
        warehouse.add(book);
        book.setStatus(BookStatus.AVAILABLE);
        System.out.println("The book " + book.getTitle() + " was added to warehouse");
        processBookRequests(book);
    }

    public void writeOffBookFromWarehouse(Book book) {
        if (book == null) {
            System.out.println("Error: book is null!");
            return;
        }
        book.setStatus(BookStatus.UNAVAILABLE);
        System.out.println("The book " + book.getTitle() + " was written off from the warehouse");
    }

    public void makeRequestForBook(Book book) {
        if (book == null) {
            System.out.println("Error: book is null!");
            return;
        }
        if (book.getStatus() == BookStatus.UNAVAILABLE) {
            System.out.println("Request created for unavailable book: " + book.getTitle());
        }
    }

    public void addBookToCart(Book book) {
        if (book == null) {
            System.out.println("Error: book is null!");
            return;
        }
        cart.add(book);
    }

    public void removeBookFromCart(Book book) {
        if (book == null) {
            System.out.println("Error: book is null!");
            return;
        }
        cart.remove(book);
    }

    public void clearCart() {
        cart.clear();
    }

    public void showCart() {
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty.\n");
            return;
        }

        System.out.println("\nYour cart:");
        for (Book book : cart) {
            System.out.println(book);
        }
        System.out.println();
    }

    private Order findOrderById(int id) {
        for (Order order : orders) {
            if (order.getId() == id) return order;
        }
        for (Order order : requests) {
            if (order.getId() == id) return order;
        }
        return null;
    }

    private Book findBookByTitle(String title) {
        for (Book book : warehouse) {
            if (book.getTitle().equals(title)) return book;
        }
        return null;
    }

    private boolean checkStatus(Book book) {
        return book.getStatus() != BookStatus.UNAVAILABLE;
    }

    private void processBookRequests(Book availableBook) {
        List<Order> readyOrders = new ArrayList<>();
        for (Order order : requests) {
            boolean containsThisBook = order.getBooks().stream()
                    .anyMatch(b -> b.getTitle().equals(availableBook.getTitle()));
            if (!containsThisBook) continue;

            boolean allAvailable = order.getBooks().stream()
                    .noneMatch(b -> b.getStatus() == BookStatus.UNAVAILABLE);

            if (allAvailable) {
                order.setHasPendingRequests(false);
                order.setStatus(OrderStatus.NEW);
                readyOrders.add(order);
                System.out.println("Order #" + order.getId() + " is now ready (book " +
                        availableBook.getTitle() + " is available)");
            }
        }
        requests.removeAll(readyOrders);
        orders.addAll(readyOrders);
    }
}
