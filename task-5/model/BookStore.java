package model;

import entity.Book;
import entity.Customer;
import entity.Order;
import enums.BookStatus;
import enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class BookStore {

    private final List<Book> warehouse = new ArrayList<>();
    private final List<Order> orders = new ArrayList<>();
    private final List<Book> cart = new ArrayList<>();
    private final List<Order> requests = new ArrayList<>();

    public List<Book> getCart() {
        return cart;
    }

    public void makeOrder(List<Book> cart, Customer customer) {
        if (cart == null || cart.isEmpty()) {
            System.out.println("Error: The cart is null or empty!");
            return;
        }

        Order order = new Order(new ArrayList<>(cart), customer);

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
        if (isBookNull(book)) return;

        warehouse.add(book);
        book.setStatus(BookStatus.AVAILABLE);
        System.out.println("The book " + book.getTitle() + " was added to warehouse");
        processBookRequests(book);
    }

    public void writeOffBookFromWarehouse(Book book) {
        if (isBookNull(book)) return;

        book.setStatus(BookStatus.UNAVAILABLE);
        System.out.println("The book " + book.getTitle() + " was written off from the warehouse");
    }

    public void makeRequestForBook(Book book) {
        if (isBookNull(book)) return;

        if (book.getStatus() == BookStatus.UNAVAILABLE) {
            book.incrementRequestCount();
            System.out.println("Request created for unavailable book: " + book.getTitle());
        }
    }

    public void addBookToCart(Book book) {
        if (isBookNull(book)) return;

        cart.add(book);
    }

    public void removeBookFromCart(Book book) {
        if (isBookNull(book)) return;

        cart.remove(book);
    }

    private boolean isBookNull(Book book) {
        if (book == null) {
            System.out.println("Error: book is null");
            return true;
        }
        return false;
    }

    public void clearCart() {
        cart.clear();
        System.out.println("The cart is empty now");
    }

    public void showCart() {
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty\n");
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

    public Book findBookByTitle(String title) {
        return warehouse.stream()
                .filter(book -> book.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    private boolean checkStatus(Book book) {
        return book.getStatus() != BookStatus.UNAVAILABLE;
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


    // --- NEW ADDED METHODS ---

    public void showBooksSortedBy(String sortedBy) {
        if (warehouse.isEmpty()) {
            System.out.println("Error: warehouse is empty");
            return;
        }

        sortBooks(sortedBy, warehouse);
        printBooks(warehouse);

    }

    private void sortBooks(String sortedBy, List<Book> bookList) {
        switch (sortedBy) {
            case "title":
                bookList.sort(Comparator.comparing(Book::getTitle));
                break;
            case "publicationDate":
                bookList.sort(Comparator.comparing(Book::getPublicationDate));
                break;
            case "price":
                bookList.sort(Comparator.comparing(Book::getPrice));
                break;
            case "status":
                bookList.sort(Comparator.comparing(Book::getStatus));
                break;
            default:
                System.out.println("Not correct sorting parameter");
                return;
        }
    }

    private void printBooks(List<Book> bookList) {
        for (int i = 0; i < bookList.size(); i++) {
            System.out.println((i + 1) + ". " + bookList.get(i));
        }
    }


    public void showOrdersSortedBy(String sortedBy) {
        if (orders.isEmpty()) {
            System.out.println("Error: no orders");
            return;
        }

        sortOrders(sortedBy, orders);
        printOrders(orders);
    }

    private void sortOrders(String sortedBy, List<Order> orderList) {
        switch (sortedBy) {
            case "executionDate":
                orderList.sort(Comparator.comparing(Order::getExecutionDate));
                break;
            case "amount":
                orderList.sort(Comparator.comparing(Order::getAmount));
                break;
            case "status":
                orderList.sort(Comparator.comparing(Order::getStatus));
                break;
            default:
                System.out.println("Not correct sorting parameter");
                return;
        }
    }

    private void printOrders(List<Order> orderList) {
        for (int i = 0; i < orderList.size(); i++) {
            System.out.println((i + 1) + ". " + orderList.get(i));
        }
    }


    private List<Order> findOrdersBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = orders.stream()
                .filter(order -> !order.getExecutionDate().isBefore(startDate))
                .filter(order -> !order.getExecutionDate().isAfter(endDate))
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .toList();

        if (sortedOrders.isEmpty()) {
            System.out.println("Error: no orders for this date");
            return new ArrayList<>();
        } else {
            return sortedOrders;
        }
    }

    public void showOrderForDateSortedBy(LocalDate startDate, LocalDate endDate, String sortedBy) {
        if (orders.isEmpty()) {
            System.out.println("Error: no orders");
            return;
        }

        List<Order> sortedOrders = findOrdersBetweenDates(startDate, endDate);
        if (sortedOrders.isEmpty()) {
            System.out.println("Error: no orders for this date");
            return;
        }

        switch (sortedBy) {
            case "date":
                sortedOrders.sort(Comparator.comparing(Order::getExecutionDate));
                break;
            case "amount":
                sortedOrders.sort(Comparator.comparing(Order::getAmount));
                break;
            default:
                System.out.println("Not correct sorting parameter");
                return;
        }

        for (int i = 0; i < sortedOrders.size(); i++) {
            System.out.println((i + 1) + ". " + sortedOrders.get(i));
        }
    }

    private boolean checkOrdersListEmpty(List<Order> list) {
        if (list.isEmpty()) {
            System.out.println("Error: no orders for this date");
            return true;
        }
        return false;
    }

    public void findEarnedFundsOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = findOrdersBetweenDates(startDate, endDate);
        if (checkOrdersListEmpty(sortedOrders)) return;

        BigDecimal totalAmount = new BigDecimal("0");
        for (Order order : sortedOrders) {
            totalAmount = totalAmount.add(order.getAmount());
        }

        System.out.println("Total amount: " + totalAmount);
    }

    public void findNumberCompletedOrdersOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = findOrdersBetweenDates(startDate, endDate);
        if (checkOrdersListEmpty(sortedOrders)) return;

        System.out.println("Total completed orders: " + sortedOrders.size());
    }

    public void showOrderDetails(int id) {
        Order rightOrder = findOrderById(id);
        if (rightOrder == null) {
            System.out.println("Order #" + id + " not found");
            return;
        }

        System.out.println("Customer: " + rightOrder.getCustomer().getName());
        System.out.println("Email: " + rightOrder.getCustomer().getEmail());
        System.out.println("Books:");
        for (Book book : rightOrder.getBooks()) {
            System.out.println("  - " + book.getTitle() + " (" + book.getPrice() + ")");
        }
    }

    public void showBookDescription(String title) {
        Book rightBook = null;
        for (Book book : warehouse) {
            if (book.getTitle().equals(title)) {
                rightBook = book;
                break;
            }
        }

        if (rightBook == null) {
            System.out.println("The book " + title + " not found");
            return;
        }

        System.out.println("Title: " + rightBook.getTitle() + " (" +
                rightBook.getPublicationDate() + ")" + "\nDescription: " +
                rightBook.getDescription());
    }

    private List<Book> findRequestedBooks() {
        return warehouse.stream()
                .filter(book -> book.getRequestCount() > 0)
                .toList();
    }

    public void showRequestedBooksSortedBy(String sortedBy) {
        List<Book> requestedBooks = findRequestedBooks();
        if (requestedBooks.isEmpty()) {
            System.out.println("No book requests yet");
            return;
        }

        switch (sortedBy) {
            case "count":
                requestedBooks.sort(Comparator.comparingInt(Book::getRequestCount));
                break;
            case "title":
                requestedBooks.sort(Comparator.comparing(Book::getTitle));
                break;
            default:
                System.out.println("Not correct sorting parameter");
                return;
        }

        for (Book book : requestedBooks) {
            System.out.println(book.getTitle() + " — Requests: " + book.getRequestCount());
        }
    }

    private List<Book> findOldBooks() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);

        return warehouse.stream()
                .filter(book -> book.getReceiptDate().isBefore(sixMonthsAgo))
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .toList();
    }

    public void showOldBooks(String sortedBy) {
        if (warehouse.isEmpty()) {
            System.out.println("Error: warehouse is empty");
            return;
        }
        List<Book> oldBooks = findOldBooks();

        if (oldBooks.isEmpty()) {
            System.out.println("No old books");
            return;
        }

        switch (sortedBy) {
            case "receiptDate":
                oldBooks.sort(Comparator.comparing(Book::getReceiptDate));
                break;
            case "price":
                oldBooks.sort(Comparator.comparing(Book::getPrice));
                break;
            default:
                System.out.println("Not correct sorting parameter");
                return;
        }

        printBooks(oldBooks);
    }

}
