package bookstore;

import java.util.*;

public class BookStore {

    private final List<Book> warehouse = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final List<Book> cart = new ArrayList<>();
    private final List<Order> requests = new ArrayList<>();

    public void makeOrder(List<Book> cart) {
        if (cart.isEmpty()) {
            System.out.println("The cart is empty!");
            return;
        }
        Order order = new Order(new ArrayList<>(cart));
        boolean hasUnavailable = false;
        for (Book book : cart) {
            if (!checkStatus(book)) {
                hasUnavailable = true;
                makeRequestForBook(book);
            }
        }
        if (hasUnavailable) {
            order.setHasPendingRequests(true);
            requests.add(order);
            System.out.println("Order #" + order.getId() + " was sent for revision");
            clearCart();
        } else {
            orders.add(order);
            System.out.println("Order #" + order.getId() + " was made successfully");
            clearCart();
        }
    }

    public void cancelOrder(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                order.setStatus(OrderStatus.CANCELED);
                System.out.println("Order #" + id + " was cancelled");
                return;
            }
        }
        for (Order order : requests) {
            if (order.getId() == id) {
                order.setStatus(OrderStatus.CANCELED);
                System.out.println("Order #" + id + " was cancelled");
                return;
            }
        }
        System.out.println("Order #" + id + " not found");
    }

    public void changeOrderStatus(int id, OrderStatus newStatus) {
        Order order = findOrderById(id);
        if (order != null) {
            order.setStatus(newStatus);
            System.out.println("Order #" + id + " status changed to " + newStatus);
        } else {
            System.out.println("Order #" + id + " not found");
        }
    }

    public void changeBookStatus(String title, BookStatus newStatus) {
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
        warehouse.add(book);
        book.setStatus(BookStatus.AVAILABLE);
        System.out.println("The book " + book.getTitle() + " was added to warehouse");
        processBookRequests(book);
    }

    public void writeOffBookFromWarehouse(Book book) {
        book.setStatus(BookStatus.UNAVAILABLE);
        System.out.println("The book " + book.getTitle() + " was written off from the warehouse");
    }

    public void makeRequestForBook(Book book) {
        if (book.getStatus() == BookStatus.UNAVAILABLE) {
            System.out.println("Request created for unavailable book: " + book.getTitle());
        }
    }

    private void processBookRequests(Book availableBook) {
        List<Order> readyOrders = new ArrayList<>();
        for (Order order : requests) {
            boolean containsThisBook = false;
            for (Book b : order.getBooks()) {
                if (b.getTitle().equals(availableBook.getTitle())) {
                    containsThisBook = true;
                    break;
                }
            }
            if (!containsThisBook) {
                continue;
            }
            boolean allAvailable = true;
            for (Book b : order.getBooks()) {
                if (b.getStatus() == BookStatus.UNAVAILABLE) {
                    allAvailable = false;
                    break;
                }
            }
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


    public void addBookToCart(Book book) {
        cart.add(book);
    }

    public void removeBookFromCart(Book book) {
        cart.remove(book);
    }

    public void clearCart() {
        cart.clear();
    }

    public void showCart() {
        System.out.println("\nYour cart:");
        for (Book book : cart) {
            System.out.println(book);
        }
        System.out.println();
    }

    private Order findOrderById(int id) {
        for (Order order : orders) {
            if (order.getId() == id) {
                return order;
            }
        }
        for (Order order : requests) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    private Book findBookByTitle (String title) {
        for (Book book : warehouse) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null;
    }

    private boolean checkStatus(Book book) {
        return book.getStatus() != BookStatus.UNAVAILABLE;
    }
}
