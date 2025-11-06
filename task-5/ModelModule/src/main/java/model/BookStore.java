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

    public String makeOrder(List<Book> cart, Customer customer) {
        if (cart == null || cart.isEmpty()) {
            return "Error: The cart is null or empty!";
        }

        Order order = new Order(new ArrayList<>(cart), customer);

        if (containsUnavailableBooks(cart, order)) {
            return handleOrderWithUnavailableBooks(order);
        } else {
            return handleSuccessfulOrder(order);
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

    private String handleOrderWithUnavailableBooks(Order order) {
        order.setHasPendingRequests(true);
        requests.add(order);
        clearCart();
        return "Order #" + order.getId() + " was sent for revision";
    }

    private String handleSuccessfulOrder(Order order) {
        orders.add(order);
        clearCart();
        return "Order #" + order.getId() + " was made successfully";
    }

    public String cancelOrder(int id) {
        if (id <= 0) {
            return "Invalid order ID";
        }

        Order order = findOrderById(id);
        if (order == null) {
            return "Order #" + id + " not found";
        }

        order.setStatus(OrderStatus.CANCELED);
        return "Order #" + id + " was cancelled";
    }

    public String changeOrderStatus(int id, OrderStatus newStatus) {
        if (newStatus == null) {
            return "Error: newStatus is null!";
        }

        Order order = findOrderById(id);
        if (order != null) {
            order.setStatus(newStatus);
            return "Order #" + id + " status changed to " + newStatus;
        } else {
            return "Order #" + id + " not found";
        }
    }

    public boolean changeBookStatus(String title, BookStatus newStatus) {
        Book searchingBook = findBookByTitle(title);
        if (searchingBook == null) {
            return false;
        }

        searchingBook.setStatus(newStatus);

        if (newStatus == BookStatus.AVAILABLE) {
            processBookRequests(searchingBook);
        }
        return true;
    }

    public boolean addBookToWarehouse(Book book) {
        if (isBookNull(book)) return false;

        warehouse.add(book);
        book.setStatus(BookStatus.AVAILABLE);
        processBookRequests(book);
        return true;
    }

    public boolean writeOffBookFromWarehouse(Book book) {
        if (isBookNull(book)) return false;

        book.setStatus(BookStatus.UNAVAILABLE);
        return true;
    }

    public void makeRequestForBook(Book book) {
        if (isBookNull(book)) return;

        if (book.getStatus() == BookStatus.UNAVAILABLE) {
            book.incrementRequestCount();
            System.out.println("Request created for unavailable book: " + book.getTitle());
        }
    }

    public boolean addBookToCart(String title) {
        Book book = findBookByTitle(title);
        if (book == null) return false;
        return cart.add(book);
    }

    public boolean removeBookFromCart(String title) {
        Book book = findBookByTitle(title);
        if (book == null) return false;
        return cart.remove(book);
    }

    private boolean isBookNull(Book book) {
        return book == null;
    }

    public void clearCart() {
        cart.clear();
    }

    public Order findOrderById(int id) {
        for (Order order : orders) {
            if (order.getId() == id) return order;
        }
        for (Order order : requests) {
            if (order.getId() == id) return order;
        }
        return null;
    }

    public Book findBookByTitle(String title) {
        if (title == null) return null;
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

    public List<Book> showBooksSortedBy(String sortedBy) {
        if (warehouse.isEmpty()) return Collections.emptyList();

        return sortBooks(sortedBy, warehouse);
    }

    private List<Book> sortBooks(String sortedBy, List<Book> bookList) {
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
                return null;
        }
        return bookList;
    }


    public List<Order> showOrdersSortedBy(String sortedBy) {
        if (orders.isEmpty()) return Collections.emptyList();

        return sortOrders(sortedBy, orders);
    }

    private List<Order> sortOrders(String sortedBy, List<Order> orderList) {
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
                return null;
        }

        return orderList;
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

    public List<Order> showOrderForDateSortedBy(LocalDate startDate, LocalDate endDate, String sortedBy) {

        List<Order> sortedOrders = findOrdersBetweenDates(startDate, endDate);
        if (sortedOrders.isEmpty()) return Collections.emptyList();

        switch (sortedBy) {
            case "date":
                sortedOrders.sort(Comparator.comparing(Order::getExecutionDate));
                break;
            case "amount":
                sortedOrders.sort(Comparator.comparing(Order::getAmount));
                break;
            default:
                return null;
        }

        return sortedOrders;
    }

    private boolean checkOrdersListEmpty(List<Order> list) {
        if (list.isEmpty()) {
            System.out.println("Error: no orders for this date");
            return true;
        }
        return false;
    }

    public BigDecimal findEarnedFundsOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = findOrdersBetweenDates(startDate, endDate);
        if (checkOrdersListEmpty(sortedOrders)) return new BigDecimal("0");

        BigDecimal totalAmount = new BigDecimal("0");
        for (Order order : sortedOrders) {
            totalAmount = totalAmount.add(order.getAmount());
        }
        return totalAmount;
    }

    public String findNumberCompletedOrdersOverPeriodTime(LocalDate startDate, LocalDate endDate) {
        List<Order> sortedOrders = findOrdersBetweenDates(startDate, endDate);
        if (checkOrdersListEmpty(sortedOrders)) return "No order for his date";

        return "Total completed orders: " + sortedOrders.size();
    }

    public Order showOrderDetails(int id) {
        return findOrderById(id);
    }

    public String showBookDescription(String title) {
        Book rightBook = null;
        for (Book book : warehouse) {
            if (book.getTitle().equals(title)) {
                rightBook = book;
                break;
            }
        }

        if (rightBook == null) {
            return "The book " + title + " not found";
        }

        return "Title: " + rightBook.getTitle() + " (" +
                rightBook.getPublicationDate() + ")" + "\nDescription: " +
                rightBook.getDescription();
    }

    private List<Book> findRequestedBooks() {
        return warehouse.stream()
                .filter(book -> book.getRequestCount() > 0)
                .toList();
    }

    public List<Book> showRequestedBooksSortedBy(String sortedBy) {
        List<Book> requestedBooks = findRequestedBooks();
        if (requestedBooks.isEmpty()) return Collections.emptyList();

        switch (sortedBy) {
            case "count":
                requestedBooks.sort(Comparator.comparingInt(Book::getRequestCount));
                break;
            case "title":
                requestedBooks.sort(Comparator.comparing(Book::getTitle));
                break;
            default:
                return null;
        }

        return requestedBooks;
    }

    private List<Book> findOldBooks() {
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);

        return warehouse.stream()
                .filter(book -> book.getReceiptDate().isBefore(sixMonthsAgo))
                .filter(book -> book.getStatus() == BookStatus.AVAILABLE)
                .toList();
    }

    public List<Book> showOldBooks(String sortedBy) {
        if (warehouse.isEmpty()) return Collections.emptyList();

        List<Book> oldBooks = findOldBooks();

        if (oldBooks.isEmpty()) return Collections.emptyList();

        switch (sortedBy) {
            case "receiptDate":
                oldBooks.sort(Comparator.comparing(Book::getReceiptDate));
                break;
            case "price":
                oldBooks.sort(Comparator.comparing(Book::getPrice));
                break;
            default:
                return null;
        }

        return oldBooks;
    }

}
